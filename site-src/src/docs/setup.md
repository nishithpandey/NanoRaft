
# Setup

NanoRaft JARs are available via Maven Central. If you are
using Gradle or Maven, just add the following lines to your your build tool dependency config:

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

If you don't have Gradle but want to build the project on your machine, ./gradlew is
available in the NanoRaft repository. Just hit the following command on your
terminal.

```
gh repo clone NanoRaft/NanoRaft && cd NanoRaft && ./gradlew build
``` 

Then you can get the JARs from `nanoraft/build/libs`, `nanoraft-hocon/build/libs`, and
`nanoraft-yaml/build/libs` directories.

-----

## Logging

NanoRaft depends on the <a href="http://www.slf4j.org/" target="_blank">SLF4J
library</a> for logging. Actually it is the only dependency of NanoRaft. Make
sure you enable the `INFO` logging level for the `io.nanoraft` package. If you
are going hard, you can also give the `DEBUG` level a shot, but I assure you it
will be a bumpy ride.

-----

## What is next?

OK. You have the NanoRaft JAR in your classpath, and the logging also looks
good. Now you are ready to build your CP distributed system! Then why don't you
start with [checking out the main abstractions](main-abstractions.md) defined in
NanoRaft?
