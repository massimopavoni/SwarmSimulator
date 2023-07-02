plugins {
    java
    jacoco
    // SonarQube plugin for code quality analysis
    id("org.sonarqube") version "4.2.1.3168"
}

repositories {
    // Maven repository
    mavenCentral()
}

sonar {
    properties {
        property("sonar.projectName", rootProject.name)
        property("sonar.host.url", project.property("sonar.host.url").toString())
        property("sonar.projectKey", project.property("sonar.projectKey").toString())
        property("sonar.token", project.property("sonar.token").toString())
    }
}