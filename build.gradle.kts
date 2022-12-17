import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"

    // 추가
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("jacoco")
}

val swaggerVersion = "3.0.0"
val jacocoVersion = "0.8.8"
val snippetsDir = file("build/generated-snippets")

dependencies {
    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // spring core
    implementation("org.springframework.boot:spring-boot-starter-web")

    // swagger
    implementation("io.springfox:springfox-boot-starter:${swaggerVersion}")

    // database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks{

    // ---------------- ( jacoco ) ----------------

    jacoco {
        toolVersion = jacocoVersion
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = true
                element = "CLASS"

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.70".toBigDecimal()
                }

                excludes = listOf()
            }
        }
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }
        finalizedBy(jacocoTestCoverageVerification)
    }

    // ---------------- ( snippet 생성 ) ----------------

    asciidoctor {
        dependsOn(test)
        inputs.dir(snippetsDir)
    }

    // ---------------- ( base ) ----------------

    test {
        useJUnitPlatform()
        outputs.dir(snippetsDir)
    }

    jar {
        // 빌드시 *-plain.jar 생성 X
        enabled = false
    }

    bootJar {
        dependsOn(asciidoctor)
        from("${asciidoctor.get().outputDir}") {
            into("BOOT-INF/classes/static/rest-docs")
        }
    }

    build {
        dependsOn(jacocoTestReport)
    }
}