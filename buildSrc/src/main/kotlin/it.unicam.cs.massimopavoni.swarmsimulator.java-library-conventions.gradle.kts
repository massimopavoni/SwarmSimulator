plugins {
    // Common convention plugin for shared build configuration between library and application projects
    id("it.unicam.cs.massimopavoni.swarmsimulator.java-common-conventions")

    // Java-library support plugin
    `java-library`
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.+")
}
