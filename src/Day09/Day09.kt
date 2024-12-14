package Day09

import kotlin.math.min
import readInput

sealed interface Content {
    val length: Int
}

data class Block(val id: Int, override val length: Int) : Content
data class FreeSpace(override val length: Int) : Content


fun parse(input: String): List<Content> = buildList {
    input.chunked(2).forEachIndexed { i, pair ->
        add(Block(i, pair[0].digitToInt()))
        if (pair.length > 1) add(FreeSpace(pair[1].digitToInt()))
    }
}

tailrec fun compress(diskMap: List<Content>): List<Content> {
    val lastBlockIndex = diskMap.indexOfLast { it is Block }
    val firstFreeIndex = diskMap.indexOfFirst { it is FreeSpace }

    if (firstFreeIndex > lastBlockIndex) return diskMap

    val block = diskMap[lastBlockIndex] as Block
    val freeSpace = diskMap[firstFreeIndex] as FreeSpace
    val moveAmount = min(freeSpace.length, block.length)

    return compress(buildList {
        addAll(diskMap.subList(0, firstFreeIndex))
        add(Block(block.id, moveAmount))
        if (freeSpace.length > moveAmount) add(FreeSpace(freeSpace.length - moveAmount))
        addAll(diskMap.subList(firstFreeIndex + 1, lastBlockIndex))
        if (block.length > moveAmount) add(Block(block.id, block.length - moveAmount))
        addAll(diskMap.subList(lastBlockIndex + 1, diskMap.size))
    })
}

fun checksum(diskMap: List<Content>): Long = diskMap.flatMap { content ->
    when (content) {
        is Block -> List(content.length) { content.id.toLong() }
        is FreeSpace -> List(content.length) { 0L }
        else -> error("Unexpected content type: $content")
    }
}.mapIndexed { index, id -> index * id }.sum()

fun part1(input: String): Long = checksum(compress(parse(input)))

fun part2(input: String): Long {
    val initialDiskMap = parse(input)
    val lastBlockId = (initialDiskMap.last { it is Block } as Block).id

    var diskMap = initialDiskMap

    (lastBlockId downTo 0).forEach { id ->
        val blockPosition = diskMap.indices.last { diskMap[it] is Block && (diskMap[it] as Block).id == id }
        val block = diskMap[blockPosition] as Block

        // Find the first free space before the block that can fit it
        val freeSpacePosition = diskMap.indices.firstOrNull {
            diskMap[it] is FreeSpace && diskMap[it].length >= block.length && it < blockPosition
        }

        if (freeSpacePosition != null) {
            val freeSpace = diskMap[freeSpacePosition] as FreeSpace
            val freeSpaceRemaining = freeSpace.length - block.length

            diskMap = buildList {
                addAll(diskMap.subList(0, freeSpacePosition))
                add(block)
                if (freeSpaceRemaining > 0) {
                    add(FreeSpace(freeSpaceRemaining))
                }
                addAll(diskMap.subList(freeSpacePosition + 1, blockPosition))
                add(FreeSpace(block.length))
                addAll(diskMap.subList(blockPosition + 1, diskMap.size))
            }
        }
    }

    return checksum(diskMap)
}

fun main() {
    val input = readInput("day09").first()
    println("Part 1 Result: ${part1(input)}")
    println("Part 2 Result: ${part2(input)}")
}
