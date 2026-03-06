# Kotlin Exercises Project

## Description

This repository contains several Kotlin exercises focused on practicing functional programming concepts, collections manipulation, and basic data processing.
The goal of these exercises is to understand how to use Kotlin features such as **lambdas, lists, maps, filtering, and data classes** to solve common programming problems.

## Technologies

* Kotlin
* Git
* GitHub

## Exercises

### Exercise 1 – List Processing with Lambda

This exercise implements a function `processList` that takes a list of integers and a predicate (lambda function).
The function iterates through the list and returns a new list containing only the elements that satisfy the given condition.

Example: filtering even numbers from a list.

Concepts used:

* Lambda functions
* Lists
* Conditional filtering
* Iteration

---

### Exercise 2 – Map Creation and Filtering

In this exercise, a list of strings is transformed into a **map** where:

* the **key** is the string
* the **value** is the length of the string

After creating the map, the program prints only the entries whose string length is greater than **4**.

Concepts used:

* Maps
* String manipulation
* Filtering map entries
* Collection processing

---

### Exercise 3 – Complex Data Processing

This exercise works with a `Person` data class containing a **name** and an **age**.

The algorithm performs the following steps:

1. Filters people whose name starts with **A** or **B**.
2. Extracts their ages.
3. Calculates the **average age**.
4. Prints the result rounded to **one decimal place**.

Concepts used:

* Data classes
* List filtering
* Mapping values
* Average calculation
* Formatting output

---

## Project Structure

```
calculator/
│
├── exercice1_kotlin
├── exercice2_kotlin
├── exercice3_kotlin
└── README.md
```

## How to Run

1. Clone the repository:

```
git clone https://github.com/luckymusk5/calculator.git
```

2. Navigate to the project folder:

```
cd calculator

```

3. Compile and run the Kotlin file:

```
kotlinc exercice1.kt -include-runtime -d exercice1.jar
java -jar exercice1.jar
```

## Authors

Project developed collaboratively as part of Kotlin programming exercises.
