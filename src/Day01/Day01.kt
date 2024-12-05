package Day01

import readInput

fun part1(input: List<String>): Int {
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()
    for (line in input) {
        val (left, right) = line.split("\\s+".toRegex()).map(String::toInt)
        leftList.add(left)
        rightList.add(right)
    }
    return leftList.sorted().zip(rightList.sorted()).sumOf { (left, right) -> kotlin.math.abs(left - right) }
}

fun part2(input: List<String>): Int {
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()
    for (line in input) {
        val (left, right) = line.split("\\s+".toRegex()).map(String::toInt)
        leftList.add(left)
        rightList.add(right)
    }
    val rightCount = rightList.groupingBy { it }.eachCount()
    return leftList.sumOf { number -> number * (rightCount[number] ?: 0) }
}

fun main() {
    val input = readInput("day01")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}