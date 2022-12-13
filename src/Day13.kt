import kotlin.math.sign

fun main() {
    open class Value
    data class IntValue(val value: Int) : Value()
    data class ListValue(val values: List<Value>) : Value() {

        fun isSmaller(other: ListValue): Int {
            val toCheck = values.size.coerceAtLeast(other.values.size)

            for (i in 0 until toCheck) {
                val left = values.getOrNull(i)
                val right = other.values.getOrNull(i)

                if (left == null && right != null)
                    return -1

                if (left != null && right == null)
                    return 1

                if (left is ListValue && right is ListValue) {
                    val result = left.isSmaller(right)
                    if (result != 0)
                        return result.sign
                }

                if (left is IntValue && right is IntValue) {
                    val result = left.value - right.value
                    if (result != 0)
                        return result.sign
                }

                if (left is IntValue && right is ListValue) {
                    val newLeft = ListValue(listOf(left))
                    val result = newLeft.isSmaller(right)
                    if (result != 0)
                        return result.sign
                }

                if (left is ListValue && right is IntValue) {
                    val newRight = ListValue(listOf(right))
                    val result = left.isSmaller(newRight)
                    if (result != 0)
                        return result.sign
                }
            }

            return 0
        }

    }

    fun String.toValue(): ListValue {
        val values = mutableListOf<Value>()
        val chars = toCharArray()

        var openLists = 0
        var subString = ""

        var digits = ""
        for (char in chars) {

            if (char == '[') {
                openLists++
            }

            if (openLists > 0) {
                subString += char

                if (char == ']') {
                    openLists--
                }

                if (openLists == 0) {
                    values.add(subString.substring(1 until subString.length - 1).toValue())
                    subString = ""
                }

                continue
            }

            if (char.isDigit()) {
                digits += char
            } else if (digits.isNotEmpty()) {
                values.add(IntValue(digits.toInt()))
                digits = ""
            }
        }

        if (digits.isNotEmpty()) {
            values.add(IntValue(digits.toInt()))
        }

        return ListValue(values)
    }

    fun part1(input: List<String>): Int {
        val p = input
            .zipWithNext()
            .filterIndexed { index, _ ->
                index % 3 == 0
            }
            .map { (one, two) ->
                one.substring(1 until one.length - 1).toValue() to two.substring(1 until two.length - 1).toValue()
            }

        return p
            .mapIndexed { index, (one, two) ->
                if (one.isSmaller(two) < 0)
                    index + 1
                else 0
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val p = input
            .filter { it.isNotBlank() }
            .map {
                it.substring(1 until it.length - 1).toValue()
            }.toMutableList()

        val begin = ListValue(listOf(ListValue(listOf(IntValue(2)))))
        val end = ListValue(listOf(ListValue(listOf(IntValue(6)))))
        p.add(begin)
        p.add(end)

        val sorted = p.sortedWith { o1, o2 -> o1.isSmaller(o2) }

        return sorted.mapIndexed { index, listValue ->
            if (listValue != begin && listValue != end)
                0
            else
                index + 1
        }
            .filter { it != 0 }
            .fold(1) { acc, i ->
                acc * i
            }
    }

    val testInput = readInput("Day13Test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}