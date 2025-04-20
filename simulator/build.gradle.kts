plugins {
    id("it.unicam.cs.massimopavoni.swarmsimulator.java-application-conventions")
    // JavaFX plugin for graphics support
    id("org.openjfx.javafxplugin") version "0.1.+"
}

dependencies {
    implementation(project(":swarm"))
    // JavaFX additional UI controls
    implementation("org.controlsfx:controlsfx:11.+")

}

javafx {
    version = "21"
    modules("javafx.controls", "javafx.fxml")
}

tasks {
    val projectProps by registering(WriteProperties::class) {
        destinationFile.set(
            file(
                layout.buildDirectory.file("resources/main/it/unicam/cs/massimopavoni/swarmsimulator/simulator/project.properties")
                    .get().asFile.path
            )
        )
        encoding = "UTF-8"
        property("name", rootProject.name)
        property("version", rootProject.version)
        property("github", "https://github.com/massimopavoni/SwarmSimulator")
        property("author", "Massimo Pavoni")
        property("account", "https://github.com/massimopavoni")
        property("license", "https://github.com/massimopavoni/SwarmSimulator/blob/main/LICENSE")
        property("notice", file("${rootDir}/license-notice.txt").readText())
    }
    processResources {
        dependsOn(projectProps)
    }
}

application {
    // Application main module and class
    mainModule.set("it.unicam.cs.massimopavoni.swarmsimulator.simulator")
    mainClass.set("it.unicam.cs.massimopavoni.swarmsimulator.simulator.launcher.Launcher")
}