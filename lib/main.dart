import 'dart:math';
import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      themeMode: ThemeMode.system,
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: const Color.fromARGB(255, 0, 0, 0),
        scaffoldBackgroundColor: const Color(0xFFF4F6FB),
      ),
      darkTheme: ThemeData.dark(useMaterial3: true),
      home: const CalculateurPage(),
    );
  }
}

class CalculateurPage extends StatefulWidget {
  const CalculateurPage({super.key});

  @override
  State<CalculateurPage> createState() => _CalculateurPageState();
}

class _CalculateurPageState extends State<CalculateurPage>
    with SingleTickerProviderStateMixin {
  final nomController = TextEditingController();
  final noteController = TextEditingController();

  List<double> notes = [];
  double? moyenne;
  double? minNote;
  double? maxNote;
  double? mediane;

  String grade = "";
  Color gradeColor = Colors.grey;

  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 600));
    _animation =
        CurvedAnimation(parent: _controller, curve: Curves.easeOutBack);
  }

  void ajouterNote() {
    final note = double.tryParse(noteController.text);
    if (note == null || note < 0 || note > 20) return;

    setState(() {
      notes.add(note);
      noteController.clear();
    });
  }

  void calculer() {
    if (nomController.text.isEmpty || notes.isEmpty) return;

    final somme = notes.reduce((a, b) => a + b);
    moyenne = somme / notes.length;

    minNote = notes.reduce(min);
    maxNote = notes.reduce(max);

    final sorted = [...notes]..sort();
    mediane = sorted.length % 2 == 0
        ? (sorted[sorted.length ~/ 2 - 1] + sorted[sorted.length ~/ 2]) / 2
        : sorted[sorted.length ~/ 2];

    if (moyenne! >= 16) {
      grade = "A";
      gradeColor = Colors.green;
    } else if (moyenne! >= 14) {
      grade = "B";
      gradeColor = Colors.blue;
    } else if (moyenne! >= 12) {
      grade = "C";
      gradeColor = Colors.orange;
    } else if (moyenne! >= 10) {
      grade = "D";
      gradeColor = Colors.deepOrange;
    } else {
      grade = "F";
      gradeColor = Colors.red;
    }

    _controller.forward(from: 0);
    setState(() {});
  }

  Widget buildNotesList() {
    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children: notes.map((note) {
        return Chip(
          label: Text(note.toString()),
          deleteIcon: const Icon(Icons.close),
          onDeleted: () {
            setState(() {
              notes.remove(note);
            });
          },
        );
      }).toList(),
    );
  }

  Widget statCard(String title, String value) {
    return Expanded(
      child: Container(
        margin: const EdgeInsets.all(6),
        padding: const EdgeInsets.all(14),
        decoration: BoxDecoration(
          color: Theme.of(context).cardColor,
          borderRadius: BorderRadius.circular(16),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.05),
              blurRadius: 10,
            )
          ],
        ),
        child: Column(
          children: [
            Text(title,
                style: const TextStyle(fontSize: 13, color: Colors.grey)),
            const SizedBox(height: 6),
            Text(value,
                style:
                    const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
          ],
        ),
      ),
    );
  }

  Widget buildChart() {
    return SizedBox(
      height: 180,
      child: BarChart(
        BarChartData(
          borderData: FlBorderData(show: false),
          titlesData: FlTitlesData(show: false),
          barGroups: notes.asMap().entries.map((entry) {
            return BarChartGroupData(
              x: entry.key,
              barRods: [
                BarChartRodData(
                  toY: entry.value,
                  width: 14,
                  borderRadius: BorderRadius.circular(6),
                )
              ],
            );
          }).toList(),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          // 🔥 HEADER PREMIUM
          Container(
            padding: const EdgeInsets.only(top: 60, bottom: 30),
            width: double.infinity,
            decoration: const BoxDecoration(
              gradient: LinearGradient(
                colors: [
                  Color.fromARGB(255, 83, 1, 83),
                  Color.fromARGB(255, 132, 21, 160)
                ],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
              borderRadius: BorderRadius.vertical(bottom: Radius.circular(30)),
            ),
            child: const Center(
              child: Text(
                "Calculateur de note",
                style: TextStyle(
                  fontSize: 26,
                  fontWeight: FontWeight.bold,
                  color: Colors.white,
                  letterSpacing: 1.2,
                ),
              ),
            ),
          ),

          Expanded(
            child: AnimatedAlign(
              duration: const Duration(milliseconds: 600),
              curve: Curves.easeInOut,
              alignment:
                  moyenne == null ? Alignment.center : Alignment.topCenter,
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(20),
                child: ConstrainedBox(
                  constraints: const BoxConstraints(maxWidth: 500),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      // ✏️ NOM
                      TextField(
                        controller: nomController,
                        decoration: InputDecoration(
                          labelText: "Nom de l'étudiant",
                          filled: true,
                          border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(16),
                              borderSide: BorderSide.none),
                          prefixIcon: const Icon(Icons.person),
                        ),
                      ),
                      const SizedBox(height: 15),

                      // ✏️ NOTE + BOUTON
                      Row(
                        children: [
                          Expanded(
                            child: TextField(
                              controller: noteController,
                              keyboardType: TextInputType.number,
                              decoration: InputDecoration(
                                labelText: "Ajouter une note",
                                filled: true,
                                border: OutlineInputBorder(
                                    borderRadius: BorderRadius.circular(16),
                                    borderSide: BorderSide.none),
                                prefixIcon: const Icon(Icons.grade),
                              ),
                            ),
                          ),
                          const SizedBox(width: 10),
                          ElevatedButton(
                            onPressed: ajouterNote,
                            style: ElevatedButton.styleFrom(
                              padding: const EdgeInsets.symmetric(
                                  horizontal: 20, vertical: 18),
                              shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(16)),
                            ),
                            child: const Text("Ajouter"),
                          )
                        ],
                      ),

                      const SizedBox(height: 15),

                      if (notes.isNotEmpty) buildNotesList(),

                      const SizedBox(height: 20),

                      ElevatedButton.icon(
                        onPressed: calculer,
                        icon: const Icon(Icons.calculate),
                        label: const Text("Calculer les statistiques"),
                        style: ElevatedButton.styleFrom(
                          padding: const EdgeInsets.symmetric(vertical: 16),
                          shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(16)),
                        ),
                      ),

                      const SizedBox(height: 30),

                      if (moyenne != null)
                        ScaleTransition(
                          scale: _animation,
                          child: Column(
                            children: [
                              Text(
                                "Moyenne : ${moyenne!.toStringAsFixed(2)} / 20",
                                style:
                                    Theme.of(context).textTheme.headlineSmall,
                              ),
                              const SizedBox(height: 10),
                              Container(
                                padding: const EdgeInsets.symmetric(
                                    horizontal: 30, vertical: 12),
                                decoration: BoxDecoration(
                                  color: gradeColor,
                                  borderRadius: BorderRadius.circular(30),
                                ),
                                child: Text(
                                  grade,
                                  style: const TextStyle(
                                      fontSize: 24,
                                      fontWeight: FontWeight.bold,
                                      color: Colors.white),
                                ),
                              ),
                              const SizedBox(height: 25),
                              Row(
                                children: [
                                  statCard("Min", minNote!.toString()),
                                  statCard("Max", maxNote!.toString()),
                                ],
                              ),
                              Row(
                                children: [
                                  statCard(
                                      "Médiane", mediane!.toStringAsFixed(2)),
                                  statCard("Notes", notes.length.toString()),
                                ],
                              ),
                              const SizedBox(height: 25),
                              buildChart(),
                            ],
                          ),
                        ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
