import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    fun List<String>.toRocks(): Set<Pair<Int, Int>> {
        return this.flatMap { line ->
            line
                .split(" -> ")
                .map {
                    it.split(",").map { it.toInt() }
                }
                .zipWithNext()
                .map {
                    val (firstX, firstY) = it.first
                    val (secondX, secondY) = it.second
                    val rocks = mutableSetOf<Pair<Int, Int>>()
                    for (y in min(firstY, secondY)..max(firstY, secondY)) {
                        for (x in min(firstX, secondX)..max(firstX, secondX)) {
                            rocks.add(x to y)
                        }
                    }
                    rocks
                }.fold(mutableSetOf<Pair<Int, Int>>()) { acc, i ->
                    acc.addAll(i)
                    acc
                }
        }.fold(mutableSetOf<Pair<Int, Int>>()) { acc, i ->
            acc.add(i)
            acc
        }
    }

    fun findNewPosition(
        currentPosition: Pair<Int, Int>,
        rocks: Set<Pair<Int, Int>>,
        restingSand: Set<Pair<Int, Int>>,
        ground: Int = Int.MAX_VALUE
    ): Pair<Int, Int> {
        val obstacles = mutableSetOf<Pair<Int, Int>>()
        obstacles.addAll(rocks)
        obstacles.addAll(restingSand)

        if (currentPosition.second + 1 == ground)
            return currentPosition

        if (currentPosition.first to currentPosition.second + 1 !in obstacles) {
            return currentPosition.first to currentPosition.second + 1
        }
        if (currentPosition.first - 1 to currentPosition.second + 1 !in obstacles) {
            return currentPosition.first - 1 to currentPosition.second + 1
        }
        if (currentPosition.first + 1 to currentPosition.second + 1 !in obstacles) {
            return currentPosition.first + 1 to currentPosition.second + 1
        }

        return currentPosition
    }

    fun printCave(
        xRange: IntRange,
        yRange: IntRange,
        rocks: Set<Pair<Int, Int>>,
        restingSand: Set<Pair<Int, Int>>,
        ground: Int = -1
    ) {
        for (y in yRange) {
            for (x in xRange) {
                val symbol = when (x to y) {
                    x to ground,
                    in rocks -> "#"

                    in restingSand -> "o"
                    else -> "."
                }
                print(symbol)
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        val rocks = input.toRocks()

        val lowestRock = rocks.maxOf {
            it.second
        }

        val restingSand = mutableSetOf<Pair<Int, Int>>()

        var sandRunning = true
        while (sandRunning) {
            var fallingSand = 500 to 0
            var newPosition = fallingSand

            do {
                fallingSand = newPosition

                if (fallingSand.second > lowestRock) {
                    sandRunning = false
                    break
                }

                newPosition = findNewPosition(fallingSand, rocks, restingSand)
            } while (fallingSand != newPosition)

            if (sandRunning) {
                restingSand += fallingSand
            }
        }

//        printCave(494 until 504, 0 until 10, rocks, restingSand)

        return restingSand.count()
    }

    fun part2(input: List<String>): Int {
        val rocks = input.toRocks()

        val ground = rocks.maxOf {
            it.second + 2
        }

        val restingSand = mutableSetOf<Pair<Int, Int>>()

        for (i in 0 until 100000) {
            var fallingSand = 500 to 0
            var newPosition = fallingSand

            do {
                fallingSand = newPosition

                newPosition = findNewPosition(fallingSand, rocks, restingSand, ground)
            } while (fallingSand != newPosition)

            restingSand += fallingSand
            if (fallingSand == 500 to 0) {
                break
            }

//            if (i % 1000 == 0) {
//                println(i)
//                printCave(500 - ground .. 500 + ground, 0 .. ground, rocks, restingSand, ground)
//            }
        }

        return restingSand.count()
    }

    val testInput = readInput("Day14Test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}