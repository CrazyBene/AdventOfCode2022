import java.util.*

fun main() {
    data class Instruction(val from: Int, val to: Int, val repeat: Int = 1)

    abstract class Crane {
        abstract fun performInstruction(state: List<Stack<Char>>, instruction: Instruction)
    }

    class CrateMover9000 : Crane() {
        override fun performInstruction(state: List<Stack<Char>>, instruction: Instruction) {
            for (i in 0 until instruction.repeat) {
                val crate = state[instruction.from].pop()
                state[instruction.to].push(crate)
            }
        }
    }

    class CrateMover9001 : Crane() {
        override fun performInstruction(state: List<Stack<Char>>, instruction: Instruction) {
            val poppedCrates = mutableListOf<Char>()
            for (i in 0 until instruction.repeat) {
                poppedCrates += state[instruction.from].pop()
            }
            for (crate in poppedCrates.reversed()) {
                state[instruction.to].push(crate)
            }
        }
    }

    class Cargo(initialState: String) {

        val state: List<Stack<Char>>

        init {
            state = mutableListOf()

            val numberOfStacks = initialState.last().toString().toInt()
            for (i in 0 until numberOfStacks) {
                val stack = Stack<Char>()
                initialState
                    .split("\r\n")
                    .reversed()
                    .drop(1)
                    .map {
                        try {
                            it[(i + 1) + i * 3]
                        } catch (e: IndexOutOfBoundsException) {
                            ' '
                        }

                    }
                    .filter { it != ' ' }
                    .forEach {
                        stack.push(it)
                    }
                state += stack
            }
        }

        fun performInstruction(crane: Crane, instruction: Instruction) {
            crane.performInstruction(state, instruction)
        }

        fun getTopOfStacks(): String {
            return state.map {
                it.peek()
            }.joinToString("")
        }

    }

    fun String.toInstruction(): Instruction {
        val regex = "^move (\\d+) from (\\d+) to (\\d+)$".toRegex()

        val result = regex.find(this)
        result?.groupValues?.get(0) ?: error("Can not map line $$this to instruction.")

        val repeat = result.groupValues[1].toInt()
        val from = result.groupValues[2].toInt() - 1
        val to = result.groupValues[3].toInt() - 1

        return Instruction(from, to, repeat)
    }

    fun String.toStateAndInstructions(): Pair<Cargo, List<Instruction>> {
        val (stateInput, instructionsInput) = this
            .split("\r\n\r\n") // windows needs \r\n splitting, instead of just \n
            .also { if (it.size != 2) error("Can not split into state and instructions.") }

        val state = Cargo(stateInput)

        val instructions = instructionsInput
            .split("\n")
            .map { it.toInstruction() }

        return state to instructions
    }

    fun part1(input: String): String {
        val (state, instructions) = input.toStateAndInstructions()

        val crane = CrateMover9000()

        instructions.forEach {
            state.performInstruction(crane, it)
        }

        return state.getTopOfStacks()
    }

    fun part2(input: String): String {
        val (state, instructions) = input.toStateAndInstructions()

        val crane = CrateMover9001()

        instructions.forEach {
            state.performInstruction(crane, it)
        }

        return state.getTopOfStacks()
    }

    val testInput = readInputAsText("Day05Test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInputAsText("Day05")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}