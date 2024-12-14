package Day07

import readInput

fun main() {
    val input = readInput("Day07")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}

fun String.calc(n1: Long, n2: Long): Long = when (this) {
    "+" -> n1 + n2
    "*" -> n1 * n2
    "||" -> (n1.toString() + n2.toString()).toLong()
    else -> error("Invalid operator $this")
}

fun List<String>.repeat(times: Int): List<List<String>> =
    List(times) { this }

data class Operation(val result: Long, val numbers: List<Long>) {
    fun isValid(operands: List<String>): Boolean {
        val allPossibleResults = cartesian(operands.repeat(numbers.size - 1))
            .map { ops ->
                numbers.drop(1).foldIndexed(numbers.first()) { index, acc, num ->
                    ops[index].calc(acc, num)
                }
            }
        return result in allPossibleResults
    }
}

fun parse(input: List<String>): List<Operation> =
    input.map { line ->
        line.split(":").let {
            Operation(it[0].trim().toLong(), it[1].trim().split(" ").map(String::toLong))
        }
    }

fun part1(input: List<String>): Long =
    parse(input).filter { it.isValid(listOf("+", "*")) }.sumOf { it.result }

fun part2(input: List<String>): Long =
    parse(input).filter { it.isValid(listOf("*", "+", "||")) }.sumOf { it.result }

fun cartesian(lists: List<List<String>>): Sequence<List<String>> = sequence {
    val indices = IntArray(lists.size)
    while (true) {
        yield(lists.mapIndexed { index, list -> list[indices[index]] })
        for (i in indices.lastIndex downTo 0) {
            if (indices[i] == lists[i].size - 1) {
                if (i == 0) return@sequence
                indices[i] = 0
            } else {
                indices[i]++
                break
            }
        }
    }
}

