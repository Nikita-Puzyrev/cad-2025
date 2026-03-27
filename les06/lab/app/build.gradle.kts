plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {

    // Spring
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-core:6.2.2")
    implementation("org.springframework:spring-aop:6.2.2")
    implementation("org.springframework:spring-jdbc:6.2.2")

    // AOP
    implementation("org.aspectj:aspectjweaver:1.9.21")

    // H2 database
    implementation("com.h2database:h2:2.2.224")

    // CSV
    implementation("org.apache.commons:commons-csv:1.9.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.AppWithSpringJava"
}

tasks.test {
    useJUnitPlatform()
}
