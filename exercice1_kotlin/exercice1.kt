fun processList(list: List<Int>, predicate: (Int) -> Boolean): List<Int> {
    val result = mutableListOf<Int>()
    for (item in list) {
        if (predicate(item)) {
            result.add(item)
        }
    }
    return result
}

// Example usage:
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    val evenNumbers = processList(numbers) { it % 2 == 0 }
    println(evenNumbers) // Output: [2, 4, 6]
}