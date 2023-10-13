import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "how.realworld"
version = "0.0.1-SNAPSHOT"

val jsonwebtokenVersion = "0.11.2"

java {
	sourceCompatibility = JavaVersion.VERSION_18
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("io.jsonwebtoken:jjwt-api:${jsonwebtokenVersion}")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:${jsonwebtokenVersion}")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jsonwebtokenVersion}")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("au.com.dius.pact.provider:junit5spring:4.4.0")
	testImplementation("com.michael-bull.kotlin-result:kotlin-result:1.1.16")
	runtimeOnly("io.github.microutils:kotlin-logging-jvm:2.1.23")
	implementation("io.github.microutils:kotlin-logging:2.0.10")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
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
