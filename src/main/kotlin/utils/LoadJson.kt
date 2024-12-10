package utils

import java.io.InputStream

// Function to load JSON from the resources directory
fun loadJsonFromResources(fileName: String): String {
    // Use the current thread's context class loader
    val stream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
        ?: throw IllegalArgumentException("File not found: $fileName")
    return stream.bufferedReader().use { it.readText() }
}