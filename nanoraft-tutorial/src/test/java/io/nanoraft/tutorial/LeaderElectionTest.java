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

package io.nanoraft.tutorial;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.nanoraft.RaftNode;
import io.nanoraft.statemachine.StateMachine;
import io.nanoraft.tutorial.atomicregister.AtomicRegister;

/*

   TO RUN THIS TEST ON YOUR MACHINE:

   $ gh repo clone NanoRaft/NanoRaft
   $ cd NanoRaft && ./mvnw clean test -Dtest=io.nanoraft.tutorial.LeaderElectionTest -DfailIfNoTests=false -Ptutorial

   YOU CAN SEE THIS CLASS AT:

   https://github.com/NanoRaft/NanoRaft/blob/master/nanoraft-tutorial/src/test/java/io/nanoraft/tutorial/LeaderElectionTest.java

 */
public class LeaderElectionTest extends BaseLocalTest {

    @Override
    protected StateMachine createStateMachine() {
        return new AtomicRegister();
    }

    @Test
    public void testLeaderElection() {
        RaftNode leader = waitUntilLeaderElected();

        assertThat(leader).isNotNull();

        System.out.println(leader.getLocalEndpoint().getId() + " is the leader!");
    }

}
