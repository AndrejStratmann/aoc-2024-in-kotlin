package Day05

import readInput

fun main() {
    val input = readInput("day05")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}

fun List<String>.isRightOrder(comparator: Comparator<String>) = this.sortedWith(comparator) == this

fun Set<String>.toComparator() = Comparator<String> { a, b ->
    when {
        "$a|$b" in this -> -1
        "$b|$a" in this -> 1
        else -> 0
    }
}

fun part1(input: List<String>): Int {
    val (rules, updates) = parseInput(input)
    val comparator = rules.toComparator()
    return updates.filter { it.isRightOrder(comparator) }
        .sumOf { it[it.size / 2].toInt() }
}

fun part2(input: List<String>): Int {
    val (rules, updates) = parseInput(input)
    val comparator = rules.toComparator()
    return updates.filterNot { it.isRightOrder(comparator) }
        .map { it.sortedWith(comparator) }
        .sumOf { it[it.size / 2].toInt() }
}

fun parseInput(input: List<String>): Pair<Set<String>, List<List<String>>> {
    val rules = input.takeWhile { it.isNotEmpty() }.toSet()
    val updates = input.dropWhile { it.isNotEmpty() }.drop(1).map { it.split(",") }
    return rules to updates
}
