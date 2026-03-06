data class Person(val name: String, val age: Int)

fun main() {
    val people = listOf(
        Person("Alice", 25),
        Person("Bob", 30),
        Person("Charlie", 35),
        Person("Anna", 22),
        Person("Ben", 28)
    )

    // 1. Filtrer les personnes dont le nom commence par A ou B
    val filtered = people.filter { it.name.startsWith("A") || it.name.startsWith("B") }

    // 2. Extraire les âges
    val ages = filtered.map { it.age }

    // 3. Calculer la moyenne
    val average = ages.average()

    // 4. Arrondir à 1 chiffre après la virgule et afficher
    println("%.1f".format(average)) // Résultat : 27.0
}