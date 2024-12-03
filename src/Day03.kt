import java.io.File

fun main() {
    val instructions = File("day03").readText()

    val part1 = Regex("""mul\((\d+),(\d+)\)""")
        .findAll(instructions)
        .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }

    val part2 = Regex("""mul\((\d+),(\d+)\)""")
        .findAll(instructions.replace("""don't\(\).*?(do\(\)|$)""".toRegex(RegexOption.DOT_MATCHES_ALL), ""))
        .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }

    println("Part 1 Result: $part1")
    println("Part 2 Result: $part2")
}