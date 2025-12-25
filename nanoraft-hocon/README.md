# NanoRaft HOCON Config Parser

Gradle (version catalog)

```toml
[versions]
nanoraft-hocon = "0.9"

[libraries]
nanoraft-hocon = { module = "io.nanoraft:nanoraft-hocon", version.ref = "nanoraft-hocon" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.nanoraft:nanoraft-hocon:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.nanoraft</groupId>
    <artifactId>nanoraft-hocon</artifactId>
    <version>0.9</version>
</dependency>
```

This project enables you to create `RaftConfig` objects from HOCON files
easily, as shown below:

```
String configFilePath = "...";
Config hoconConfig = ConfigFactory.parseFile(new File(configFilePath));
RaftConfig raftConfig = HoconRaftConfigParser.parseConfig(hoconConfig);
``` 

Other than reading your config from a file, you can create your HOCON `Config`
object in any other way and then parse it via
`HoconRaftConfigParser.parseConfig()`.

[nanoraft-default.conf](https://github.com/NanoRaft/NanoRaft/blob/master/nanoraft-hocon/nanoraft-default.conf)
is the default NanoRaft HOCON configuration file.

Please refer to
[NanoRaft documentation page](https://nanoraft.io/docs/configuration/)
to learn more about configuration. 
