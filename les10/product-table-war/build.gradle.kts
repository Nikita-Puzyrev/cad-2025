plugins {
    id("java")
    id("war")
}

group = "ru.bsuedu.cad"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    // Spring
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-tx:6.2.2")

    // Hibernate + JPA
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Hikari + H2
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.2.224")

    // CSV
    implementation("org.apache.commons:commons-csv:1.10.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.9")

    // Servlet API (для Tomcat)
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("org.springframework:spring-orm:6.2.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}