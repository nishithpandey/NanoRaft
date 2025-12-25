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

package io.nanoraft.impl.task;

import static io.nanoraft.RaftNodeStatus.INITIAL;
import static io.nanoraft.RaftNodeStatus.isTerminal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nanoraft.RaftEndpoint;
import io.nanoraft.RaftNodeStatus;
import io.nanoraft.impl.RaftNodeImpl;
import io.nanoraft.impl.state.RaftState;
import io.nanoraft.model.RaftModelFactory;

/**
 * The base class for the tasks that should not run on some Raft node statuses.
 * <p>
 * Subclass tasks are executed only if the local Raft node is already started,
 * and not terminated or left the Raft group.
 */
public abstract class RaftNodeStatusAwareTask implements Runnable {

    protected final RaftNodeImpl node;
    protected final RaftState state;
    protected final RaftModelFactory modelFactory;

    protected RaftNodeStatusAwareTask(RaftNodeImpl node) {
        this.node = node;
        this.state = node.state();
        this.modelFactory = node.getModelFactory();
    }

    @Override
    public final void run() {
        RaftNodeStatus status = node.getStatus();
        if (isTerminal(status) || status == INITIAL) {
            getLogger().debug("{} Won't run, since status is {}", localEndpointStr(), status);
            return;
        }

        try {
            doRun();
        } catch (Throwable e) {
            getLogger().error(localEndpointStr() + " got a failure in " + getClass().getSimpleName(), e);
        }
    }

    protected abstract void doRun();

    private Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    protected final RaftEndpoint localEndpoint() {
        return node.getLocalEndpoint();
    }

    protected final String localEndpointStr() {
        return node.localEndpointStr();
    }

}
