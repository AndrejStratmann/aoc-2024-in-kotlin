package Day18

import java.util.PriorityQueue
import readInput

const val MAP_SIZE = 71
const val NUM_BYTES = 1024

private typealias Map = HashMap<Vec2, MemoryType>

private data class Vec2(var x: Int, var y: Int) {
    constructor(list: List<Int>) : this(list[0], list[1])

    fun getValidNeighbours(): List<Vec2> {
        return listOf(
            Vec2(x + 1, y),
            Vec2(x - 1, y),
            Vec2(x, y + 1),
            Vec2(x, y - 1)
        ).filter { it.x in 0 until MAP_SIZE && it.y in 0 until MAP_SIZE }
    }

    override fun toString(): String = "$x,$y"
}

private enum class MemoryType { CORRUPTED, SAFE }

private fun dijkstra(start: Vec2, end: Vec2, map: Map): Int {
    val visited = HashSet<Vec2>()
    val costs = HashMap<Vec2, Int>()
    val queue = PriorityQueue<Pair<Vec2, Int>>(compareBy { it.second })

    queue.add(start to 0)
    while (queue.isNotEmpty()) {
        val (currPos, currCost) = queue.poll()
        if (currPos == end) return currCost

        if (visited.contains(currPos)) continue
        visited.add(currPos)

        val neighbours = currPos.getValidNeighbours()
        for (neighbour in neighbours) {
            if (map.getOrDefault(
                    neighbour,
                    MemoryType.SAFE
                ) == MemoryType.CORRUPTED || visited.contains(neighbour)
            ) continue
            val nextCost = currCost + 1
            if (nextCost < costs.getOrDefault(neighbour, Int.MAX_VALUE)) {
                costs[neighbour] = nextCost
                queue.add(neighbour to nextCost)
            }
        }
    }
    return Int.MAX_VALUE
}

private fun canFindPath(start: Vec2, end: Vec2, map: Map): Boolean {
    val visited = HashSet<Vec2>()
    val queue = PriorityQueue<Pair<Vec2, Int>>(compareBy { it.second })

    queue.add(start to 0)
    while (queue.isNotEmpty()) {
        val (currPos, _) = queue.poll()
        if (currPos == end) return true

        if (visited.contains(currPos)) continue
        visited.add(currPos)

        val neighbours = currPos.getValidNeighbours()
        for (neighbour in neighbours) {
            if (map.getOrDefault(
                    neighbour,
                    MemoryType.SAFE
                ) == MemoryType.CORRUPTED || visited.contains(neighbour)
            ) continue
            queue.add(neighbour to 0)
        }
    }
    return false
}

fun solvePart1(input: String): String {
    val bytes = input.lines().map { Vec2(it.split(",").map { it.toInt() }) }
    val map = (0 until NUM_BYTES).associate { bytes[it] to MemoryType.CORRUPTED }.toMap()

    val start = Vec2(0, 0)
    val end = Vec2(MAP_SIZE - 1, MAP_SIZE - 1)

    return dijkstra(start, end, map as Map).toString()
}

fun solvePart2(input: String): String {
    val bytes = input.lines().map { Vec2(it.split(",").map { it.toInt() }) }
    val map = (0 until NUM_BYTES).associate { bytes[it] to MemoryType.CORRUPTED }.toMutableMap()

    val start = Vec2(0, 0)
    val end = Vec2(MAP_SIZE - 1, MAP_SIZE - 1)

    var currentByte = NUM_BYTES - 1

    while (canFindPath(start, end, map as Map)) {
        map[bytes[currentByte]] = MemoryType.CORRUPTED
        currentByte++
    }

    return bytes[currentByte - 1].toString()
}

fun main() {
    val input: String = readInput("day18").joinToString("\n")

    println("Day 18: ")
    println("Part 1: ${solvePart1(input)}")
    println("Part 2: ${solvePart2(input)}")
}