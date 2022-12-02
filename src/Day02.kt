fun main() {
    fun List<String>.toGameRounds(myHandShapeMapping: (HandShape, String) -> HandShape): List<GameRound> = this.map { line ->
        val shapeInputs = line.split(" ")
        if (shapeInputs.size != 2) error("Each round/line needs exactly 2 hand shapes")

        val opponentHandShape = HandShape.fromValue(shapeInputs[0])
        val myHandShape = myHandShapeMapping(opponentHandShape, shapeInputs[1])
        GameRound(opponentHandShape, myHandShape)
    }

    fun part1(input: List<String>): Int {
        val gameRounds = input.toGameRounds { _, myShapeInput ->
            HandShape.fromValue(
                when(myShapeInput) {
                    "X" -> "A"
                    "Y" -> "B"
                    "Z" -> "C"
                    else -> error("The second move must be XYZ.")
                }
            )
        }

        return gameRounds.sumOf { it.evaluate() }
    }

    fun part2(input: List<String>): Int {
        val gameRounds = input.toGameRounds { opponentHandShape, myShapeInput ->
            when (myShapeInput) {
                "X" -> HandShape.getLosingShape(opponentHandShape)
                "Y" -> opponentHandShape
                "Z" -> HandShape.getWinningShape(opponentHandShape)
                else -> error("The second move must be XYZ.")
            }
        }

        return gameRounds.sumOf { it.evaluate() }
    }

    val testInput = readInput("Day02Test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}

data class GameRound(val opponentHandShape: HandShape, val myHandShape: HandShape) {

    fun evaluate(): Int {
        val pointsFromResult = when (Pair(opponentHandShape, myHandShape)) {
            Pair(HandShape.SCISSORS, HandShape.PAPER),
            Pair(HandShape.ROCK, HandShape.SCISSORS),
            Pair(HandShape.PAPER, HandShape.ROCK) -> 0

            Pair(HandShape.ROCK, HandShape.ROCK),
            Pair(HandShape.PAPER, HandShape.PAPER),
            Pair(HandShape.SCISSORS, HandShape.SCISSORS) -> 3

            Pair(HandShape.ROCK, HandShape.PAPER),
            Pair(HandShape.PAPER, HandShape.SCISSORS),
            Pair(HandShape.SCISSORS, HandShape.ROCK) -> 6

            else -> error("The combination of $opponentHandShape and $myHandShape is not possible.")
        }

        return pointsFromResult + myHandShape.points
    }

}

enum class HandShape(val points: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    companion object {
        fun fromValue(value: String): HandShape {
            return when (value) {
                "A" -> ROCK
                "B" -> PAPER
                "C" -> SCISSORS
                else -> error("A hand shape which is not ABC was selected.")
            }
        }

        fun getWinningShape(otherShape: HandShape): HandShape {
            return when (otherShape) {
                ROCK -> PAPER
                PAPER -> SCISSORS
                SCISSORS -> ROCK
            }
        }

        fun getLosingShape(otherShape: HandShape): HandShape {
            return when (otherShape) {
                ROCK -> SCISSORS
                PAPER -> ROCK
                SCISSORS -> PAPER
            }
        }

    }

}