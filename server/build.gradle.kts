import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.flywaydb.flyway") version "9.19.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "how.realworld"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_18
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-core:9.19.0")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("au.com.dius.pact.provider:junit5spring:4.4.0")
	testImplementation("com.michael-bull.kotlin-result:kotlin-result:1.1.16")
	runtimeOnly("io.github.microutils:kotlin-logging-jvm:2.1.23")
	implementation("io.github.microutils:kotlin-logging:2.0.10")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.mockito", module = "mockito-core")
	}
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:postgresql:1.17.3")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("commons-codec:commons-codec:1.15")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "18"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
