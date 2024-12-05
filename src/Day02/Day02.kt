package Day02

import readInput

fun part1(input: List<String>): Int {
    fun isSafe(report: List<Int>): Boolean {
        val isIncreasing = report.zipWithNext().all { it.second > it.first }
        val isDecreasing = report.zipWithNext().all { it.second < it.first }
        val differencesOk = report.zipWithNext().all { (a, b) -> (1..3).contains(kotlin.math.abs(b - a)) }
        return (isIncreasing || isDecreasing) && differencesOk
    }
    return input.map { it.split(" ").map(String::toInt) }.count { isSafe(it) }
}

fun part2(input: List<String>): Int {
    fun isSafe(report: List<Int>): Boolean {
        val isIncreasing = report.zipWithNext().all { it.second > it.first }
        val isDecreasing = report.zipWithNext().all { it.second < it.first }
        val differencesOk = report.zipWithNext().all { (a, b) -> (1..3).contains(kotlin.math.abs(b - a)) }
        return (isIncreasing || isDecreasing) && differencesOk
    }
    fun isSafeWithDampener(report: List<Int>): Boolean {
        if (isSafe(report)) return true
        for (i in report.indices) {
            val modifiedReport = report.toMutableList().apply { removeAt(i) }
            if (isSafe(modifiedReport)) return true
        }
        return false
    }
    return input.map { it.split(" ").map(String::toInt) }.count { isSafeWithDampener(it) }
}

fun main() {
    val input = readInput("Day02")
    println("Part 1: Safe reports = ${Day01.part1(input)}")
    println("Part 2: Safe reports with Dampener = ${Day01.part2(input)}")
}
