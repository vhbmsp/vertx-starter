import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*;


buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.13")
    }
}


plugins {
    kotlin ("jvm") version "1.8.10"
    id("com.google.protobuf") version "0.9.3"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("plugin.serialization") version "1.8.21"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val vertxVersion = "4.4.1"
val junitJupiterVersion = "5.9.1"
val grpcVersion = "1.54.1"
val grpcKotlinVersion = "1.3.0"
val googleProtobufVersion = "3.10.1"

val mainVerticleName = "com.example.starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
    mainClass.set(launcherClassName)
}

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-core")
    implementation("io.vertx:vertx-config")
    implementation("io.vertx:vertx-grpc:$vertxVersion")
    implementation("io.vertx:vertx-grpc-protoc-plugin:$vertxVersion")
    implementation("io.vertx:vertx-grpc-server:$vertxVersion")
    implementation("io.vertx:vertx-redis-client:$vertxVersion")

    implementation("io.vertx:vertx-lang-kotlin")
    implementation("io.vertx:vertx-lang-kotlin-coroutines")


    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")


    // implementation("org.jetbrains.kotlin:kotlin-reflect")
    // implementation("io.grpc:grpc-protobuf:1.33.1")
    // implementation("io.grpc:grpc-stub:1.33.1")
    // implementation("io.grpc:grpc-netty:1.33.1")
    // compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    // api("com.google.protobuf:protobuf-java-util:3.13.0")
    // implementation("io.grpc:grpc-all:1.33.1")
    // --> api("io.grpc:grpc-kotlin-stub:0.2.1")

    // implementation("io.grpc:protoc-gen-grpc-java:1.54.1")
    // implementation("io.grpc:protoc-gen-grpc-kotlin:0.1.5")


    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    // implementation("com.google.protobuf:protobuf-gradle-plugin:0.8.13")
    // implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(kotlin("stdlib-jdk8"))
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$googleProtobufVersion"
    }

    plugins {
        id("grpc"){
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:0.1.5"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}



val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "17"

tasks.withType<JavaCompile> {
    targetCompatibility = "17"
}


tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

