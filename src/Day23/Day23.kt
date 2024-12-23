import kotlin.collections.Map

fun main() {

    fun parseInput(input: List<String>): List<Pair<String, String>> {
        return input.map { it.split('-') }
            .map { Pair(it[0], it[1]) }
    }

    fun buildConnectionsMap(connections: List<Pair<String, String>>): Map<String, Set<String>> {
        return buildMap<String, MutableSet<String>> {
            connections.forEach { (left, right) ->
                computeIfAbsent(left) { mutableSetOf() }.add(right)
                computeIfAbsent(right) { mutableSetOf() }.add(left)
            }
        }.toMap()
    }

    fun part1(input: List<String>): Int {
        fun isCorrectName(potentialClique: Triple<String, String, String>): Boolean {
            return potentialClique.first.startsWith('t') ||
                    potentialClique.second.startsWith('t') ||
                    potentialClique.third.startsWith('t')
        }

        fun isClique(first: String, second: String, third: String, connections: Map<String, Set<String>>): Boolean {
            fun isConnected(a: String, b: String): Boolean {
                return connections.getValue(a).contains(b)
            }

            return isConnected(first, second) && isConnected(second, third) && isConnected(first, third)
        }

        val connections = parseInput(input)
        val connectionsMap = buildConnectionsMap(connections)
        val possibleComputers = connectionsMap.filterValues { it.size >= 2 }.keys

        val cliques = buildList {
            possibleComputers.forEachIndexed { firstIndex, first ->
                possibleComputers.drop(firstIndex + 1).forEachIndexed { secondIndex, second ->
                    possibleComputers.drop(firstIndex + secondIndex + 2).forEach { third ->
                        val potentialClique = Triple(first, second, third)
                        if (isCorrectName(potentialClique) && isClique(first, second, third, connectionsMap)) {
                            add(potentialClique)
                        }
                    }
                }
            }
        }

        return cliques.size
    }

    fun part2(input: List<String>): String {
        val connections = parseInput(input)
        val connectionsMap = buildConnectionsMap(connections)

        fun bronKerbosh(p: MutableSet<String>, r: Set<String>, x: MutableSet<String>, result: MutableSet<Set<String>>) {
            if (x.isEmpty() && p.isEmpty()) {
                result.add(r)
            }

            p.toList().forEach { v ->
                val neighbors = connectionsMap.getValue(v)
                bronKerbosh(p.intersect(neighbors).toMutableSet(), r + v, x.intersect(neighbors).toMutableSet(), result)

                p.remove(v)
                x.add(v)
            }
        }

        fun findMaximumClique(p: Set<String>): Set<String> {
            val result = mutableSetOf<Set<String>>()

            bronKerbosh(p.toMutableSet(), emptySet(), mutableSetOf(), result)

            return result.maxBy { it.size }
        }

        val clique = findMaximumClique(connectionsMap.keys)
        return clique.sorted().joinToString(separator = ",")
    }

    val input = readInput("day23")
    part1(input).println()
    part2(input).println()
}