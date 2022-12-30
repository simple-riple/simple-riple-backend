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
val querydslVersion = "5.0.0"

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
    kotlin("kapt") version "1.7.10"
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

    // spring core
    implementation("org.springframework.boot:spring-boot-starter-web")

    // swagger
    implementation("io.springfox:springfox-boot-starter:${swaggerVersion}")

    // db
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // querydsl
    implementation("com.querydsl:querydsl-jpa:${querydslVersion}")
    kapt("com.querydsl:querydsl-apt:${querydslVersion}:jpa")
    kaptTest("com.querydsl:querydsl-apt:${querydslVersion}:jpa")

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

val snippetsDir = file("build/generated-snippets")

tasks {

    // ---------- ( jacoco ) ----------

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = false
                element = "CLASS"

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.80".toBigDecimal()
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
        inputs.dir(snippetsDir)
    }

    // ---------- ( base ) ----------

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