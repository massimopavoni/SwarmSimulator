plugins {
    // SonarQube plugin for code quality analysis
    id("org.sonarqube") version "4.+"
}

repositories {
    // Maven repository
    mavenCentral()
}

rootProject.version = "1.1.0"

sonar {
    properties {
        property("sonar.projectName", rootProject.name)
        property("sonar.host.url", project.property("sonar.host.url").toString())
        property("sonar.projectKey", project.property("sonar.projectKey").toString())
        property("sonar.token", project.property("sonar.token").toString())
        property("sonar.coverage.exclusions", project.property("sonar.coverage.exclusions").toString())
    }
}
