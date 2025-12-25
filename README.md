# NanoRaft

# TODO update documentation

[![Maven Central](https://maven-badges.sml.io/maven-central/io.nanoraft/nanoraft/badge.svg?style=for-the-badge)](https://central.sonatype.com/artifact/io.nanoraft/nanoraft)
[![javadoc](https://javadoc.io/badge2/io.nanoraft/nanoraft/javadoc.svg?style=for-the-badge)](https://javadoc.io/doc/io.nanoraft/nanoraft)
[![GitHub](https://img.shields.io/github/license/NanoRaft/NanoRaft?color=brightgreen&style=for-the-badge)](LICENSE)
[![GitHub Workflow Status (with branch)](https://img.shields.io/github/actions/workflow/status/NanoRaft/NanoRaft/check.yml?branch=master&style=for-the-badge)](https://github.com/NanoRaft/NanoRaft/actions/workflows/check.yml?query=branch%3Amaster)

![](https://nanoraft.io/img/nanoraft-logo.png)

NanoRaft is a feature-complete and stable open-source implementation of the
Raft consensus algorithm in Java. __It is a single lightweight JAR file of a few
hundred KBs of size.__ It can be used for building fault tolerant and
strongly-consistent (CP) data, metadata and coordination services. A few
examples of possible use-cases are building distributed file systems, key-value
stores, distributed lock services, etc.

NanoRaft works on top of a minimalistic and modular design. __It is a single
lightweight JAR with a few hundred KBs of size and only logging dependency.__
It contains an isolated implementation of the Raft consensus algorithm, and
a set of accompanying abstractions to run the algorithm in a multi-threaded and
distributed environment. These abstractions are defined to isolate the core
algorithm from the concerns of persistence, thread-safety, serialization,
networking, and actual state machine logic. Users are required to provide their
own implementations of these abstractions to build their custom CP distributed
systems with NanoRaft.

__Please note that NanoRaft is not a high-level solution like a distributed
key-value store or a distributed lock service. It is a core library that offers
a set of abstractions and functionalities to help you build such high-level
systems.__

## Features

NanoRaft implements the leader election, log replication, log compaction
(snapshotting), and cluster membership changes components of the Raft consensus
algorithm. Additionally, it offers a rich set of optimizations and
enhancements:

* Adaptive batching during log replication,
* Back pressure to prevent OOMEs on Raft leader and followers,
* Parallel snapshot transfer from Raft leader and followers,
* Pre-voting and leader stickiness ([Section 4.2.3 of the Raft dissertation](https://github.com/ongardie/dissertation)
  and [Four modifications of the Raft consensus algorithm](https://openlife.cc/system/files/4-modifications-for-Raft-consensus.pdf)),
* Auto-demotion of Raft leader on loss of quorum
  heartbeats [(Section 6.2 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Linearizable quorum reads without appending log
  entries [(Section 6.4 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Lease-based local queries on Raft
  leader [(Section 6.4.1 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Monotonic local queries on Raft
  followers [(Section 6.4.1 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Parallel disk writes on Raft leader and
  followers [(Section 10.2.1 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Leadership transfer [(Section 3.10 of the Raft dissertation)](https://github.com/ongardie/dissertation).
* [Improved majority quorums](https://basri.dev/posts/2020-07-27-improved-majority-quorums-for-raft/)

## Get started

See [the User Guide](https://nanoraft.io/docs/setup).

## Use NanoRaft in your project

Add NanoRaft to your dependency list:

Gradle (version catalog)

```toml
[versions]
nanoraft = "0.9"

[libraries]
nanoraft = { module = "io.nanoraft:nanoraft", version.ref = "nanoraft" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.nanoraft:nanoraft:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.nanoraft</groupId>
    <artifactId>nanoraft</artifactId>
    <version>0.9</version>
</dependency>
```

## Build from source

Pull the latest code with `gh repo clone NanoRaft/NanoRaft`
and build with `cd NanoRaft && ./gradlew build`.

## Source code layout

`nanoraft` module contains the source code of NanoRaft along with its unit
and integration test suite.

`nanoraft-hocon` and `nanoraft-yaml` modules are utility libraries for
parsing HOCON and YAML files to start Raft nodes.

`nanoraft-metrics` module contains the integration with the Micrometer library
for publishing NanoRaft metrics to external systems.

`afloatdb` contains a simple in-memory distributed KV store project built with NanoRaft and gRPC.

`site-src` contains the source files of [nanoraft.io](https://nanoraft.io).

## Contribute to NanoRaft

You can see [this guide](CONTRIBUTING.md) for contributing to NanoRaft.

## License

NanoRaft is available under [the Apache 2 License](https://github.com/NanoRaft/NanoRaft/blob/master/LICENSE).

NanoRaft originates from the Raft implementation that
powers [Hazelcast IMDG's CP Subsystem module](https://github.com/hazelcast/hazelcast/tree/master/hazelcast/src/main/java/com/hazelcast/cp/internal/raft).
You can see [the announcement](https://nanoraft.io/blog/2021-09-03-introducing-nanoraft/) for details.
