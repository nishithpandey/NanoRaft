# NanoRaft metrics with Micrometer integration

Add the following dependency to the classpath for publishing NanoRaft metrics
to external monitoring systems via
<a href="https://micrometer.io/" target="_blank">Micrometer</a>.

Gradle (version catalog)

```toml
[versions]
nanoraft-metrics = "0.9"

[libraries]
nanoraft-metrics = { module = "io.nanoraft:nanoraft-metrics", version.ref = "nanoraft-metrics" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.nanoraft:nanoraft-metrics:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.nanoraft</groupId>
    <artifactId>nanoraft-metrics</artifactId>
    <version>0.9</version>
</dependency>
```

<a href="https://github.com/NanoRaft/NanoRaft/blob/master/nanoraft-metrics/src/main/java/io/nanoraft/metrics/RaftNodeMetrics.java" target="_blank">
`RaftNodeMetrics`</a>
implements the
<a href="https://github.com/NanoRaft/NanoRaft/blob/master/nanoraft/src/main/java/io/nanoraft/report/RaftNodeReportListener.java" target="_blank">
`RaftNodeReportListener`</a>
interface and can be injected into created `RaftNode` instances via
`RaftNodeBuilder.setRaftNodeReportListener()`. Then, several metrics extracted
from published `RaftNodeReport` objects are passed to meter registries. 
