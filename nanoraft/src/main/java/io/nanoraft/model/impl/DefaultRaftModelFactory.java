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

package io.nanoraft.model.impl;

import javax.annotation.Nonnull;

import io.nanoraft.model.RaftModel;
import io.nanoraft.model.RaftModelFactory;
import io.nanoraft.model.groupop.UpdateRaftGroupMembersOp.UpdateRaftGroupMembersOpBuilder;
import io.nanoraft.model.impl.groupop.DefaultUpdateRaftGroupMembersOpOrBuilder;
import io.nanoraft.model.impl.log.DefaultLogEntryOrBuilder;
import io.nanoraft.model.impl.log.DefaultRaftGroupMembersViewOrBuilder;
import io.nanoraft.model.impl.log.DefaultSnapshotChunkOrBuilder;
import io.nanoraft.model.impl.log.DefaultSnapshotEntryOrBuilder;
import io.nanoraft.model.impl.message.DefaultAppendEntriesFailureResponseOrBuilder;
import io.nanoraft.model.impl.message.DefaultAppendEntriesRequestOrBuilder;
import io.nanoraft.model.impl.message.DefaultAppendEntriesSuccessResponseOrBuilder;
import io.nanoraft.model.impl.message.DefaultInstallSnapshotRequestOrBuilder;
import io.nanoraft.model.impl.message.DefaultInstallSnapshotResponseOrBuilder;
import io.nanoraft.model.impl.message.DefaultPreVoteRequestOrBuilder;
import io.nanoraft.model.impl.message.DefaultPreVoteResponseOrBuilder;
import io.nanoraft.model.impl.message.DefaultTriggerLeaderElectionRequestOrBuilder;
import io.nanoraft.model.impl.message.DefaultVoteRequestOrBuilder;
import io.nanoraft.model.impl.message.DefaultVoteResponseOrBuilder;
import io.nanoraft.model.impl.persistence.DefaultRaftEndpointPersistentStateOrBuilder;
import io.nanoraft.model.impl.persistence.DefaultRaftTermPersistentStateOrBuilder;
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

/**
 * The default implementation of {@link RaftModelFactory}.
 * <p>
 * Creates POJO-style implementations of the {@link RaftModel} objects.
 */
public class DefaultRaftModelFactory implements RaftModelFactory {

    @Nonnull
    @Override
    public LogEntryBuilder createLogEntryBuilder() {
        return new DefaultLogEntryOrBuilder();
    }

    @Nonnull
    @Override
    public SnapshotEntryBuilder createSnapshotEntryBuilder() {
        return new DefaultSnapshotEntryOrBuilder();
    }

    @Nonnull
    @Override
    public SnapshotChunkBuilder createSnapshotChunkBuilder() {
        return new DefaultSnapshotChunkOrBuilder();
    }

    @Nonnull
    @Override
    public AppendEntriesRequestBuilder createAppendEntriesRequestBuilder() {
        return new DefaultAppendEntriesRequestOrBuilder();
    }

    @Nonnull
    @Override
    public AppendEntriesSuccessResponseBuilder createAppendEntriesSuccessResponseBuilder() {
        return new DefaultAppendEntriesSuccessResponseOrBuilder();
    }

    @Nonnull
    @Override
    public AppendEntriesFailureResponseBuilder createAppendEntriesFailureResponseBuilder() {
        return new DefaultAppendEntriesFailureResponseOrBuilder();
    }

    @Nonnull
    @Override
    public InstallSnapshotRequestBuilder createInstallSnapshotRequestBuilder() {
        return new DefaultInstallSnapshotRequestOrBuilder();
    }

    @Nonnull
    @Override
    public InstallSnapshotResponseBuilder createInstallSnapshotResponseBuilder() {
        return new DefaultInstallSnapshotResponseOrBuilder();
    }

    @Nonnull
    @Override
    public PreVoteRequestBuilder createPreVoteRequestBuilder() {
        return new DefaultPreVoteRequestOrBuilder();
    }

    @Nonnull
    @Override
    public PreVoteResponseBuilder createPreVoteResponseBuilder() {
        return new DefaultPreVoteResponseOrBuilder();
    }

    @Nonnull
    @Override
    public TriggerLeaderElectionRequestBuilder createTriggerLeaderElectionRequestBuilder() {
        return new DefaultTriggerLeaderElectionRequestOrBuilder();
    }

    @Nonnull
    @Override
    public VoteRequestBuilder createVoteRequestBuilder() {
        return new DefaultVoteRequestOrBuilder();
    }

    @Nonnull
    @Override
    public VoteResponseBuilder createVoteResponseBuilder() {
        return new DefaultVoteResponseOrBuilder();
    }

    @Nonnull
    @Override
    public UpdateRaftGroupMembersOpBuilder createUpdateRaftGroupMembersOpBuilder() {
        return new DefaultUpdateRaftGroupMembersOpOrBuilder();
    }

    @Nonnull
    @Override
    public RaftGroupMembersViewBuilder createRaftGroupMembersViewBuilder() {
        return new DefaultRaftGroupMembersViewOrBuilder();
    }

    @Nonnull
    @Override
    public RaftEndpointPersistentStateBuilder createRaftEndpointPersistentStateBuilder() {
        return new DefaultRaftEndpointPersistentStateOrBuilder();
    }

    @Nonnull
    @Override
    public RaftTermPersistentStateBuilder createRaftTermPersistentStateBuilder() {
        return new DefaultRaftTermPersistentStateOrBuilder();
    }

}
