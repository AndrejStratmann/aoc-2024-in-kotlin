package Day10

import readInput

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
}

data class Position(val x: Int, val y: Int) {
    fun isOnGrid(gridSize: Int): Boolean =
        (x in 0 until gridSize && y in 0 until gridSize)

    fun translate(direction: Direction): Position =
        Position(x + direction.dx, y + direction.dy)
}

data class Point(val pos: Position, val height: Int) {
    fun isTrailhead(): Boolean = (height == 0)
    fun isPeak(): Boolean = (height == 9)

    fun moves(points: List<Point>, gridSize: Int): List<Point> =
        Direction.values()
            .map { pos.translate(it) }
            .filter { it.isOnGrid(gridSize) }
            .mapNotNull { nextPos -> points.find { it.pos == nextPos && it.height == height + 1 } }
}

fun parse(input: List<String>): List<Point> =
    input.flatMapIndexed { row, line ->
        line.mapIndexed { col, char -> Point(Position(col, row), char.digitToInt()) }
    }

fun getScore(points: List<Point>, gridSize: Int, allowRevisit: Boolean): Int {
    var score = 0

    points.filter { it.isTrailhead() }.forEach { trailhead ->
        val queue = ArrayDeque<Point>()
        val visited = mutableSetOf<Point>()

        queue.add(trailhead)

        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()

            if (allowRevisit || point !in visited) {
                visited.add(point)

                if (point.isPeak()) {
                    score++
                } else {
                    queue.addAll(point.moves(points, gridSize))
                }
            }
        }
    }

    return score
}

fun part1(input: List<String>): Int {
    val points = parse(input)
    return getScore(points, input.size, false)
}

fun part2(input: List<String>): Int {
    val points = parse(input)
    return getScore(points, input.size, true)
}

fun main() {
    val input = readInput("day10")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}
