package Day22

import readInput
import kotlin.collections.Map

fun main() {
    val input = readInput("day22")
    println("Part 1: ${Day22.part1(input, 2000)}")
    println("Part 2: ${Day22.part2(input, 2000)}")
}

fun part1(input: List<String>, sequenceLength: Int): Long {
    fun solve(seed: Long): Long {
        val seq = getRandomSequence(seed, sequenceLength)
        return seq.drop(2000).first()
    }

    return input.map { it.toLong() }
        .sumOf { solve(it) }
}

fun getNext(prev: Long): Long {
    val a = ((prev * 64) xor prev).mod(16777216)
    val b = ((a / 32) xor a).mod(16777216)
    val c = ((b * 2048) xor b).mod(16777216)
    return c.toLong()
}

fun getRandomSequence(seed: Long, length: Int) = generateSequence(seed, ::getNext).take(length + 1)

fun getPriceSequence(seed: Long, length: Int) = getRandomSequence(seed, length).map { it.mod(10) }

fun part2(input: List<String>, sequenceLength: Int): Int {
    fun buildPrefixesMap(seed: Long): Map<Prefix, Int> {
        return HashMap<Prefix, Int>(sequenceLength).apply {
            getPriceSequence(seed, sequenceLength)
                .zipWithNext { a, b -> b to (b - a) }
                .windowed(4)
                .map { window ->
                    val price = window.last().first
                    val prefixes = window.map { it.second }
                    prefixes to price
                }.forEach { (prefixes, price) -> putIfAbsent(prefixes, price) }
        }
    }

    val seeds = input.map { it.toLong() }
    val prefixesToPrices: List<Map<Prefix, Int>> = seeds.map { buildPrefixesMap(it) }
    val uniquePrefixes: Set<Prefix> = prefixesToPrices.flatMap { it.keys }.toSet()

    return uniquePrefixes.maxOf { prefix -> prefixesToPrices.sumOf { it.getOrDefault(prefix, 0) } }
}

private typealias Prefix = List<Int>