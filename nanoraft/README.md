# NanoRaft

This module contains the source code of NanoRaft along with its unit and
integration test suite.

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