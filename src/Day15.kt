import kotlin.math.abs

fun main() {
    data class Position(val xPos: Int, val yPos: Int) {

        fun distanceTo(otherPosition: Position): Int = abs(xPos - otherPosition.xPos) + abs(yPos - otherPosition.yPos)

    }

    data class Sensor(val position: Position, val beaconPosition: Position)

    fun part1(input: List<String>, yToCheck: Int): Int {
        val sensors = input.map {
            val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()

            val result = regex.find(it)
            result?.groupValues?.get(0) ?: error("Can not map line $$it to Sensor.")

            val sensorXPos = result.groupValues[1].toInt()
            val sensorYPos = result.groupValues[2].toInt()
            val beaconXPos = result.groupValues[3].toInt()
            val beaconYPos = result.groupValues[4].toInt()

            Sensor(Position(sensorXPos, sensorYPos), Position(beaconXPos, beaconYPos))
        }

        val obstacles = sensors.flatMap {
            listOf(it.position, it.beaconPosition)
        }

        val xStart = sensors.minOf {
            it.position.xPos - it.position.distanceTo(it.beaconPosition)
        } - 2
        val xEnd = sensors.maxOf {
            it.position.xPos + it.position.distanceTo(it.beaconPosition)
        } + 2

        val positions = mutableListOf<Position>()
        for (x in xStart..xEnd) {
            val positionToCheck = Position(x, yToCheck)

            var closer = false
            for (sensor in sensors) {
                val beaconDistance = sensor.position.distanceTo(sensor.beaconPosition)
                val distanceToSensor = positionToCheck.distanceTo(sensor.position)

                if (distanceToSensor <= beaconDistance) {
                    closer = true
                    break
                }
            }

            if (closer) {
                positions.add(positionToCheck)
            }
        }

        return positions.filterNot {
            it in obstacles
        }.count()
    }

    fun part2(input: List<String>, maxCheckSize: Int): Long {
        val sensors = input.map {
            val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()

            val result = regex.find(it)
            result?.groupValues?.get(0) ?: error("Can not map line $$it to Sensor.")

            val sensorXPos = result.groupValues[1].toInt()
            val sensorYPos = result.groupValues[2].toInt()
            val beaconXPos = result.groupValues[3].toInt()
            val beaconYPos = result.groupValues[4].toInt()

            Sensor(Position(sensorXPos, sensorYPos), Position(beaconXPos, beaconYPos))
        }

        (0..maxCheckSize).forEach { y ->
            val ranges = sensors.mapNotNull { sensor ->
                val beaconDistance = sensor.position.distanceTo(sensor.beaconPosition)
                val begin = sensor.position.xPos + (beaconDistance - abs(y - sensor.position.yPos)).unaryMinus()
                val end = sensor.position.xPos + beaconDistance - abs(y - sensor.position.yPos)
                (begin..end).takeUnless { it.isEmpty() }
            }.sortedBy { it.first() }

            ranges.reduce { acc, range -> (acc union range) ?: return (acc.last + 1) * 4_000_000L + y }
        }

        error("Error")
    }

    val testInput = readInput("Day15Test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println("Question 1 - Answer: ${part1(input, 2000000)}")
    println("Question 2 - Answer: ${part2(input, 4_000_000)}")
}

private infix fun IntRange.union(other: IntRange): IntRange? {
    return when (this.first <= other.last && this.last >= other.first) {
        true -> IntRange(minOf(first, other.first), maxOf(last, other.last))
        false -> null
    }
}