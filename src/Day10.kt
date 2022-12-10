fun main() {
    open class Instruction(val cycles: Int)
    class NoOp : Instruction(1)
    class AddX(val amount: Int) : Instruction(2)

    fun List<String>.toInstructions(): List<Instruction> {
        return this.map {
            val i = it.split(" ")
            when (i[0]) {
                "noop" -> NoOp()
                "addx" -> AddX(i[1].toInt())
                else -> error("Instruction $it is unknown.")
            }

        }
    }

    fun part1(input: List<String>): Int {
        val instructions = input.toInstructions()

        var currentCycle = 1
        var registerX = 1

        val cycleValues = mutableListOf<Int>()

        instructions.forEach {
            for (i in 0 until it.cycles) {
                if ((currentCycle - 20) % 40 == 0)
                    cycleValues += registerX * currentCycle

                currentCycle++
            }

            if (it is AddX)
                registerX += it.amount
        }

        return cycleValues.sum()
    }

    fun part2(input: List<String>): String {
        val instructions = input.toInstructions()

        var currentCycle = 1
        var registerX = 1

        var crtOutput = ""

        instructions.forEach {
            for (i in 0 until it.cycles) {
                val crtPosition = (currentCycle - 1) % 40

                crtOutput += if (crtPosition in registerX - 1..registerX + 1) "#" else "."
                if (currentCycle % 40 == 0)
                    crtOutput += "\n"

                currentCycle++
            }

            if (it is AddX)
                registerX += it.amount
        }

        return crtOutput
    }

    val testInput = readInput("Day10Test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: \n${part2(input)}")
}