fun main() {
    val words = listOf("apple", "bat", "banana", "cat", "dragonfruit")
    
    // Create a map where keys are the strings and values are their lengths
    val wordLengths = words.associateWith { it.length }
    
    // Filter entries where length > 4
    val filtered = wordLengths.filter { it.value > 4 }
    
    
    println(filtered) // Output: {apple=5, banana=6, dragonfruit=11}
}