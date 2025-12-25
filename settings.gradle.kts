rootProject.name = "NanoRaft"

include("nanoraft")
include("nanoraft-hocon")
include("nanoraft-metrics")
include("nanoraft-store-sqlite")
include("nanoraft-tutorial")
include("nanoraft-yaml")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage") //
    repositories {
        mavenCentral()
    }
}
