package Day13

import readInput

data class Equation(
    val a1: Long, val b1: Long, val c1: Long,
    val a2: Long, val b2: Long, val c2: Long
)

fun parseEquation(text: String): Equation {
    fun parseRegexToTuple(regex: Regex, text: String): Pair<Long, Long> =
        regex.find(text)
            ?.groupValues
            ?.let { it[1].toLong() to it[2].toLong() }
            ?: (0L to 0L)

    val (a1, a2) = parseRegexToTuple(Regex("""A: X\+(\d+), Y\+(\d+)"""), text)
    val (b1, b2) = parseRegexToTuple(Regex("""B: X\+(\d+), Y\+(\d+)"""), text)
    val (c1, c2) = parseRegexToTuple(Regex("""Prize: X=(\d+), Y=(\d+)"""), text)

    return Equation(a1, b1, c1, a2, b2, c2)
}

fun solveEquation(equation: Equation): Pair<Long, Long>? {
    fun areMultiples(equation: Equation): Boolean =
        equation.a1 * equation.b2 == equation.a2 * equation.b1 &&
                equation.a1 * equation.c2 == equation.a2 * equation.c1 &&
                equation.b1 * equation.c2 == equation.b2 * equation.c1

    if (areMultiples(equation)) { // Infinite solutions
        if (equation.a1 != 0L && equation.c1 % equation.a1 == 0L) {
            val a = equation.c1 / equation.a1
            if (a >= 0) return a to 0L
        }
        if (equation.b1 != 0L && equation.c1 % equation.b1 == 0L) {
            val b = equation.c1 / equation.b1
            if (b >= 0) return 0L to b
        }
        return null
    }

    val delta = equation.a1 * equation.b2 - equation.a2 * equation.b1
    if (delta != 0L) { // Unique solution
        val x = (equation.c1 * equation.b2 - equation.c2 * equation.b1) / delta
        val y = (equation.a1 * equation.c2 - equation.a2 * equation.c1) / delta
        if ((equation.c1 * equation.b2 - equation.c2 * equation.b1) % delta == 0L &&
            (equation.a1 * equation.c2 - equation.a2 * equation.c1) % delta == 0L &&
            x >= 0 && y >= 0
        ) {
            return x to y
        }
    }

    return null
}

fun upgradeClawMachine(equation: Equation): Equation = equation.copy(
    c1 = equation.c1 + 10_000_000_000_000,
    c2 = equation.c2 + 10_000_000_000_000
)

fun solvePart(input: String, upgrade: Boolean = false): Long =
    input.split("\n\n")
        .map(::parseEquation)
        .map { if (upgrade) upgradeClawMachine(it) else it }
        .mapNotNull(::solveEquation)
        .sumOf { (a, b) -> a * 3 + b }

fun part1(input: String): String =
    solvePart(input).toString()

fun part2(input: String): String =
    solvePart(input, upgrade = true).toString()

fun main() {
    val input = readInput("day13").joinToString("\n")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}
