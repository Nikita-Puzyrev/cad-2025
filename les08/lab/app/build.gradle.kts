plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {

    // Spring Core + Context
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-core:6.2.2")
    implementation("org.springframework:spring-beans:6.2.2")
    implementation("org.springframework:spring-tx:6.2.2")
    implementation("org.springframework:spring-orm:6.2.2")

    // Hibernate ORM (JPA)
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    // JPA API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // HikariCP (DataSource)
    implementation("com.zaxxer:HikariCP:5.1.0")

    // H2 database
    implementation("com.h2database:h2:2.2.224")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.9")

    // Annotations
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    implementation("org.apache.commons:commons-csv:1.10.0")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.app.App"
}

tasks.test {
    useJUnitPlatform()
}