fun main() {
    data class Node(val xPos: Int, val yPos: Int, val height: Char, val isStart: Boolean, val isEnd: Boolean) {

        val neighbours = mutableListOf<Node>()

    }

    fun Char.isNextTo(otherChar: Char) = otherChar.code - code <= 1

    fun List<String>.toHill(): List<List<Node>> {
        val hill = this.mapIndexed { yPos, line ->
            line.mapIndexed { xPos, char ->
                val height = when (char) {
                    'S' -> 'a'
                    'E' -> 'z'
                    else -> char
                }
                Node(xPos, yPos, height, char == 'S', char == 'E')
            }
        }

        val height = hill.size
        val width = hill[0].size

        for (y in 0 until height) {
            for (x in 0 until width) {
                val currentNode = hill[y][x]

                val potentialNeighbours = mutableListOf<Node>()

                if (x > 0)
                    potentialNeighbours.add(hill[y][x - 1])
                if (x < width - 1)
                    potentialNeighbours.add(hill[y][x + 1])
                if (y > 0)
                    potentialNeighbours.add(hill[y - 1][x])
                if (y < height - 1)
                    potentialNeighbours.add(hill[y + 1][x])

                currentNode.neighbours.addAll(
                    potentialNeighbours.filter {
                        currentNode.height.isNextTo(it.height)
                    }
                )
            }
        }
        return hill
    }

    fun solveWithDijkstra(nodes: List<Node>, startNode: Node): Pair<Map<Node, Int>, Map<Node, Node?>> {
        val distances = nodes.associateWith { Int.MAX_VALUE }.toMutableMap()
        val previous: MutableMap<Node, Node?> = nodes.associateWith { null }.toMutableMap()

        val notYetVisited = mutableListOf<Node>()
        notYetVisited.addAll(nodes)

        distances[startNode] = 0

        while (notYetVisited.isNotEmpty()) {
            val currentNode = notYetVisited.sortedBy { distances[it] }.first()
            notYetVisited.remove(currentNode)

            if(currentNode.isEnd)
                return distances to previous

            for (neighbor in currentNode.neighbours) {
                if(neighbor !in notYetVisited)
                    continue

                val newDistance = distances[currentNode]!!.plus(1)

                if(newDistance < distances[neighbor]!!) {
                    distances[neighbor] = newDistance
                    previous[neighbor] = currentNode
                }

            }
        }

        return distances to previous
    }

    fun part1(input: List<String>): Int {
        val nodes = input.toHill().flatten()
        val startNode = nodes.first {
            it.isStart
        }
        val endNode = nodes.first {
            it.isEnd
        }

        val (_, previous) = solveWithDijkstra(nodes, startNode)

        var steps = -1
        var checkNode: Node? = endNode
        while(checkNode != null) {
            steps++
            checkNode = previous[checkNode]
        }

        return steps
    }

    fun part2(input: List<String>): Int {
        val nodes = input.toHill().flatten()
        val possibleStartNodes = nodes.filter {
            it.height == 'a'
        }
        val endNode = nodes.first {
            it.isEnd
        }

        return possibleStartNodes.map {
            val (_, previous) = solveWithDijkstra(nodes, it)

            var steps = -1
            var checkNode: Node? = endNode
            while(checkNode != null) {
                steps++
                checkNode = previous[checkNode]
            }

            steps
        }.min()
    }

    val testInput = readInput("Day12Test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}