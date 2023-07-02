plugins {
    // Java support
    java
    // Tests JaCoCo reports support
    jacoco
}

repositories {
    // Maven repository
    mavenCentral()
}

dependencies {
    // Use Guava for common utilities
    implementation("com.google.guava:guava:32.+")
    // Use JUnit Jupiter for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.+")
    // Use Mockito for mocking
    testImplementation("org.mockito:mockito-core:3.+")
    testImplementation("org.mockito:mockito-inline:3.+")
    testImplementation("org.mockito:mockito-junit-jupiter:3.+")
}

tasks.javadoc {
    setDestinationDir(file("$rootDir/docs/javadoc/${project.name}"))
}

tasks.named<Test>("test") {
    dependsOn(tasks.javadoc)
    // Use JUnit Platform for unit tests
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    // Enable xml reports for SonarQube
    reports {
        xml.required.set(true)
    }
}

// Add sonar dependency on test reports
rootProject.tasks.named("sonar").get().dependsOn(tasks.jacocoTestReport)
