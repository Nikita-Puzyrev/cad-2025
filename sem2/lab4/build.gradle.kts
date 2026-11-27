plugins {
    `java-library`
    id("org.springframework.boot")
}

dependencies {
    api(project(":lab2"))
    implementation(project(":lab3"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
}
