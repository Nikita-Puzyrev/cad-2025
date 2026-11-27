plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":lab2"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
