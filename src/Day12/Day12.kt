package Day12

import readInput

typealias Grid = List<String>
typealias Point2D = Pair<Int, Int>
typealias PairOf<T> = Pair<T, T>

private val directions = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)

private fun Point2D.rotate(): Point2D =
    second to -first

private operator fun Grid.get(p: Point2D): Char? =
    this.getOrNull(p.first)?.getOrNull(p.second)

private operator fun Point2D.plus(direction: Point2D): Point2D =
    first + direction.first to second + direction.second

fun floodfill(grid: Grid, start: Point2D): Set<Point2D> {
    val points = mutableSetOf(start)
    val queue = ArrayDeque<Point2D>().apply { add(start) }
    val targetChar = grid[start]

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        directions.forEach { dir ->
            val neighbor = current + dir
            if (neighbor !in points && grid[neighbor] == targetChar) {
                points.add(neighbor)
                queue.add(neighbor)
            }
        }
    }

    return points
}

fun scoreRegion(points: Set<Point2D>): PairOf<Int> {
    val area = points.size
    val (perimeter1, perimeter2) = points.flatMap { p -> directions.map { p to it } }
        .filter { it.first + it.second !in points }
        .fold(0 to 0) { (p1, p2), (p, d) ->
            val neighbor = p + d.rotate()
            when {
                neighbor !in points || neighbor + d in points -> p1 + 1 to p2 + 1
                else -> p1 + 1 to p2
            }
        }
    return area * perimeter1 to area * perimeter2
}

fun solve(input: Grid): PairOf<Int> {
    val seen = mutableSetOf<Point2D>()
    var result = 0 to 0

    input.forEachIndexed { row, line ->
        line.indices.forEach { col ->
            val start = row to col
            if (start !in seen) {
                val regionPoints = floodfill(input, start)
                seen.addAll(regionPoints)
                result += scoreRegion(regionPoints)
            }
        }
    }

    return result
}

fun part1(input: Grid): Int = solve(input).first
fun part2(input: Grid): Int = solve(input).second

fun main() {
    val input = readInput("day12")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}
