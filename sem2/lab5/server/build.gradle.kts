plugins {
    java
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":lab4"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
