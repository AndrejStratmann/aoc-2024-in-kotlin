package Day06

import readInput

data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction) = when (direction) {
        Direction.NORTH -> Point(x, y - 1)
        Direction.EAST -> Point(x + 1, y)
        Direction.SOUTH -> Point(x, y + 1)
        Direction.WEST -> Point(x - 1, y)
    }
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun rotate(turn: Turn) = when (turn) {
        Turn.RIGHT -> Direction.values()[(ordinal + 1) % 4]
        Turn.LEFT -> Direction.values()[(if (ordinal - 1 < 0) 3 else ordinal - 1) % 4]
    }
}

enum class Turn {
    RIGHT, LEFT
}

fun main() {
    fun parse(input: List<String>): MutableMap<Point, Char> {
        val matrix = mutableMapOf<Point, Char>()
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                matrix[Point(x, y)] = c
            }
        }
        return matrix
    }

    fun MutableMap<Point, Char>.traverse(obstacle: Point? = null): Pair<Set<Point>, Boolean> {
        val seen = mutableSetOf<Pair<Point, Direction>>()
        var direction = Direction.NORTH
        var position = this.entries.find { it.value == '^' }!!.key

        obstacle?.let { this[it] = '#' }

        while (this[position] != null && (position to direction) !in seen) {
            seen += position to direction
            val next = position.move(direction)

            if (this[next] == '#') {
                direction = direction.rotate(Turn.RIGHT)
            } else {
                position = next
            }
        }

        obstacle?.let { this[it] = '.' }

        val loop = this[position] != null
        return seen.map { it.first }.toSet() to loop
    }

    fun part1(input: List<String>) = parse(input).traverse().first.size

    fun part2(input: List<String>): Int {
        val grid = parse(input)
        val start = grid.entries.find { it.value == '^' }!!.key

        return grid.traverse()
            .first
            .filterNot { it == start }
            .count { grid.traverse(it).second }
    }

    val input = readInput("Day06")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}


fun <T> println(item: T) = kotlin.io.println(item)
