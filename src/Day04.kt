fun main() {
    data class SectionAssignment(val start: Int, val end: Int)

    fun String.toSectionAssignment(): SectionAssignment {
        return split("-")
            .map { it.toInt() }
            .zipWithNext()
            .map { SectionAssignment(it.first, it.second) }
            .also { if(it.size != 1) error("Could not convert $this to SectionAssignment.") }
            .first()
    }

    fun String.toSectionAssignmentPair(): Pair<SectionAssignment, SectionAssignment> {
        return split(",")
        .map { it.toSectionAssignment()}
        .zipWithNext()
        .also { if(it.size != 1) error("Could not convert $this to SectionAssignmentPair.") }
        .first()
    }

    fun part1(input: List<String>): Int {
        return input
            .map { it.toSectionAssignmentPair() }
            .count { pair ->
                (pair.first.start <= pair.second.start && pair.first.end >= pair.second.end) ||
                        (pair.first.start >= pair.second.start && pair.first.end <= pair.second.end)
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { it.toSectionAssignmentPair() }
            .count { pair ->
                (pair.first.start <= pair.second.start && pair.first.end >= pair.second.start) ||
                        (pair.first.start >= pair.second.start && pair.first.start <= pair.second.end)
            }
    }

    val testInput = readInput("Day04Test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}