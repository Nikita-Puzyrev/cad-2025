import org.gradle.api.tasks.bundling.War

plugins {
    application
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-orm:6.2.2")
    implementation("org.springframework:spring-tx:6.2.2")
    implementation("org.springframework:spring-web:6.2.2")

    implementation("org.springframework.data:spring-data-jpa:3.4.2")

    implementation("org.hibernate.orm:hibernate-core:6.6.5.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.3.232")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.app.App"

    applicationDefaultJvmArgs = listOf(
        "-Dfile.encoding=UTF-8",
        "-Dsun.stdout.encoding=UTF-8",
        "-Dsun.stderr.encoding=UTF-8"
    )
}

tasks.named<War>("war") {
    archiveFileName.set("petshop.war")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}