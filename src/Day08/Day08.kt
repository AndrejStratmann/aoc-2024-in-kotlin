package Day08

import kotlin.math.abs
import readInput

data class Grid(
    val antennas: Map<Char, List<Pos>>,
    val width: Int,
    val height: Int
) {
    operator fun contains(pos: Pos): Boolean = pos.row in 0 until height && pos.col in 0 until width
}

data class Pos(val row: Int, val col: Int) {
    operator fun minus(other: Pos): Pos = Pos(row - other.row, col - other.col)
    operator fun plus(other: Pos): Pos = Pos(row + other.row, col + other.col)

    fun normalized(): Pos {
        val gcd = gcd(abs(row), abs(col))
        return Pos(row / gcd, col / gcd)
    }
}

tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

fun solve(grid: Grid, op: (Pos, Pos) -> Sequence<Pos>): Int = buildSet {
    grid.antennas.values.forEach { positions ->
        positions.indices.forEach { i1 ->
            val pos1 = positions[i1]
            (i1 + 1..positions.lastIndex).forEach { i2 ->
                val pos2 = positions[i2]
                op(pos1, pos2).forEach { add(it) }
            }
        }
    }
}.count()

fun part1(grid: Grid): Int = solve(grid) { pos1, pos2 ->
    val delta = pos2 - pos1
    sequenceOf(pos1 - delta, pos2 + delta).filter { it in grid }
}

fun part2(grid: Grid): Int = solve(grid) { pos1, pos2 ->
    val delta = (pos2 - pos1).normalized()
    var p = pos1 - delta
    while (p in grid) p -= delta
    generateSequence(p) { it + delta }
        .dropWhile { it !in grid }
        .takeWhile { it in grid }
}

fun parse(input: List<String>): Grid {
    val antennas = mutableMapOf<Char, MutableList<Pos>>()
    input.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { col, ch -> if (ch != '.') ch to Pos(row, col) else null }
    }.forEach { (ch, pos) ->
        antennas.getOrPut(ch) { mutableListOf() }.add(pos)
    }

    return Grid(antennas, input.first().length, input.size)
}


fun main() {
    val input = readInput("day08")
    val grid = parse(input) // Parse the list of strings into a Grid object.
    println("Part 1 Result: ${part1(grid)}")
    println("Part 2 Result: ${part2(grid)}")
}
