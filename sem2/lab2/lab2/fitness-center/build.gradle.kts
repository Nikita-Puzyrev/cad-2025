plugins {
	java
	id("org.springframework.boot") version "3.4.12"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.fitness"
version = "0.0.1-SNAPSHOT"
description = "Project for Fitness Center Management"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}
