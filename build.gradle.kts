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
    testImplementation("com.fathzer:javaluator:3.0.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks.test {
//    jvmArgs("-da")
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
