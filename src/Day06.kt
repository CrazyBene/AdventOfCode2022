fun main() {
    fun String.findFirstOccurrenceOfUniqueChars(amount: Int): Int {
        this
            .windowed(amount)
            .forEachIndexed { index, chars ->
                if(chars.toSet().size == amount)
                    return index + amount
            }
        error("No start of packet marker in $this found.")
    }

    fun part1(input: String) =
        input.findFirstOccurrenceOfUniqueChars(4)

    fun part2(input: String) =
        input.findFirstOccurrenceOfUniqueChars(14)

    val testInput = readInputAsText("Day06Test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInputAsText("Day06")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}