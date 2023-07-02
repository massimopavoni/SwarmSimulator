plugins {
    id("it.unicam.cs.massimopavoni.swarmsimulator.java-application-conventions")
}

dependencies {
    implementation(project(":swarm"))
}

application {
    // Application main class
    mainClass.set("it.unicam.cs.massimopavoni.swarmsimulator.simulator.App")
}