fun main() {

    fun part1(input: List<String>): Int {
        return input
            .map { assignmentPair ->
                assignmentPair
                    .split(",")
                    .map { range ->
                        range.split("-").map { it.toInt() }.zipWithNext().first()
                    }
                    .zipWithNext()
                    .first()

            }.count { pair ->
                (pair.first.first <= pair.second.first && pair.first.second >= pair.second.second) ||
                        (pair.first.first >= pair.second.first && pair.first.second <= pair.second.second)
            }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day04Test")
    check(part1(testInput) == 2)
//    check(part2(testInput) == 70)

    val input = readInput("Day04")
    println("Question 1 - Answer: ${part1(input)}")
//    println("Question 2 - Answer: ${part2(input)}")
}