tasks.register("increaseVersionCode") {
    doLast {
        val versionCode = Integer.parseInt(System.getProperty("VERSION_CODE")) + 1
        val versionName = System.getProperty("VERSION_NAME").split("\\.")
        val last = Integer.parseInt(versionName.last()) + 1
//        versionName[versionName.size - 1] = last.toString()
        val newVersionName = versionName.joinToString(".")

        val gradlePropertiesFile = project.file("gradle.properties.kts")
        val propertiesContent = gradlePropertiesFile.readText()

        val updatedProperties = propertiesContent.replaceFirst(
            "VERSION_CODE=\\d+".toRegex(),
            "VERSION_CODE=$versionCode"
        ).replaceFirst(
            "VERSION_NAME=.*".toRegex(),
            "VERSION_NAME=$newVersionName"
        )

        gradlePropertiesFile.writeText(updatedProperties)
    }
}


tasks.named("build") {
    dependsOn("increaseVersionCode")
}