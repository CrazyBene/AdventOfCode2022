import java.lang.Exception

fun main() {
    fun playRound(monkeys: List<Monkey>) {
        for (monkey in monkeys) {
            while (monkey.items.isNotEmpty()) {
                var item = monkey.getFirstItem()
                item = monkey.calculateNewValue(item)
                item = item.div(3.toLong())
                val newMonkey = if (item % monkey.divisibleValue.toLong() == 0L) monkey.testTrue else monkey.testFalse

                monkeys[newMonkey].addItem(item)
            }
        }
    }

    fun playRound2(monkeys: List<Monkey>, divider: Int) {
        for (monkey in monkeys) {
            while (monkey.items.isNotEmpty()) {
                var item = monkey.getFirstItem()
                item = monkey.calculateNewValue(item)
                item %= divider
                val newMonkey = if (item % monkey.divisibleValue.toLong() == 0L) monkey.testTrue else monkey.testFalse

                monkeys[newMonkey].addItem(item)
            }
        }
    }

    fun List<String>.toMonkeys(): List<Monkey> {
        return this
            .filterNot {
                it.isBlank()
            }
            .chunked(6)
            .map { line ->
                val startingItems = line[1].split(":")[1].split(",").map { it.trim().toLong() }
                val operationLine = line[2].split(" ")
                val operation = operationLine[operationLine.size - 2]
                val operationValue = try {
                    operationLine[operationLine.size - 1].toLong()
                } catch (e: Exception) {
                    (-1).toLong()
                }
                val divisibleValue = line[3].split(" ").last().toInt()
                val testTrue = line[4].split(" ").last().toInt()
                val testFalse = line[5].split(" ").last().toInt()

                Monkey(startingItems, operation, operationValue, divisibleValue, testTrue, testFalse)
            }
    }

    fun part1(input: List<String>): Int {
        val monkeys = input.toMonkeys()

        repeat(20) { playRound(monkeys) }

        return monkeys.map { it.inspectedItems }.sortedDescending().take(2)
            .fold(1) { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.toMonkeys()

        val divider = monkeys.map { it.divisibleValue }.fold(1) { acc, i -> acc * i }

        repeat(10000) {
            playRound2(monkeys, divider)
        }

        return monkeys.map { it.inspectedItems }.sortedDescending().take(2)
            .fold(1) { acc, i -> acc * i }
    }

    val testInput = readInput("Day11Test")
    check(part1(testInput) == 10605)
    check(part2(testInput) == 2_713_310_158)

    val input = readInput("Day11")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}

class Monkey(
    items: List<Long>,
    operation: String,
    operationValue: Long,
    divisibleValue: Int,
    testTrue: Int,
    testFalse: Int
) {

    val items: ArrayDeque<Long>

    private val operation: String
    private val operationValue: Long

    val divisibleValue: Int
    val testTrue: Int
    val testFalse: Int

    var inspectedItems = 0

    init {
        this.items = ArrayDeque(items)
        this.operation = operation
        this.operationValue = operationValue
        this.divisibleValue = divisibleValue
        this.testTrue = testTrue
        this.testFalse = testFalse
    }

    fun addItem(item: Long) {
        items.add(item)
    }

    fun getFirstItem(): Long {
        return items.removeFirst()
    }

    fun calculateNewValue(item: Long): Long {
        inspectedItems++
        val ov = if (operationValue < 0.toLong()) item else operationValue
        return if (operation == "+") item + ov else item * ov
    }

    override fun toString(): String {
        return "Monkey(items=$items, inspectedItems=$inspectedItems)"
    }

}