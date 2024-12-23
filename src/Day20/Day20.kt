package Day20

import kotlin.math.abs
import readInput

class Day20(input: String) {
    private val path = buildSet {
        val lines = input.lines()
        var position = lines.withIndex().firstNotNullOf { (y, line) ->
            val x = line.indexOf('S')
            if (x < 0) null else y to x
        }
        add(position)
        do {
            position = position.adjacencies().single { next ->
                val (y, x) = next
                y in lines.indices && x in lines[y].indices && lines[y][x] != '#' && add(next)
            }
        } while (lines[position.first][position.second] != 'E')
    }.withIndex().sortedWith(compareBy({ it.value.first }, { it.value.second }))

    fun part1(time: Int = 100) = solve(2, time)

    fun part2(time: Int = 100) = solve(20, time)

    private fun solve(cheats: Int, time: Int): Int {
        var count = 0
        for (i in path.indices) {
            val (t1, pos1) = path[i]
            val limit = pos1.first + cheats to pos1.second
            for (j in i + 1..<path.size) {
                val (t2, pos2) = path[j]
                if (compareBy<Pair<Int, Int>>({ it.first }, { it.second }).compare(pos2, limit) > 0) break
                val distance = abs(pos2.first - pos1.first) + abs(pos2.second - pos1.second)
                if (distance <= cheats && distance + time <= abs(t2 - t1)) count++
            }
        }
        return count
    }

    companion object {
        private fun Pair<Int, Int>.adjacencies() =
            arrayOf(first - 1 to second, first to second - 1, first to second + 1, first + 1 to second)
    }
}

fun main() {
    val input = readInput("day20").joinToString("\n")
    val day20 = Day20(input)

    println("Day 20: ")
    println("Part 1: ${day20.part1()}")
    println("Part 2: ${day20.part2()}")
}
