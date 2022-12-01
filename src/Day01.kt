fun main() {
    fun part1(input: List<String>): Int {
        val elves = inputToElves(input)
        return getElvesWithMostCalories(elves).sumOf { elf -> elf.sum() }
    }

    fun part2(input: List<String>): Int {
        val elves = inputToElves(input)
        return getElvesWithMostCalories(elves, 3).sumOf { elf -> elf.sum() }
    }

    val testInput = readInput("Day01Test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}

fun getElvesWithMostCalories(elves: List<List<Int>>, n: Int = 1): List<List<Int>> {
    if (n <= 0) error("The count of elves with most calories must be greater than 0")

    val elvesToCheck = elves.toMutableList()

    val elvesWithMostCalories = mutableListOf<List<Int>>()
    for (i in 0 until n) {
        val index = elvesToCheck.indices.maxByOrNull { elvesToCheck.map { elf -> elf.sum() }[it] } ?: break
        elvesWithMostCalories.add(elvesToCheck[index])
        elvesToCheck.removeAt(index)
    }
    return elvesWithMostCalories
}

fun inputToElves(input: List<String>): MutableList<List<Int>> {
    val elves = mutableListOf<List<Int>>()
    var restInputLines = input.toMutableList()
    while (restInputLines.isNotEmpty()) {
        elves.add(restInputLines.takeWhile { it.isNotEmpty() }.map { it.toInt() })
        restInputLines = restInputLines.dropWhile { it.isNotEmpty() }.drop(1).toMutableList()
    }
    return elves
}
