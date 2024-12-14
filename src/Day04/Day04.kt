package Day04

import readInput

fun main() {
    val input = readInput("day04")

    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}

private fun parseInput(input: List<String>): List<CharArray> =
    input.map(String::toCharArray)

private fun part1(input: List<String>): Int =
    countXmas(parseInput(input))

private fun part2(input: List<String>): Int =
    countMAS(parseInput(input))

private val directions = listOf(
    Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1),
    Pair(1, 1), Pair(1, -1), Pair(-1, 1), Pair(-1, -1)
)

private fun countXmas(grid: List<CharArray>): Int =
    grid.withIndex().sumOf { (y, row) ->
        row.withIndex().sumOf { (x, c) ->
            if (c != 'X') 0 else getXmasSequences(grid, x, y).count { it == "XMAS" }
        }
    }

private fun getXmasSequences(grid: List<CharArray>, x: Int, y: Int): List<String> =
    directions.mapNotNull { (dx, dy) ->
        generateSequence(0) { it + 1 }.take(4).map { i ->
            val newX = x + dx * i
            val newY = y + dy * i
            grid.getOrNull(newY)?.getOrNull(newX)
        }.joinToString("").takeIf { it.length == 4 }
    }

private fun countMAS(grid: List<CharArray>): Int =
    grid.withIndex().sumOf { (y, row) ->
        row.withIndex().count { (x, c) ->
            c == 'A' && isMAS(grid, x, y)
        }
    }


private fun isMAS(grid: List<CharArray>, x: Int, y: Int): Boolean {
    val adjacentPositions = listOf(
        Pair(x - 1, y - 1), Pair(x + 1, y - 1),
        Pair(x - 1, y + 1), Pair(x + 1, y + 1)
    )

    val neighbors = adjacentPositions.mapNotNull { (nx, ny) ->
        grid.getOrNull(ny)?.getOrNull(nx)
    }

    return neighbors.size == 4 && neighbors.toSet() == setOf('M', 'S') &&
            (neighbors[0] != neighbors[3]) && (neighbors[1] != neighbors[2])
}
