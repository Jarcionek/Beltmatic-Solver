plugins {
    kotlin("jvm") version "2.1.10"
}

group = "uk.co.jpawlak.beltmatic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.openjdk.nashorn:nashorn-core:15.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
