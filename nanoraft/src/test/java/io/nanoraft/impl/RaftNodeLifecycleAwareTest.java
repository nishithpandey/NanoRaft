/*
 * Copyright (c) 2020, NanoRaft.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nanoraft.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.nanoraft.RaftEndpoint;
import io.nanoraft.RaftNode;
import io.nanoraft.RaftNodeStatus;
import io.nanoraft.executor.impl.DefaultRaftNodeExecutor;
import io.nanoraft.impl.local.LocalRaftEndpoint;
import io.nanoraft.lifecycle.RaftNodeLifecycleAware;
import io.nanoraft.model.groupop.UpdateRaftGroupMembersOp.UpdateRaftGroupMembersOpBuilder;
import io.nanoraft.model.impl.DefaultRaftModelFactory;
import io.nanoraft.model.log.LogEntry;
import io.nanoraft.model.log.LogEntry.LogEntryBuilder;
import io.nanoraft.model.log.RaftGroupMembersView;
import io.nanoraft.model.log.SnapshotChunk;
import io.nanoraft.model.log.SnapshotChunk.SnapshotChunkBuilder;
import io.nanoraft.model.log.SnapshotEntry.SnapshotEntryBuilder;
import io.nanoraft.model.message.AppendEntriesFailureResponse.AppendEntriesFailureResponseBuilder;
import io.nanoraft.model.message.AppendEntriesRequest;
import io.nanoraft.model.message.AppendEntriesSuccessResponse;
import io.nanoraft.model.message.InstallSnapshotRequest.InstallSnapshotRequestBuilder;
import io.nanoraft.model.message.InstallSnapshotResponse.InstallSnapshotResponseBuilder;
import io.nanoraft.model.message.PreVoteRequest.PreVoteRequestBuilder;
import io.nanoraft.model.message.PreVoteResponse.PreVoteResponseBuilder;
import io.nanoraft.model.message.RaftMessage;
import io.nanoraft.model.message.TriggerLeaderElectionRequest.TriggerLeaderElectionRequestBuilder;
import io.nanoraft.model.message.VoteRequest.VoteRequestBuilder;
import io.nanoraft.model.message.VoteResponse.VoteResponseBuilder;
import io.nanoraft.model.persistence.RaftEndpointPersistentState;
import io.nanoraft.model.persistence.RaftTermPersistentState;
import io.nanoraft.persistence.RaftStore;
import io.nanoraft.report.RaftNodeReport;
import io.nanoraft.report.RaftNodeReportListener;
import io.nanoraft.statemachine.StateMachine;
import io.nanoraft.test.util.BaseTest;
import io.nanoraft.transport.Transport;

public class RaftNodeLifecycleAwareTest extends BaseTest {

    private final RaftEndpoint localEndpoint = LocalRaftEndpoint.newEndpoint();
    private final List<RaftEndpoint> initialMembers = List.of(localEndpoint, LocalRaftEndpoint.newEndpoint(),
            LocalRaftEndpoint.newEndpoint());

    private final DelegatingRaftNodeExecutor executor = new DelegatingRaftNodeExecutor();
    private final NopTransport transport = new NopTransport();
    private final NopStateMachine stateMachine = new NopStateMachine();
    private final DelegatingRaftModelFactory modelFactory = new DelegatingRaftModelFactory();
    private final NopRaftNodeReportListener reportListener = new NopRaftNodeReportListener();
    private final NopRaftStore store = new NopRaftStore();

    private RaftNode raftNode;

    @Before
    public void init() {
        raftNode = RaftNode.newBuilder().setGroupId("default").setLocalEndpoint(localEndpoint)
                .setInitialGroupMembers(initialMembers).setExecutor(executor).setTransport(transport)
                .setStateMachine(stateMachine).setModelFactory(modelFactory).setRaftNodeReportListener(reportListener)
                .setStore(store).build();
    }

    @After
    public void tearDown() {
        if (raftNode != null) {
            raftNode.terminate();
        }
    }

    @Test
    public void testExecutorStart() {
        raftNode.start().join();

        assertThat(executor.startCall).isGreaterThan(0);
        assertThat(executor.executeCall).isGreaterThan(0);
        assertThat(executor.executeCall).isLessThan(executor.startCall);
    }

    @Test
    public void testExecutorTerminate() {
        raftNode.start().join();
        raftNode.terminate().join();

        assertThat(executor.startCall).isGreaterThan(0);
        assertThat(executor.executeCall).isGreaterThan(0);
        assertThat(executor.terminateCall).isGreaterThan(0);
        assertThat(executor.executeCall).isLessThan(executor.startCall);
        assertThat(executor.lastExecuteCall).isLessThan(executor.terminateCall);
    }

    @Test
    public void testTransportStart() {
        raftNode.start().join();

        assertThat(transport.startCall).isGreaterThan(0);
        assertThat(transport.sendCall).isGreaterThan(0);
        assertThat(transport.startCall).isLessThan(transport.sendCall);
    }

    @Test
    public void testTransportTerminate() {
        raftNode.start().join();
        raftNode.terminate().join();

        assertThat(transport.startCall).isGreaterThan(0);
        assertThat(transport.sendCall).isGreaterThan(0);
        assertThat(transport.terminateCall).isGreaterThan(0);
        assertThat(transport.startCall).isLessThan(transport.sendCall);
        assertThat(transport.sendCall).isLessThan(transport.terminateCall);
        assertThat(transport.lastSendCall).isLessThan(transport.terminateCall);
    }

    @Test
    public void testStateMachineStart() {
        raftNode.start().join();

        assertThat(stateMachine.startCall).isGreaterThan(0);
    }

    @Test
    public void testStateMachineTerminate() {
        raftNode.start().join();
        raftNode.terminate().join();

        assertThat(stateMachine.startCall).isGreaterThan(0);
        assertThat(stateMachine.terminateCall).isGreaterThan(0);
        assertThat(stateMachine.startCall).isLessThan(stateMachine.terminateCall);
    }

    @Test
    public void testModelFactoryStart() {
        raftNode.start().join();

        assertThat(modelFactory.startCall).isGreaterThan(0);
        assertThat(modelFactory.createCall).isGreaterThan(0);
        assertThat(modelFactory.startCall).isLessThan(modelFactory.createCall);
    }

    @Test
    public void testModelFactoryTerminate() {
        raftNode.start().join();
        raftNode.terminate().join();

        assertThat(modelFactory.startCall).isGreaterThan(0);
        assertThat(modelFactory.createCall).isGreaterThan(0);
        assertThat(modelFactory.terminateCall).isGreaterThan(0);
        assertThat(modelFactory.startCall).isLessThan(modelFactory.createCall);
        assertThat(modelFactory.createCall).isLessThan(modelFactory.terminateCall);
        assertThat(modelFactory.lastCreateCall).isLessThan(modelFactory.terminateCall);
    }

    @Test
    public void testReportListenerStart() {
        raftNode.start().join();

        assertThat(reportListener.startCall).isGreaterThan(0);
        assertThat(reportListener.acceptCall).isGreaterThan(0);
        assertThat(reportListener.startCall).isLessThan(reportListener.acceptCall);
    }

    @Test
    public void testReportListenerTerminate() {
        raftNode.start().join();
        raftNode.terminate().join();

        assertThat(reportListener.startCall).isGreaterThan(0);
        assertThat(reportListener.acceptCall).isGreaterThan(0);
        assertThat(reportListener.terminateCall).isGreaterThan(0);
        assertThat(reportListener.startCall).isLessThan(reportListener.acceptCall);
        assertThat(reportListener.acceptCall).isLessThan(reportListener.terminateCall);
        assertThat(reportListener.lastAcceptCall).isLessThan(reportListener.terminateCall);
    }

    @Test
    public void testStoreStart() {
        raftNode.start().join();

        assertThat(store.startCall).isGreaterThan(0);
        assertThat(store.persistCall).isGreaterThan(0);
        assertThat(store.startCall).isLessThan(store.persistCall);
    }

    @Test
    public void testStoreTerminate() {
        raftNode.start().join();
        raftNode.terminate().join();

        assertThat(store.startCall).isGreaterThan(0);
        assertThat(store.persistCall).isGreaterThan(0);
        assertThat(store.terminateCall).isGreaterThan(0);
        assertThat(store.startCall).isLessThan(store.persistCall);
        assertThat(store.persistCall).isLessThan(store.terminateCall);
        assertThat(store.lastPersistCall).isLessThan(store.terminateCall);
    }

    @Test
    public void testTerminateCalledForAllComponentsWhenStartFails() {
        stateMachine.failOnStart = true;
        try {
            raftNode.start().join();
            fail("Start should fail when any component fails on start");
        } catch (CompletionException ignored) {
        }

        assertThat(raftNode.getStatus()).isEqualTo(RaftNodeStatus.TERMINATED);
        assertThat(stateMachine.terminateCall).isGreaterThan(0);
        if (executor.startCall > 0) {
            assertThat(executor.terminateCall).isGreaterThan(0);
        }
        if (transport.startCall > 0) {
            assertThat(transport.terminateCall).isGreaterThan(0);
        }
        if (store.startCall > 0) {
            assertThat(store.terminateCall).isGreaterThan(0);
        }
        if (modelFactory.startCall > 0) {
            assertThat(modelFactory.terminateCall).isGreaterThan(0);
        }
        if (reportListener.startCall > 0) {
            assertThat(reportListener.terminateCall).isGreaterThan(0);
        }
    }

    @Test
    public void testTerminateCalledForAllComponentsWhenStartAndTerminateFails() {
        stateMachine.failOnStart = true;
        stateMachine.failOnTerminate = true;
        try {
            raftNode.start().join();
            fail("Start should fail when any component fails on start");
        } catch (CompletionException ignored) {
        }

        assertThat(raftNode.getStatus()).isEqualTo(RaftNodeStatus.TERMINATED);
        assertThat(stateMachine.terminateCall).isGreaterThan(0);
        if (executor.startCall > 0) {
            assertThat(executor.terminateCall).isGreaterThan(0);
        }
        if (transport.startCall > 0) {
            assertThat(transport.terminateCall).isGreaterThan(0);
        }
        if (store.startCall > 0) {
            assertThat(store.terminateCall).isGreaterThan(0);
        }
        if (modelFactory.startCall > 0) {
            assertThat(modelFactory.terminateCall).isGreaterThan(0);
        }
        if (reportListener.startCall > 0) {
            assertThat(reportListener.terminateCall).isGreaterThan(0);
        }
    }

    private static class NopTransport implements Transport, RaftNodeLifecycleAware {
        private volatile int startCall;
        private volatile int terminateCall;
        private volatile int sendCall;
        private volatile int lastSendCall;
        private volatile int callOrder;

        @Override
        public void onRaftNodeStart() {
            if (startCall == 0) {
                startCall = ++callOrder;
            }
        }

        @Override
        public void onRaftNodeTerminate() {
            if (terminateCall == 0) {
                terminateCall = ++callOrder;
            }
        }

        @Override
        public void send(@Nonnull RaftEndpoint target, @Nonnull RaftMessage message) {
            lastSendCall = ++callOrder;
            if (sendCall == 0) {
                sendCall = lastSendCall;
            }
        }

        @Override
        public boolean isReachable(@Nonnull RaftEndpoint endpoint) {
            return false;
        }
    }

    private static class NopStateMachine implements StateMachine, RaftNodeLifecycleAware {
        private volatile int startCall;
        private volatile int terminateCall;
        private volatile int callOrder;
        private volatile boolean failOnStart;
        private volatile boolean failOnTerminate;

        @Override
        public void onRaftNodeStart() {
            if (startCall == 0) {
                startCall = ++callOrder;
            }
            if (failOnStart) {
                throw new RuntimeException("failed on purpose!");
            }
        }

        @Override
        public void onRaftNodeTerminate() {
            if (terminateCall == 0) {
                terminateCall = ++callOrder;
            }
            if (failOnTerminate) {
                throw new RuntimeException("failed on purpose!");
            }
        }

        @Override
        public Object runOperation(long commitIndex, @Nonnull Object operation) {
            return null;
        }

        @Override
        public void takeSnapshot(long commitIndex, Consumer<Object> snapshotChunkConsumer) {
        }

        @Override
        public void installSnapshot(long commitIndex, @Nonnull List<Object> snapshotChunks) {
        }

        @Nonnull
        @Override
        public Object getNewTermOperation() {
            return null;
        }
    }

    private static class DelegatingRaftNodeExecutor extends DefaultRaftNodeExecutor implements RaftNodeLifecycleAware {
        private volatile int startCall;
        private volatile int terminateCall;
        private volatile int executeCall;
        private volatile int lastExecuteCall;
        private volatile int callOrder;

        @Override
        public void onRaftNodeStart() {
            if (startCall == 0) {
                startCall = ++callOrder;
            }
        }

        @Override
        public void onRaftNodeTerminate() {
            if (terminateCall == 0) {
                terminateCall = ++callOrder;
            }
        }

        @Override
        public void execute(@Nonnull Runnable task) {
            lastExecuteCall = ++callOrder;
            if (executeCall == 0) {
                executeCall = lastExecuteCall;
            }
            super.execute(task);
        }

        @Override
        public void submit(@Nonnull Runnable task) {
            lastExecuteCall = ++callOrder;
            if (executeCall == 0) {
                executeCall = lastExecuteCall;
            }
            super.submit(task);
        }

        @Override
        public void schedule(@Nonnull Runnable task, long delay, @Nonnull TimeUnit timeUnit) {
            lastExecuteCall = ++callOrder;
            if (executeCall == 0) {
                executeCall = lastExecuteCall;
            }
            super.schedule(task, delay, timeUnit);
        }
    }

    public static class DelegatingRaftModelFactory extends DefaultRaftModelFactory implements RaftNodeLifecycleAware {
        private volatile int startCall;
        private volatile int terminateCall;
        private volatile int createCall;
        private volatile int lastCreateCall;
        private volatile int callOrder;

        @Override
        public void onRaftNodeStart() {
            if (startCall == 0) {
                startCall = ++callOrder;
            }
        }

        @Override
        public void onRaftNodeTerminate() {
            if (terminateCall == 0) {
                terminateCall = ++callOrder;
            }
        }

        @Nonnull
        @Override
        public LogEntryBuilder createLogEntryBuilder() {
            recordCall();
            return super.createLogEntryBuilder();
        }

        @Nonnull
        @Override
        public SnapshotEntryBuilder createSnapshotEntryBuilder() {
            recordCall();
            return super.createSnapshotEntryBuilder();
        }

        @Nonnull
        @Override
        public SnapshotChunkBuilder createSnapshotChunkBuilder() {
            recordCall();
            return super.createSnapshotChunkBuilder();
        }

        @Nonnull
        @Override
        public AppendEntriesRequest.AppendEntriesRequestBuilder createAppendEntriesRequestBuilder() {
            recordCall();
            return super.createAppendEntriesRequestBuilder();
        }

        @Nonnull
        @Override
        public AppendEntriesSuccessResponse.AppendEntriesSuccessResponseBuilder createAppendEntriesSuccessResponseBuilder() {
            recordCall();
            return super.createAppendEntriesSuccessResponseBuilder();
        }

        @Nonnull
        @Override
        public AppendEntriesFailureResponseBuilder createAppendEntriesFailureResponseBuilder() {
            recordCall();
            return super.createAppendEntriesFailureResponseBuilder();
        }

        @Nonnull
        @Override
        public InstallSnapshotRequestBuilder createInstallSnapshotRequestBuilder() {
            recordCall();
            return super.createInstallSnapshotRequestBuilder();
        }

        @Nonnull
        @Override
        public InstallSnapshotResponseBuilder createInstallSnapshotResponseBuilder() {
            recordCall();
            return super.createInstallSnapshotResponseBuilder();
        }

        @Nonnull
        @Override
        public PreVoteRequestBuilder createPreVoteRequestBuilder() {
            recordCall();
            return super.createPreVoteRequestBuilder();
        }

        @Nonnull
        @Override
        public PreVoteResponseBuilder createPreVoteResponseBuilder() {
            recordCall();
            return super.createPreVoteResponseBuilder();
        }

        @Nonnull
        @Override
        public TriggerLeaderElectionRequestBuilder createTriggerLeaderElectionRequestBuilder() {
            recordCall();
            return super.createTriggerLeaderElectionRequestBuilder();
        }

        @Nonnull
        @Override
        public VoteRequestBuilder createVoteRequestBuilder() {
            recordCall();
            return super.createVoteRequestBuilder();
        }

        @Nonnull
        @Override
        public VoteResponseBuilder createVoteResponseBuilder() {
            recordCall();
            return super.createVoteResponseBuilder();
        }

        @Nonnull
        @Override
        public UpdateRaftGroupMembersOpBuilder createUpdateRaftGroupMembersOpBuilder() {
            recordCall();
            return super.createUpdateRaftGroupMembersOpBuilder();
        }

        private void recordCall() {
            lastCreateCall = ++callOrder;
            if (createCall == 0) {
                createCall = lastCreateCall;
            }
        }
    }

    private static class NopRaftNodeReportListener implements RaftNodeReportListener, RaftNodeLifecycleAware {
        private volatile int startCall;
        private volatile int terminateCall;
        private volatile int acceptCall;
        private volatile int lastAcceptCall;
        private volatile int callOrder;

        @Override
        public void onRaftNodeStart() {
            if (startCall == 0) {
                startCall = ++callOrder;
            }
        }

        @Override
        public void onRaftNodeTerminate() {
            if (terminateCall == 0) {
                terminateCall = ++callOrder;
            }
        }

        @Override
        public void accept(RaftNodeReport report) {
            lastAcceptCall = ++callOrder;
            if (acceptCall == 0) {
                acceptCall = lastAcceptCall;
            }
        }
    }

    private static class NopRaftStore implements RaftStore, RaftNodeLifecycleAware {
        private volatile int startCall;
        private volatile int terminateCall;
        private volatile int persistCall;
        private volatile int lastPersistCall;
        private volatile int callOrder;

        @Override
        public void onRaftNodeStart() {
            if (startCall == 0) {
                startCall = ++callOrder;
            }
        }

        @Override
        public void onRaftNodeTerminate() {
            if (terminateCall == 0) {
                terminateCall = ++callOrder;
            }
        }

        @Override
        public void persistAndFlushLocalEndpoint(@Nonnull RaftEndpointPersistentState localEndpointPersistentState) {
            recordCall();
        }

        @Override
        public void persistAndFlushInitialGroupMembers(@Nonnull RaftGroupMembersView initialGroupMembers) {
            recordCall();
        }

        @Override
        public void persistAndFlushTerm(@Nonnull RaftTermPersistentState termPersistentState) {
            recordCall();
        }

        @Override
        public void persistLogEntries(@Nonnull List<LogEntry> logEntries) throws IOException {
            recordCall();
        }

        @Override
        public void persistSnapshotChunk(@Nonnull SnapshotChunk snapshotChunk) {
            recordCall();
        }

        @Override
        public void truncateLogEntriesFrom(long logIndexInclusive) {
            recordCall();
        }

        @Override
        public void truncateLogEntriesUntil(long logIndexInclusive) {
            recordCall();
        }

        @Override
        public void deleteSnapshotChunks(long logIndex, int snapshotChunkCount) {
            recordCall();
        }

        @Override
        public void flush() {
            recordCall();
        }

        private void recordCall() {
            lastPersistCall = ++callOrder;
            if (persistCall == 0) {
                persistCall = lastPersistCall;
            }
        }

    }

}
