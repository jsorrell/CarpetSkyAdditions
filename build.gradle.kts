class Versions(properties: ExtraPropertiesExtension) {
  val mod = properties["mod_version"] as String
  val java = JavaVersion.toVersion(properties["java_version"] as String)
  val minecraft = properties["minecraft_version"] as String
  val minecraftCompatibility = properties["compatible_minecraft_versions"] as String
  val project = "$minecraft-$mod"
  val yarnMappings = properties["yarn_mappings"] as String
  val fabricLoader = properties["loader_version"] as String
  val fabricApi = properties["fabric_version"] as String
  val carpet = properties["carpet_core_version"] as String
  val clothConfig = properties["cloth_config_version"] as String
  val modmenu = properties["modmenu_version"] as String
}

val versions = Versions(project.extra)
val modId = project.extra["mod_id"] as String

plugins {
  id("fabric-loom").version("1.1-SNAPSHOT")
}

base {
  archivesName.set(modId)
}
version = versions.project

val fillBuildTemplate = tasks.register<Copy>("fillBuildTemplate") {
  val templateContext = mapOf(
    "id" to project.extra["mod_id"] as String,
    "name" to project.extra["mod_name"] as String,
    "version" to version,
    "minecraft_version" to versions.minecraft,
    "yarn_mappings" to versions.yarnMappings,
  )
  inputs.properties(templateContext)
  from("src/template/java")
  into(layout.buildDirectory.dir("generated/java"))
  expand(templateContext)
}
sourceSets.main.get().java.srcDir(layout.buildDirectory.dir("generated/java"))

tasks {
  withType<JavaCompile> {
    dependsOn(fillBuildTemplate)
    options.encoding = "UTF-8"
    targetCompatibility = versions.java.toString()
    sourceCompatibility = targetCompatibility
    options.release.set(versions.java.ordinal + 1)
  }

  processResources {
    val templateContext = mapOf(
      "name" to project.extra["mod_name"],
      "version" to version,
      "mc_compatibility" to versions.minecraftCompatibility,
    )

    inputs.properties(templateContext)
    filesMatching("fabric.mod.json") {
      expand(templateContext)
    }
  }

  java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(versions.java.ordinal + 1)) }
    sourceCompatibility = versions.java
    targetCompatibility = versions.java
  }
  jar {
    // Embed license in output jar
    from("LICENSE")
  }
}

loom {
  accessWidenerPath.set(file("src/main/resources/$modId.accesswidener"))
}

repositories {
  maven("https://masa.dy.fi/maven") {
    content {
      includeGroup("carpet")
    }
  }
  maven("https://maven.shedaniel.me") {
    content {
      includeGroup("me.shedaniel.cloth")
    }
  }
  maven("https://maven.terraformersmc.com/releases/") {
    content {
      includeGroup("com.terraformersmc")
    }
  }
}

dependencies {
  minecraft("com.mojang", "minecraft", versions.minecraft)
  mappings("net.fabricmc", "yarn", versions.yarnMappings, null, "v2")
  modImplementation("net.fabricmc", "fabric-loader", versions.fabricLoader)
  modImplementation("carpet", "fabric-carpet", versions.carpet)

  arrayOf(
    "fabric-object-builder-api-v1",
    "fabric-registry-sync-v0",
    "fabric-resource-loader-v0",
    "fabric-transitive-access-wideners-v1",
  ).forEach { modImplementation(fabricApi.module(it, versions.fabricApi)) }

  modImplementation("me.shedaniel.cloth", "cloth-config-fabric", versions.clothConfig) {
    exclude("net.fabricmc.fabric-api")
  }
  modImplementation("com.terraformersmc", "modmenu", versions.modmenu)
}

val copyTranslations = tasks.register<Copy>("copyTranslations") {
  from("translations-pack")
  into(layout.buildDirectory.dir("translations-pack"))

  into("assets/$modId/lang") {
    from("src/main/resources/assets/$modId/lang") {
      exclude("en_us.json")
    }
  }
}

tasks.register<Zip>("zipTranslationPack") {
  dependsOn(copyTranslations)
  archiveClassifier.set("translations")
  archiveVersion.set(versions.mod)
  group = "build"
  from(copyTranslations.get().outputs)
  destinationDirectory.set(base.distsDirectory)
}
