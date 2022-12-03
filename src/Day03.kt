fun main() {
    fun <T> getItemsInBothLists(list1: List<T>, list2: List<T>) = list1.filter { list2.contains(it) }.distinct()

    data class Item(val character: Char) {
        fun getPriorityValue(): Int {
            return if (character.isLowerCase())
                character - 'a' + 1
            else if (character.isUpperCase())
                character - 'A' + 27
            else
                error("Item $this seems to be neither lower nor upper case.")
        }
    }

    data class Rucksack(val compartment1: List<Item>, val compartment2: List<Item>) {

        fun getAllItems() = compartment1 + compartment2
        fun getDuplicateItem(): Item {
            val itemsInBothLists = getItemsInBothLists(compartment1, compartment2)
            if (itemsInBothLists.size == 1)
                return itemsInBothLists.first()

            error("There are ${itemsInBothLists.size} items which appear in both compartments of $this.")
        }
    }

    data class ElvesGroup(val rucksacks: List<Rucksack>) {

        fun getItemInAllRucksacks(): List<Item> {
            if (rucksacks.isEmpty()) return emptyList()
            if (rucksacks.size == 1) return rucksacks.first().getAllItems()

            var itemsToCompare = rucksacks.first().getAllItems()
            for (i in 1 until rucksacks.size)
                itemsToCompare = getItemsInBothLists(itemsToCompare, rucksacks[i].getAllItems())

            return itemsToCompare
        }

    }

    fun String.splitInHalf(): Pair<String, String> {
        if (length % 2 != 0) error("String '$this' needs an event amount of characters to be split in half.")
        val middle = length / 2
        return substring(0, middle) to substring(middle, length)
    }

    fun String.toItemList(): List<Item> {
        return toList().map { Item(it) }
    }

    fun part1(input: List<String>): Int {
        val rucksacks = input.map {
            val (compartment1, compartment2) = it.splitInHalf()
            Rucksack(compartment1.toItemList(), compartment2.toItemList())
        }

        return rucksacks.sumOf {
            it.getDuplicateItem().getPriorityValue()
        }
    }

    fun part2(input: List<String>): Int {
        val elvesGroups = input.map {
            val (compartment1, compartment2) = it.splitInHalf()
            Rucksack(compartment1.toItemList(), compartment2.toItemList())
        }.chunked(3).map { ElvesGroup(it) }

        return elvesGroups.sumOf {
            val itemsInAllRucksacks = it.getItemInAllRucksacks()
            if (itemsInAllRucksacks.size != 1)
                error("There seems to not exactly one item which is present in $it.")

            itemsInAllRucksacks.first().getPriorityValue()
        }
    }

    val testInput = readInput("Day03Test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}