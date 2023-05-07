import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.protobuf.gradle.GenerateProtoTask
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.compiler.plugin.parsePluginOption
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



plugins {
    kotlin ("jvm") version "1.7.21"
    id("com.google.protobuf") version "0.9.3"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

val vertxVersion = "4.4.1"
val junitJupiterVersion = "5.9.1"
val grpcVersion = "1.47.0"
val grpcKotlinVersion = "1.3.0"

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

    implementation("io.vertx:vertx-lang-kotlin")
    implementation("io.vertx:vertx-lang-kotlin-coroutines")
    implementation("com.google.protobuf:protobuf-kotlin:3.21.12")


    implementation(kotlin("stdlib-jdk8"))
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")


}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.8.0"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                java { }
                kotlin { }

            }
            task.plugins {
                kotlin { }
            }
        }
    }
}


sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
        java {
            srcDirs("generated-sources/main/java", "generated-sources/main/grpc")
        }
    }
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "17"

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
