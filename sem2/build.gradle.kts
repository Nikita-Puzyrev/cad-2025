plugins {
    id("org.springframework.boot") version "3.4.5" apply false
}

subprojects {
    repositories {
        mavenCentral()
    }

    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of(17)
            }
        }

        dependencies {
            "implementation"(platform("org.springframework.boot:spring-boot-dependencies:3.4.5"))
        }
    }
}
