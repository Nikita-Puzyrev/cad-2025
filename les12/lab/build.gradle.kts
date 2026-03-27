plugins {
    id("java")
    id("war")
    kotlin("jvm")
}

group = "ru.bsuedu.cad"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {

    // Spring
    implementation("org.springframework:spring-context:6.1.5")
    implementation("org.springframework:spring-webmvc:6.1.5")
    implementation("org.springframework:spring-orm:6.1.5")
    implementation("org.springframework:spring-tx:6.1.5")
    implementation("org.springframework:spring-jdbc:6.1.6")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.2.224")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    // Jackson (JSON)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    // Thymeleaf
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")

    // CSV
    implementation("org.apache.commons:commons-csv:1.10.0")

    // Servlet API
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // Логирование
    implementation("org.slf4j:slf4j-simple:2.0.12")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}