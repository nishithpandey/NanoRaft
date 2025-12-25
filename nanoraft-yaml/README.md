# NanoRaft YAML Config Parser

Gradle (version catalog)

```toml
[versions]
nanoraft-yaml = "0.9"

[libraries]
nanoraft-yaml = { module = "io.nanoraft:nanoraft-yaml", version.ref = "nanoraft-yaml" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.nanoraft:nanoraft-yaml:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.nanoraft</groupId>
    <artifactId>nanoraft-yaml</artifactId>
    <version>0.9</version>
</dependency>
```

This project enables you to create `RaftConfig` objects from YAML files
easily, as shown below:

```
String configFilePath = "...";
RaftConfig raftConfig = YamlRaftConfigParser.parseFile(new Yaml(), configFilePath);
``` 

Other than reading your config from a file, `YamlRaftConfigParser` also offers
a few other parsing methods.

[nanoraft-default.yaml](https://github.com/NanoRaft/NanoRaft/blob/master/nanoraft-yaml/nanoraft-default.yaml)
is the default NanoRaft YAML configuration file.

Please refer to
[NanoRaft documentation page](https://nanoraft.io/docs/configuration/)
to learn more about configuration. 
