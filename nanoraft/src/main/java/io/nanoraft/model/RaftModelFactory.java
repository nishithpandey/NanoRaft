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

package io.nanoraft.model;

import javax.annotation.Nonnull;

import io.nanoraft.RaftNode;
import io.nanoraft.lifecycle.RaftNodeLifecycleAware;
import io.nanoraft.model.groupop.UpdateRaftGroupMembersOp.UpdateRaftGroupMembersOpBuilder;
import io.nanoraft.model.impl.DefaultRaftModelFactory;
import io.nanoraft.model.log.LogEntry.LogEntryBuilder;
import io.nanoraft.model.log.RaftGroupMembersView.RaftGroupMembersViewBuilder;
import io.nanoraft.model.log.SnapshotChunk.SnapshotChunkBuilder;
import io.nanoraft.model.log.SnapshotEntry.SnapshotEntryBuilder;
import io.nanoraft.model.message.AppendEntriesFailureResponse.AppendEntriesFailureResponseBuilder;
import io.nanoraft.model.message.AppendEntriesRequest.AppendEntriesRequestBuilder;
import io.nanoraft.model.message.AppendEntriesSuccessResponse.AppendEntriesSuccessResponseBuilder;
import io.nanoraft.model.message.InstallSnapshotRequest.InstallSnapshotRequestBuilder;
import io.nanoraft.model.message.InstallSnapshotResponse.InstallSnapshotResponseBuilder;
import io.nanoraft.model.message.PreVoteRequest.PreVoteRequestBuilder;
import io.nanoraft.model.message.PreVoteResponse.PreVoteResponseBuilder;
import io.nanoraft.model.message.TriggerLeaderElectionRequest.TriggerLeaderElectionRequestBuilder;
import io.nanoraft.model.message.VoteRequest.VoteRequestBuilder;
import io.nanoraft.model.message.VoteResponse.VoteResponseBuilder;
import io.nanoraft.model.persistence.RaftEndpointPersistentState.RaftEndpointPersistentStateBuilder;
import io.nanoraft.model.persistence.RaftTermPersistentState.RaftTermPersistentStateBuilder;
import io.nanoraft.persistence.RaftStore;
import io.nanoraft.transport.Transport;

/**
 * Used for creating {@link RaftModel} objects with the builder pattern.
 * <p>
 * Users of NanoRaft can provide an implementation of this interface while
 * creating {@link RaftNode} instances. Otherwise,
 * {@link DefaultRaftModelFactory} is used. {@link RaftModel} objects created by
 * a Raft model factory implementation are passed to {@link Transport} for
 * networking, and {@link RaftStore} for persistence.
 * <p>
 * A {@link RaftModelFactory} implementation can implement
 * {@link RaftNodeLifecycleAware} to perform initialization and clean up work
 * during {@link RaftNode} startup and termination. {@link RaftNode} calls
 * {@link RaftNodeLifecycleAware#onRaftNodeStart()} before calling any other
 * method on {@link RaftModelFactory}, and finally calls
 * {@link RaftNodeLifecycleAware#onRaftNodeTerminate()} on termination.
 */
public interface RaftModelFactory {

    @Nonnull
    LogEntryBuilder createLogEntryBuilder();

    @Nonnull
    SnapshotEntryBuilder createSnapshotEntryBuilder();

    @Nonnull
    SnapshotChunkBuilder createSnapshotChunkBuilder();

    @Nonnull
    AppendEntriesRequestBuilder createAppendEntriesRequestBuilder();

    @Nonnull
    AppendEntriesSuccessResponseBuilder createAppendEntriesSuccessResponseBuilder();

    @Nonnull
    AppendEntriesFailureResponseBuilder createAppendEntriesFailureResponseBuilder();

    @Nonnull
    InstallSnapshotRequestBuilder createInstallSnapshotRequestBuilder();

    @Nonnull
    InstallSnapshotResponseBuilder createInstallSnapshotResponseBuilder();

    @Nonnull
    PreVoteRequestBuilder createPreVoteRequestBuilder();

    @Nonnull
    PreVoteResponseBuilder createPreVoteResponseBuilder();

    @Nonnull
    TriggerLeaderElectionRequestBuilder createTriggerLeaderElectionRequestBuilder();

    @Nonnull
    VoteRequestBuilder createVoteRequestBuilder();

    @Nonnull
    VoteResponseBuilder createVoteResponseBuilder();

    @Nonnull
    UpdateRaftGroupMembersOpBuilder createUpdateRaftGroupMembersOpBuilder();

    @Nonnull
    RaftGroupMembersViewBuilder createRaftGroupMembersViewBuilder();

    @Nonnull
    RaftEndpointPersistentStateBuilder createRaftEndpointPersistentStateBuilder();

    @Nonnull
    RaftTermPersistentStateBuilder createRaftTermPersistentStateBuilder();

}
