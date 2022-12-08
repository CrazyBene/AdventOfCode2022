fun main() {
    fun List<Int>.toVisibilityList(): List<Boolean> {
        return this.mapIndexed { index, numberToCheck ->
            for (i in index + 1 until this.size) {
                if (this[i] >= numberToCheck)
                    return@mapIndexed false
            }
            true
        }
    }

    infix fun List<List<Boolean>>.combine(otherGrid: List<List<Boolean>>): List<List<Boolean>> {
        return this.mapIndexed { y, line ->
            line.mapIndexed { x, boolean ->
                boolean || otherGrid[y][x]
            }
        }
    }


    fun visibleFromTopAndBottom(grid: List<List<Int>>): Pair<List<List<Boolean>>, List<List<Boolean>>> {
        val visibleFromTop = mutableListOf<MutableList<Boolean>>()
        val visibleFromBottom = mutableListOf<MutableList<Boolean>>()

        val width = grid[0].size

        for (currentX in 0 until width) {
            visibleFromTop.add(mutableListOf())
            visibleFromBottom.add(mutableListOf())
        }

        for (currentX in 0 until width) {
            val numbersToCheck = grid.flatMapIndexed { y, line ->
                line.filterIndexed { x: Int, _: Int ->
                    x == currentX
                }
            }

            numbersToCheck.reversed().toVisibilityList().reversed().forEachIndexed { index, b ->
                visibleFromTop[index].add(b)
            }
            numbersToCheck.toVisibilityList().forEachIndexed { index, b ->
                visibleFromBottom[index].add(b)
            }
        }

        return visibleFromTop to visibleFromBottom
    }

    fun visibleFromSides(grid: List<List<Int>>): Pair<List<List<Boolean>>, List<List<Boolean>>> {
        val visibleFromLeft = mutableListOf<List<Boolean>>()
        val visibleFromRight = mutableListOf<List<Boolean>>()

        val height = grid.size

        for (currentY in 0 until height) {
            val numbersToCheck = grid[currentY]

            visibleFromLeft.add(numbersToCheck.reversed().toVisibilityList().reversed())
            visibleFromRight.add(numbersToCheck.toVisibilityList())
        }

        return visibleFromLeft to visibleFromRight
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { line ->
            line.toList().map { it.digitToInt() }
        }

        val (visibleFromTop, visibleFromBottom) = visibleFromTopAndBottom(grid)
        val (visibleFromLeft, visibleFromRight) = visibleFromSides(grid)

        val visible = visibleFromTop combine visibleFromBottom combine visibleFromLeft combine visibleFromRight

        return visible.flatMap { line ->
            line.map {
                if (it)
                    1
                else
                    0
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { line ->
            line.toList().map { it.digitToInt() }
        }

        val gridResult = grid.mapIndexed { currentY, line ->
            List(line.size) { currentX ->
                val treeToCheck = grid[currentY][currentX]

                val treesToCheckDownwards = grid.flatMapIndexed { y, line ->
                    line.filterIndexed { x: Int, _: Int ->
                        x == currentX && y > currentY
                    }
                }
                var treesVisibleDownwards = 0
                for (tree in treesToCheckDownwards) {
                    treesVisibleDownwards++
                    if (tree >= treeToCheck)
                        break
                }

                val treesToCheckUpwards = grid.flatMapIndexed { y, line ->
                    line.filterIndexed { x: Int, _: Int ->
                        x == currentX && y < currentY
                    }
                }.reversed()
                var treesVisibleUpwards = 0
                for (tree in treesToCheckUpwards) {
                    treesVisibleUpwards++
                    if (tree >= treeToCheck)
                        break
                }

                val treesToCheckLeft = grid.flatMapIndexed { y, line ->
                    line.filterIndexed { x: Int, _: Int ->
                        x < currentX && y == currentY
                    }
                }.reversed()
                var treesVisibleLeft = 0
                for (tree in treesToCheckLeft) {
                    treesVisibleLeft++
                    if (tree >= treeToCheck)
                        break
                }

                val treesToCheckRight = grid.flatMapIndexed { y, line ->
                    line.filterIndexed { x: Int, _: Int ->
                        x > currentX && y == currentY
                    }
                }
                var treesVisibleRight = 0
                for (tree in treesToCheckRight) {
                    treesVisibleRight++
                    if (tree >= treeToCheck)
                        break
                }

                treesVisibleDownwards * treesVisibleUpwards * treesVisibleLeft * treesVisibleRight
            }
        }

        return gridResult.maxOf {
            it.max()
        }
    }

    val testInput = readInput("Day08Test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}