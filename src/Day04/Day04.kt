private typealias WordSearch = List<String>

fun main() {
    fun WordSearch.dim() = size to first().length
    fun WordSearch.mirrorHor() = map(String::reversed)
    fun WordSearch.mirrorVer() = reversed()

    fun countMatches(
        ws: WordSearch,
        patterns: List<Pair<Int, Int>>,
        word: String
    ): Int {
        val (n, m) = ws.dim()
        return (0 until n - patterns.maxOf { it.first })
            .sumOf { i ->
                (0 until m - patterns.maxOf { it.second })
                    .count { j ->
                        patterns.indices.all { k ->
                            val (dx, dy) = patterns[k]
                            ws[i + dx][j + dy] == word[k]
                        }
                    }
            }
    }

    fun countPatterns(
        input: WordSearch,
        word: String,
        patterns: List<List<Pair<Int, Int>>>
    ): Int = patterns.sumOf { pattern ->
        countMatches(input, pattern, word) +
                countMatches(input.mirrorHor(), pattern, word) +
                countMatches(input.mirrorVer(), pattern, word) +
                countMatches(input.mirrorHor().mirrorVer(), pattern, word)
    }

    fun part1(input: WordSearch): Int {
        val xmas = "XMAS"
        val patterns = listOf(
            // Horizontal
            listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
            // Vertical
            listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
            // Diagonal
            listOf(0 to 0, 1 to 1, 2 to 2, 3 to 3)
        )
        return countPatterns(input, xmas, patterns)
    }

    fun part2(input: WordSearch): Int {
        val masPattern1 = listOf(
            listOf(0 to 0, 2 to 0, 1 to 1, 0 to 2, 2 to 2), // "M.A.S"
            listOf(0 to 0, 0 to 2, 1 to 1, 2 to 0, 2 to 2)  // "M.M.S"
        )
        return countPatterns(input, "MASS", masPattern1)
    }

    val input = readInput("day04")
    println(part1(input))
    println(part2(input))
}