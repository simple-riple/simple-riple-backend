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

// ---------------------------- ( version ) ----------------------------

val swaggerVersion = "3.0.0"
val jacocoVersion = "0.8.8"

// ---------------------------- ( plugin + extension ) ----------------------------

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"

    // 추가
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("jacoco")
    id("org.sonarqube") version "3.5.0.2730"
}

jacoco {
    toolVersion = jacocoVersion
}

sonarqube {
    properties {
        property("sonar.projectKey", "simple-riple_simple-riple-backend")
        property("sonar.organization", "simple-riple-sonarqube")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/test/jacocoTestReport.xml")
    }
}

// ---------------------------- ( dependency ) ----------------------------

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

// ---------------------------- ( task ) ----------------------------

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks{

    // ---------- ( jacoco ) ----------

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = true
                element = "CLASS"

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.0".toBigDecimal()
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

    // ---------- ( adoc ) ----------

    asciidoctor {
        dependsOn(test)
        inputs.dir(file("build/generated-snippets"))
    }

    // ---------- ( base ) ----------

    test {
        useJUnitPlatform()
        outputs.dir(file("build/generated-snippets"))
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