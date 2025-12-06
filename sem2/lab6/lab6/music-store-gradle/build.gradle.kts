plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.musicstore"
version = "0.0.1-SNAPSHOT"
description = "Music Equipment Store Application (Gradle)"

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
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")  // ← JPA
    
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")  // ← SQLite драйвер
     implementation("org.hibernate.orm:hibernate-community-dialects:6.5.2.Final")  // ← SQLite диалект для Hibernate

    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("music-store.jar")
}