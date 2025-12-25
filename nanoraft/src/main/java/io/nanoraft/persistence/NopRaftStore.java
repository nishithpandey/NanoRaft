/*
 * Original work Copyright (c) 2008-2020, Hazelcast, Inc.
 * Modified work Copyright (c) 2020, NanoRaft.
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

package io.nanoraft.persistence;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import io.nanoraft.model.log.LogEntry;
import io.nanoraft.model.log.RaftGroupMembersView;
import io.nanoraft.model.log.SnapshotChunk;
import io.nanoraft.model.persistence.RaftEndpointPersistentState;
import io.nanoraft.model.persistence.RaftTermPersistentState;

/**
 * Used when a Raft node works transiently (its state is not persisted).
 */
public class NopRaftStore implements RaftStore {

    @Override
    public void persistAndFlushLocalEndpoint(@Nonnull RaftEndpointPersistentState localEndpointPersistentState) {

    }

    @Override
    public void persistAndFlushInitialGroupMembers(@Nonnull RaftGroupMembersView initialGroupMembers) {
    }

    @Override
    public void persistAndFlushTerm(@Nonnull RaftTermPersistentState termPersistentState) {
    }

    @Override
    public void persistLogEntries(@Nonnull List<LogEntry> logEntries) throws IOException {
    }

    @Override
    public void persistSnapshotChunk(@Nonnull SnapshotChunk snapshotChunk) {
    }

    @Override
    public void truncateLogEntriesFrom(long logIndexInclusive) {
    }

    @Override
    public void truncateLogEntriesUntil(long logIndexInclusive) {
    }

    @Override
    public void deleteSnapshotChunks(long logIndex, int snapshotChunkCount) {
    }

    @Override
    public void flush() {
    }

}
