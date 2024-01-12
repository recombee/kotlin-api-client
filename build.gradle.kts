import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.codearte.gradle.nexus:gradle-nexus-staging-plugin:0.22.0")
    }
}

plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.22.0"
}

group = "com.recombee"
version = "4.1.0"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.mockito:mockito-core:5.8.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

kotlin {
    explicitApi()
}

// Retrieve credentials and other properties
val ossrhUsername: String by project
val ossrhPassword: String by project

// Nexus Staging Plugin Configuration
configure<io.codearte.gradle.nexus.NexusStagingExtension> {
    packageGroup = "com.recombee"
    username = ossrhUsername
    password = ossrhPassword
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])

            pom {
                name.set("Recombee API Client in Kotlin")
                description.set("A client library for easy use of the Recombee recommendation API in Android applications")
                url.set("https://recombee.com")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("ondra_fiedler")
                        name.set("Ondrej Fiedler")
                        email.set("ondrej.fiedler@recombee.com")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:Recombee/kotlin-api-client.git")
                    developerConnection.set("scm:git:git@github.com:Recombee/kotlin-api-client.git")
                    url.set("https://github.com/Recombee/kotlin-api-client")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            url = uri(releasesRepoUrl)
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}
