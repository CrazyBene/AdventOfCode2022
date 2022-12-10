import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun List<String>.toMoveInstructions(): List<Pair<Int, Int>> {
        return this.flatMap {
            val (char, times) = it.split(" ")
            val move = when (char.first()) {
                'U' -> 0 to 1
                'R' -> 1 to 0
                'D' -> 0 to -1
                'L' -> -1 to 0
                else -> error("Can not convert ${char.first()} to move direction.")
            }
            List(times.toInt()) { move }
        }
    }

    fun Pair<Int, Int>.isTouching(otherPosition: Pair<Int, Int>): Boolean {
        return abs(this.first - otherPosition.first) <= 1 && abs(this.second - otherPosition.second) <= 1
    }

    fun getFollowMoveInstruction(headPosition: Pair<Int, Int>, tailPosition: Pair<Int, Int>): Pair<Int, Int> {
        if (headPosition.isTouching(tailPosition))
            return 0 to 0

        val distanceX = headPosition.first - tailPosition.first
        val distanceY = headPosition.second - tailPosition.second

        return distanceX.sign to distanceY.sign
    }

    operator fun Pair<Int, Int>.plus(moveInstruction: Pair<Int, Int>): Pair<Int, Int> {
        return this.first + moveInstruction.first to this.second + moveInstruction.second
    }

    fun part1(input: List<String>): Int {
        val moveInstructions = input.toMoveInstructions()

        var headPosition = 0 to 0
        var tailPosition = 0 to 0

        val positionsVisitedByTail = mutableSetOf<Pair<Int, Int>>()
        positionsVisitedByTail.add(tailPosition)

        moveInstructions.forEach {
            headPosition += it
            tailPosition += getFollowMoveInstruction(headPosition, tailPosition)
            positionsVisitedByTail.add(tailPosition)
        }

        return positionsVisitedByTail.size
    }

    fun part2(input: List<String>): Int {
        val moveInstructions = input.toMoveInstructions()

        val numberOfRopeSegments = 10
        val ropeSegmentsPositions = Array(numberOfRopeSegments) { 0 to 0 }

        val positionsVisitedByTail = mutableSetOf<Pair<Int, Int>>()
        positionsVisitedByTail.add(ropeSegmentsPositions.last())

        moveInstructions.forEach {
            for (i in ropeSegmentsPositions.indices) {
                if (i == 0)
                    ropeSegmentsPositions[0] = ropeSegmentsPositions[0] + it
                else
                    ropeSegmentsPositions[i] = ropeSegmentsPositions[i] + getFollowMoveInstruction(
                        ropeSegmentsPositions[i - 1],
                        ropeSegmentsPositions[i]
                    )
            }
            positionsVisitedByTail.add(ropeSegmentsPositions.last())
        }

        return positionsVisitedByTail.size
    }

    val testInput = readInput("Day09Test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}