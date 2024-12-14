package Day03

import readInput

fun part1(instructions: String): Long {
    val multiplicationRegex = Regex("""mul\((\d+),(\d+)\)""")
    return multiplicationRegex.findAll(instructions)
        .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }
}

fun part2(instructions: String): Long {
    val cleanInstructions = instructions.replace("""don't\(\).*?(do\(\)|$)""".toRegex(RegexOption.DOT_MATCHES_ALL), "")
    val multiplicationRegex = Regex("""mul\((\d+),(\d+)\)""")
    return multiplicationRegex.findAll(cleanInstructions)
        .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }
}

fun main() {
    val instructions = readInput("day03").joinToString("\n")
    println("Part 1 Result: ${part1(instructions)}")
    println("Part 2 Result: ${part2(instructions)}")
}
