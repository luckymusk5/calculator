package com.example.calculator

import java.io.File

// --- MODÈLES DE DONNÉES ---
data class Etudiant(
    val nom: String,
    val note: Double,
    val grade: String
) {
    // Fonction de formatage
    fun formatAffichage(): String =
        "%-35s | %6.2f | %-2s".format(nom, note, grade)
}

// --- FONCTIONS UTILITAIRES ---
fun obtenirGrade(note: Double): String {
    return when {
        note >= 16 -> "A"
        note >= 14 -> "B"
        note >= 12 -> "C"
        note >= 10 -> "D"
        else -> "F"
    }
}

// --- CHARGEMENT CSV ---
fun chargerDonneesCSV(cheminFichier: String): List<Etudiant> {
    return try {
        val file = File(cheminFichier)
        if (!file.exists()) {
            println("❌ Erreur : Le fichier '$cheminFichier' n'existe pas")
            emptyList()
        } else {
            file.readLines()
                .drop(1) // on ignore l’en-tête
                .mapNotNull { ligne ->
                    val parts = ligne.split(",")
                    if (parts.size >= 2) {
                        val nom = parts[0].trim()
                        val note = parts[1].trim().toDoubleOrNull()
                        if (note != null && note in 0.0..20.0) {
                            Etudiant(nom, note, obtenirGrade(note))
                        } else null
                    } else null
                }
        }
    } catch (e: Exception) {
        println("❌ Erreur lors de la lecture du fichier : ${e.message}")
        emptyList()
    }
}

// --- ÉCRITURE CSV AVEC STATISTIQUES ---
fun ecrireCSV(etudiants: List<Etudiant>, cheminFichier: String) {
    try {
        val file = File(cheminFichier)
        file.printWriter().use { out ->
            // En-tête
            out.println("Nom,Note,Grade")
            // Données
            etudiants.forEach { out.println("${it.nom},${it.note},${it.grade}") }

            // Statistiques globales
            val notes = etudiants.map { it.note }
            val moyenne = notes.average()
            val min = notes.minOrNull() ?: 0.0
            val max = notes.maxOrNull() ?: 0.0
            val sorted = notes.sorted()
            val mediane = if (sorted.size % 2 == 0) {
                (sorted[sorted.size / 2 - 1] + sorted[sorted.size / 2]) / 2.0
            } else {
                sorted[sorted.size / 2]
            }
            val gradeGlobal = obtenirGrade(moyenne)

            out.println()
            out.println("STATISTIQUES GLOBALES")
            out.println("Nombre d'étudiants,${etudiants.size}")
            out.println("Moyenne,${"%.2f".format(moyenne)}")
            out.println("Note minimale,${"%.2f".format(min)}")
            out.println("Note maximale,${"%.2f".format(max)}")
            out.println("Médiane,${"%.2f".format(mediane)}")
            out.println("Grade global,$gradeGlobal")
        }
        println("✅ Fichier CSV créé : $cheminFichier")
    } catch (e: Exception) {
        println("❌ Erreur lors de l’écriture du fichier : ${e.message}")
    }
}

// --- AFFICHAGE TABLEAU ---
fun afficherTableauEtudiants(etudiants: List<Etudiant>) {
    if (etudiants.isEmpty()) {
        println("❌ Aucun étudiant à afficher")
        return
    }

    println("\n" + "═".repeat(70))
    println("📊 TABLEAU DES ÉTUDIANTS ET LEURS GRADES")
    println("═".repeat(70))
    println("%-35s | %-10s | %-10s".format("Nom", "Note", "Grade"))
    println("-".repeat(70))

    etudiants.forEach { println(it.formatAffichage()) }

    println("═".repeat(70))
}

// --- MAIN ---
fun main(args: Array<String>) {
    println("\n🎓 === CALCULATEUR DE NOTES AVEC GRADES ===\n")

    val cheminFichier = if (args.isNotEmpty()) {
        args[0]
    } else {
        print("📁 Entrez le chemin du fichier CSV : ")
        readLine() ?: ""
    }

    if (cheminFichier.isBlank()) {
        println("❌ Aucun chemin fourni")
        return
    }

    println("⏳ Chargement des données...")
    val etudiants = chargerDonneesCSV(cheminFichier)

    if (etudiants.isNotEmpty()) {
        println("✅ ${etudiants.size} étudiant(s) chargé(s)\n")
        afficherTableauEtudiants(etudiants)

        // Exemple d’utilisation d’un higher-order function (filter)
        val reussites = etudiants.filter { it.note >= 10 }
        println("\n✅ Étudiants ayant réussi :")
        reussites.forEach { println(it.nom) }

        // Exemple d’un lambda passé à une fonction personnalisée
        fun appliquerSurEtudiants(liste: List<Etudiant>, action: (Etudiant) -> Unit) {
            liste.forEach(action)
        }
        println("\n🔎 Application d’un lambda personnalisé :")
        appliquerSurEtudiants(etudiants) { e ->
            println("Étudiant ${e.nom} → Grade ${e.grade}")
        }

        // Création du fichier de sortie avec grades + stats
        val fichierSortie = "notes_avec_grades.csv"
        ecrireCSV(etudiants, fichierSortie)
    } else {
        println("❌ Aucun étudiant n'a pu être chargé")
    }
}