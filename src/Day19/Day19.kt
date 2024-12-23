package Day19

import readInput

private fun getValidDesigns(designs: List<String>, towels: List<String>): List<String> = designs
    .filter {
        Regex("""^(${towels.joinToString("|")})+$""")
            .matches(it)
    }

private fun isValidDesign(design: String, towels: List<String>): Boolean =
    Regex("""^(${towels.joinToString("|")})+$""")
        .matches(design)

private fun waysToMake(
    design: String,
    towels: List<String>,
    memo: HashMap<String, Long>
): Long {
    if (memo.contains(design)) return memo[design]!!
    if (!isValidDesign(design, towels)) return 0

    val result = towels.sumOf {
        when {
            design == it -> 1
            design.startsWith(it) -> waysToMake(design.removePrefix(it), towels, memo)
            else -> 0
        }
    }

    memo[design] = result
    return result
}

fun solvePart1(input: String): String {
    val towels = input
        .split("\n\n")[0]
        .split(", ")
    val designs = input
        .split("\n\n")[1]
        .lines()

    return getValidDesigns(designs, towels)
        .count()
        .toString()
}

fun solvePart2(input: String): String {
    val towels = input
        .split("\n\n")[0]
        .split(", ")
    val designs = input
        .split("\n\n")[1]
        .lines()

    val memo = HashMap<String, Long>()

    return designs
        .sumOf { waysToMake(it, towels, memo) }
        .toString()
}

fun main() {
    val input: String = readInput("day19").joinToString("\n")

    println("Day 19: ")
    println("Part 1: ${solvePart1(input)}")
    println("Part 2: ${solvePart2(input)}")
}
