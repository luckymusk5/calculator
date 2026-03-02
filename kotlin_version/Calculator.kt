package com.example.flutter_application_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

// --- ACTIVITÉ PRINCIPALE ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculatorApp()
            }
        }
    }
}

// --- LOGIQUE DE CALCUL ---
data class Stats(
    val moyenne: Double,
    val min: Double,
    val max: Double,
    val mediane: Double,
    val grade: String,
    val color: Color
)

fun calculerStats(notes: List<Double>): Stats {
    val moyenne = notes.average()
    val min = notes.minOrNull() ?: 0.0
    val max = notes.maxOrNull() ?: 0.0
    val sorted = notes.sorted()
    val mediane = if (sorted.size % 2 == 0) {
        (sorted[sorted.size / 2 - 1] + sorted[sorted.size / 2]) / 2.0
    } else {
        sorted[sorted.size / 2]
    }

    val (grade, color) = when {
        moyenne >= 16 -> "A" to Color.Green
        moyenne >= 14 -> "B" to Color.Blue
        moyenne >= 12 -> "C" to Color(0xFFFFA500)
        moyenne >= 10 -> "D" to Color(0xFFFF8C00)
        else -> "F" to Color.Red
    }

    return Stats(moyenne, min, max, mediane, grade, color)
}

// --- INTERFACE UI ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CalculatorApp() {
    var nom by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }
    val notes = remember { mutableStateListOf<Double>() }
    var stats by remember { mutableStateOf<Stats?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F6FB))) {
        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(Color(0xFF530153), Color(0xFF8415A0))),
                    shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(top = 50.dp, bottom = 25.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Calculateur de note", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Nom de l'étudiant") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Person, null) }
            )

            Spacer(Modifier.height(15.dp))

            Row {
                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text("Ajouter une note") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Grade, null) }
                )
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        noteInput.toDoubleOrNull()?.let { if (it in 0.0..20.0) { notes.add(it); noteInput = "" } }
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) { Text("Ajouter") }
            }

            Spacer(Modifier.height(15.dp))

            // LISTE DES NOTES
            androidx.compose.layout.FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                notes.forEach { note ->
                    InputChip(
                        selected = false,
                        onClick = {},
                        label = { Text(note.toString()) },
                        trailingIcon = {
                            IconButton(onClick = { notes.remove(note) }) {
                                Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = { if (nom.isNotBlank() && notes.isNotEmpty()) stats = calculerStats(notes) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Calculate, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Calculer")
                }
                Spacer(Modifier.width(10.dp))
                OutlinedButton(
                    onClick = {
                        nom = "Alice (Démo)"
                        notes.clear()
                        notes.addAll(listOf(18.5, 16.0, 19.0))
                        stats = calculerStats(notes)
                    },
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) { Text("Démo") }
            }

            Spacer(Modifier.height(30.dp))

            AnimatedVisibility(visible = stats != null, enter = scaleIn()) {
                stats?.let { s ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Moyenne : ${String.format(Locale.US, "%.2f", s.moyenne)} / 20", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        Surface(color = s.color, shape = RoundedCornerShape(30.dp)) {
                            Text(s.grade, modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(25.dp))
                        Row {
                            StatCard("Min", s.min.toString(), Modifier.weight(1f))
                            StatCard("Max", s.max.toString(), Modifier.weight(1f))
                        }
                        Row {
                            StatCard("Médiane", String.format(Locale.US, "%.2f", s.mediane), Modifier.weight(1f))
                            StatCard("Notes", notes.size.toString(), Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(25.dp))
                        BarChartCustom(notes)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier) {
    Card(modifier = modifier.padding(6.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BarChartCustom(notes: List<Double>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(150.dp).padding(10.dp)) {
        val width = size.width
        val height = size.height
        val barWidth = if (notes.isNotEmpty()) (width / notes.size) * 0.7f else 0f
        val spacing = if (notes.isNotEmpty()) (width / notes.size) * 0.3f else 0f

        notes.forEachIndexed { index, note ->
            val barHeight = (note.toFloat() / 20f) * height
            drawRoundRect(
                color = Color(0xFF8415A0),
                topLeft = Offset(x = index * (barWidth + spacing), y = height - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
        }
    }
}
