# NanoRaft Tutorial

Gradle (version catalog)

```toml
[versions]
nanoraft-tutorial = "0.9"

[libraries]
nanoraft-tutorial = { module = "io.nanoraft:nanoraft-tutorial", version.ref = "nanoraft-tutorial" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.nanoraft:nanoraft-tutorial:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.nanoraft</groupId>
    <artifactId>nanoraft-tutorial</artifactId>
    <version>0.9</version>
</dependency>
```