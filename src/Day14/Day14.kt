package Day14

import readInput

data class Vec2(val x: Int, val y: Int) {
    companion object {
        fun fromString(s: String) = s.split(",").map { it.toInt() }.let { Vec2(it[0], it[1]) }
    }

    operator fun plus(delta: Vec2) = Vec2(x + delta.x, y + delta.y)

    fun wrapped(dimensions: Vec2): Vec2 = Vec2(
        x = (x % dimensions.x + dimensions.x) % dimensions.x,
        y = (y % dimensions.y + dimensions.y) % dimensions.y
    )
}

data class Robot(var position: Vec2, val velocity: Vec2) {
    fun moveAndWrap(space: Space) {
        position = (position + velocity).wrapped(space.dimensions)
    }

    fun getQuadrant(space: Space): Quadrant {
        val middleX = (space.dimensions.x - 1) / 2
        val middleY = (space.dimensions.y - 1) / 2

        return when {
            position.x < middleX && position.y < middleY -> Quadrant.TOP_LEFT
            position.x > middleX && position.y < middleY -> Quadrant.TOP_RIGHT
            position.x < middleX && position.y > middleY -> Quadrant.BOTTOM_LEFT
            position.x > middleX && position.y > middleY -> Quadrant.BOTTOM_RIGHT
            else -> Quadrant.AXIS
        }
    }

    enum class Quadrant {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, AXIS
    }
}

data class Space(val dimensions: Vec2) {
    private val rows = 0 until dimensions.y
    private val cols = 0 until dimensions.x

    fun renderMap(robots: List<Robot>): String =
        rows.joinToString("\n") { y ->
            cols.joinToString("") { x ->
                val count = robots.count { it.position == Vec2(x, y) }
                if (count == 0) "." else count.toString()
            }
        }

    private fun quadrants(): List<Pair<IntRange, IntRange>> {
        val left = 0 until dimensions.x / 2
        val right = dimensions.x / 2 until dimensions.x
        val top = 0 until dimensions.y / 2
        val bottom = dimensions.y / 2 until dimensions.y

        return listOf(left to top, left to bottom, right to top, right to bottom)
    }

    fun quadrantCounts(robots: List<Robot>): List<Int> =
        quadrants().map { (xRange, yRange) ->
            robots.count { it.position.x in xRange && it.position.y in yRange }
        }

    fun clusteringScore(robots: List<Robot>): Int =
        rows.sumOf { y ->
            robots.filter { it.position.y == y }
                .map { it.position.x }
                .sorted()
                .zipWithNext()
                .sumOf { (first, second) -> second - first }
        }
}

fun parseInput(input: List<String>): List<Robot> =
    input.map { line ->
        val position = Vec2.fromString(line.substringBefore(" ").substringAfter("="))
        val velocity = Vec2.fromString(line.substringAfter(" ").substringAfter("="))
        Robot(position, velocity)
    }

fun part1(input: List<String>, space: Space): Long {
    val robots = parseInput(input)
    repeat(100) { robots.forEach { it.moveAndWrap(space) } }
    return robots.groupingBy { it.getQuadrant(space) }
        .eachCount()
        .filterKeys { it != Robot.Quadrant.AXIS }
        .values
        .reduce(Int::times)
        .toLong()
}

fun part2(input: List<String>, space: Space): Day14Result {
    val robots = parseInput(input)
    var bestResult = Day14Result(0, "", Int.MAX_VALUE)

    for (iteration in 1..space.dimensions.x * space.dimensions.y) {
        robots.forEach { it.moveAndWrap(space) }
        val score = space.clusteringScore(robots)
        if (score < bestResult.score) {
            bestResult = Day14Result(iteration, space.renderMap(robots), score)
        }
    }

    return bestResult
}

data class Day14Result(val seconds: Int, val map: String, val score: Int)

fun main() {
    val input = readInput("day14")
    val space = Space(Vec2(101, 103))

    println("Part 1 Result: ${part1(input, space)}")
    val part2Result = part2(input, space)
    println("Part 2 Map:\n${part2Result.map}")
    println("Part 2 Seconds: ${part2Result.seconds}")
}
