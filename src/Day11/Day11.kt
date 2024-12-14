package Day11

import readInput

fun removeLeadingZeros(s: String): String =
    s.trimStart('0').ifEmpty { "0" }

fun splitNumber(s: String): List<String> =
    listOf(
        s.substring(0, s.length / 2),
        removeLeadingZeros(s.substring(s.length / 2))
    )

fun multiplyNumber(s: String): String =
    (s.toLong() * 2024).toString()

fun blink(stone: String, blinkMap: MutableMap<String, List<String>>): List<String> =
    blinkMap.getOrPut(stone) {
        buildList {
            when {
                stone == "0" -> add("1")
                stone.length % 2 == 0 -> addAll(splitNumber(stone))
                else -> add(multiplyNumber(stone))
            }
        }
    }

fun doBlinks(input: String, blinks: Int): Long {
    val blinkMap = mutableMapOf<String, List<String>>()
    val stoneCounts = mutableMapOf<String, Long>().apply {
        input.split(" ").forEach { this[it] = 1L }
    }

    repeat(blinks) {
        val currentStones = stoneCounts.filter { it.value > 0 }
        currentStones.forEach { (stone, count) ->
            stoneCounts[stone] = stoneCounts[stone]!! - count
            blink(stone, blinkMap).forEach { newStone ->
                stoneCounts[newStone] = stoneCounts.getOrDefault(newStone, 0) + count
            }
        }
    }

    return stoneCounts.values.sum()
}

fun part1(input: String): Long = doBlinks(input, 25)

fun part2(input: String): Long = doBlinks(input, 75)

fun main() {
    val input = readInput("day11").first() // Assuming single-line input for this task
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}
