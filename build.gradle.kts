import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*

plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

group = "com.recombee"
version = "6.0.0"

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

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

tasks.register("releaseToCentral") {
    dependsOn("publishToSonatype", "closeAndReleaseSonatypeStagingRepository")
}

/**
 * Publications
 */
publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])

            artifact(sourcesJar)
            artifact(javadocJar)

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
}

/**
 * Signing
 * Provide standard Gradle signing props:
 * - signing.keyId
 * - signing.password
 * - signing.secretKeyRingFile (or use in-memory key via signingKey / signingPassword)
 */
signing {
    if (findProperty("signing.keyId") != null &&
        findProperty("signing.secretKeyRingFile") != null &&
        findProperty("signing.password") != null) {
        sign(publishing.publications["mavenKotlin"])
    } else {
        println("Signing properties not found, skipping signing configuration.")
    }
}

/**
 * Nexus Publish (Sonatype Central)
 * Use sonatypeUsername / sonatypePassword (user-token pair recommended).
 * For Sonatype Central (OSSRH EOL), use the new staging API & snapshots URLs.
 */
nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))

            username.set(findProperty("sonatypeUsername") as String?)
            password.set(findProperty("sonatypePassword") as String?)
        }
    }
}
