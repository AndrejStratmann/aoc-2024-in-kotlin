package Day15

import readInput

data class Position(val row: Int, val col: Int)

fun parseInput(input: List<String>): Pair<List<String>, String> {
    val sections = input.joinToString("\n").split("\n\n")
    return sections[0].lines() to sections[1].trim()
}

fun simulateWarehouse(gridLines: List<String>, moves: String, scaleUp: Boolean): Int {
    val grid = if (scaleUp) scaleUpGrid(gridLines) else gridLines.map { it.toCharArray().toMutableList() }
    val directions = mapOf('<' to Position(0, -1), '>' to Position(0, 1), '^' to Position(-1, 0), 'v' to Position(1, 0))

    var robotPos = findRobotPosition(grid)

    for (move in moves) {
        val direction = directions[move] ?: continue
        val newRobotPos = Position(robotPos.row + direction.row, robotPos.col + direction.col)

        if (canMove(grid, newRobotPos, direction, scaleUp)) {
            moveBoxes(grid, newRobotPos, direction, scaleUp)
            robotPos = newRobotPos
        }
    }

    return calculateGPS(grid, scaleUp)
}

fun findRobotPosition(grid: List<MutableList<Char>>): Position {
    for (row in grid.indices) {
        for (col in grid[row].indices) {
            if (grid[row][col] == '@') {
                grid[row][col] = '.'
                return Position(row, col)
            }
        }
    }
    throw IllegalArgumentException("Robot position not found in the grid")
}

fun scaleUpGrid(gridLines: List<String>): MutableList<MutableList<Char>> {
    return gridLines.map { line ->
        line.flatMap { char ->
            when (char) {
                '#' -> listOf('#', '#')
                'O' -> listOf('[', ']')
                '.' -> listOf('.', '.')
                '@' -> listOf('@', '.')
                else -> listOf('.')
            }
        }.toMutableList()
    }.toMutableList()
}

fun canMove(grid: List<MutableList<Char>>, pos: Position, direction: Position, scaleUp: Boolean): Boolean {
    val (row, col) = pos
    if (row !in grid.indices || col !in grid[row].indices || grid[row][col] == '#') return false

    if (scaleUp && grid[row][col] == '[' && grid[row][col + 1] == ']') {
        val nextRow = row + direction.row
        val nextCol = col + direction.col
        val nextCol2 = nextCol + 1
        return nextRow in grid.indices &&
                nextCol in grid[nextRow].indices &&
                nextCol2 in grid[nextRow].indices &&
                grid[nextRow][nextCol] == '.' &&
                grid[nextRow][nextCol2] == '.'
    }

    return true
}

fun moveBoxes(grid: List<MutableList<Char>>, pos: Position, direction: Position, scaleUp: Boolean) {
    val (row, col) = pos
    if (scaleUp && grid[row][col] == '[' && grid[row][col + 1] == ']') {
        val nextRow = row + direction.row
        val nextCol = col + direction.col
        val nextCol2 = nextCol + 1

        grid[nextRow][nextCol] = '['
        grid[nextRow][nextCol2] = ']'
        grid[row][col] = '.'
        grid[row][col + 1] = '.'
    }
}

fun calculateGPS(grid: List<MutableList<Char>>, scaleUp: Boolean): Int {
    return grid.indices.sumOf { row ->
        grid[row].indices.sumOf { col ->
            when {
                scaleUp && grid[row][col] == '[' -> 100 * row + col
                !scaleUp && grid[row][col] == 'O' -> 100 * row + col
                else -> 0
            }
        }
    }
}

fun part1(input: List<String>): Int {
    val (gridLines, moves) = parseInput(input)
    return simulateWarehouse(gridLines, moves, scaleUp = false)
}

fun part2(input: List<String>): Int {
    val (gridLines, moves) = parseInput(input)
    return simulateWarehouse(gridLines, moves, scaleUp = true)
}

fun main() {
    val input = readInput("day15")
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}
