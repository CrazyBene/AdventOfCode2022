import kotlin.math.abs

fun main() {
    fun List<String>.toMoveInstructions(): List<MoveInstruction> {
        return this.flatMap {
            val (char, times) = it.split(" ")
            MoveInstruction.fromChar(char.first(), times.toInt())
        }
    }

    fun Pair<Int, Int>.isTouching(otherPosition: Pair<Int, Int>): Boolean {
        return abs(this.first - otherPosition.first) <= 1 && abs(this.second - otherPosition.second) <= 1
    }

    fun getFollowMoveInstruction(headPosition: Pair<Int, Int>, tailPosition: Pair<Int, Int>): MoveInstruction {
        if (headPosition.isTouching(tailPosition))
            return MoveInstruction.NONE

        val distanceX = headPosition.first - tailPosition.first
        val distanceY = headPosition.second - tailPosition.second

        return when (distanceX to distanceY) {
            0 to 2 -> MoveInstruction.UP
            0 to -2 -> MoveInstruction.DOWN
            -2 to 0 -> MoveInstruction.LEFT
            2 to 0 -> MoveInstruction.RIGHT
            else -> {
                if (distanceX > 0 && distanceY > 0) {
                    return MoveInstruction.UP_RIGHT
                }
                if (distanceX > 0 && distanceY < 0) {
                    return MoveInstruction.DOWN_RIGHT
                }
                if (distanceX < 0 && distanceY < 0) {
                    return MoveInstruction.DOWN_LEFT
                }
                if (distanceX < 0 && distanceY > 0) {
                    return MoveInstruction.UP_LEFT
                }
                error("Tail $tailPosition can not follow head $headPosition.")
            }
        }
    }

    operator fun Pair<Int, Int>.plus(moveInstruction: MoveInstruction): Pair<Int, Int> {
        return this.first + moveInstruction.direction.first to this.second + moveInstruction.direction.second
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

        val ropeSegmentsPositions = MutableList(numberOfRopeSegments) { 0 to 0 }

        val positionsVisitedByTail = mutableSetOf<Pair<Int, Int>>()
        positionsVisitedByTail.add(ropeSegmentsPositions.last())

        moveInstructions.forEach {
            for (i in 0 until ropeSegmentsPositions.size) {
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

enum class MoveInstruction(val direction: Pair<Int, Int>) {

    UP(0 to 1),
    UP_RIGHT(1 to 1),
    RIGHT(1 to 0),
    DOWN_RIGHT(1 to -1),
    DOWN(0 to -1),
    DOWN_LEFT(-1 to -1),
    LEFT(-1 to 0),
    UP_LEFT(-1 to 1),
    NONE(0 to 0);

    companion object {

        fun fromChar(char: Char): MoveInstruction {
            return when (char) {
                'U' -> UP
                'D' -> DOWN
                'L' -> LEFT
                'R' -> RIGHT
                else -> error("Can not convert $char to MoveInstruction.")
            }
        }

        fun fromChar(char: Char, times: Int): List<MoveInstruction> {
            val moveInstruction = fromChar(char)
            return List(times) { moveInstruction }
        }

    }

}