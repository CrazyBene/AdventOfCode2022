fun main() {
    data class Directory(val name: String) {

        val directories = mutableMapOf<String, Directory>()
        val files = mutableMapOf<String, Long>()

        fun addDirectory(directory: Directory) {
            directories[directory.name] = directory
        }

        fun addFile(name: String, size: Long) {
            files[name] = size
        }

        fun toPrettyString(indentation: Int = 0): String {
            var string =  "- $name (dir)"
            for(directory in directories.values) {
                string += "\n"
                for(i in 0 .. indentation) string += "\t"
                string += directory.toPrettyString(indentation + 1)
            }
            for(file in files.toList()) {
                string += "\n"
                for(i in 0 .. indentation) string += "\t"
                string += "- ${file.first} (file, size=${file.second})"
            }

            return string
        }

        fun getSize(): Long {
            val sizeOfChildren = directories.values.sumOf {
                it.getSize()
            }
            val sizeOfFiles = files.values.sumOf {
                it
            }
            return sizeOfChildren + sizeOfFiles
        }

        fun getDirectoriesWithSize(): List<Pair<String, Long>> {
            val d = mutableListOf<Pair<String, Long>>()

            d.add(name to getSize())
            directories.values.forEach {
                d.addAll(it.getDirectoriesWithSize())
            }

            return d
        }

    }

    fun getCurrentDirectory(rootDirectory: Directory, currentPath: List<String>): Directory {
        var currentDirectory = rootDirectory
        for(d in currentPath) {
            currentDirectory = currentDirectory.directories[d]!!
        }
        return currentDirectory
    }

    fun List<String>.toFileSystem(): Directory {
        val currentPath = mutableListOf<String>()
        val rootDirectory = Directory("/")

        this.forEach {
            val currentDirectory = getCurrentDirectory(rootDirectory, currentPath)
            val words = it.split(" ")
            when (words[0]) {
                "$" -> {
                    if (words[1] == "cd") {
                        when (words[2]) {
                            "/" -> currentPath.clear()
                            ".." -> currentPath.removeLast()
                            else -> currentPath.add(words[2])
                        }
                    }
                }

                "dir" -> {
                    currentDirectory.addDirectory(Directory(words[1]))
                }

                else -> {
                    currentDirectory.addFile(words[1], words[0].toLong())
                }
            }
        }
        return rootDirectory
    }

    fun part1(input: List<String>) : Long {
        val rootDirectory = input.toFileSystem()

        return rootDirectory.getDirectoriesWithSize()
            .filter {
            it.second <= 100000
            }
            .sumOf { it.second }
    }

    fun part2(input: List<String>) : Long {
        val rootDirectory = input.toFileSystem()

        val freeSpace = 70000000 - rootDirectory.getSize()
        val neededSpace = 30000000 - freeSpace

        return rootDirectory.getDirectoriesWithSize()
            .filter {
                it.second >= neededSpace
            }
            .minOf { it.second }
    }

    val testInput = readInput("Day07Test")
    check(part1(testInput) == 95437.toLong())
    check(part2(testInput) == 24933642.toLong())

    val input = readInput("Day07")
    println("Question 1 - Answer: ${part1(input)}")
    println("Question 2 - Answer: ${part2(input)}")
}