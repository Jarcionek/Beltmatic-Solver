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
    testImplementation("org.openjdk.nashorn:nashorn-core:15.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
