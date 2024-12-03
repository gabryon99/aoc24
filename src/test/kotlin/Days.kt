import Utility.readFileContent
import Utility.readFileLines
import me.gabryon.Day01
import me.gabryon.Day02
import me.gabryon.Day03
import java.io.File
import kotlin.test.Test

object Utility {
    private const val INPUT_FILE_PATH = "inputs"

    fun readFileContent(filePath: String): String = File("$INPUT_FILE_PATH/$filePath").readText(Charsets.UTF_8)
    fun readFileLines(filePath: String): List<String> = readFileContent(filePath).lines()
}

fun <A, B> List<A>.toPair(transformer: (A) -> B) : Pair<B, B> {
    require(size == 2) { "Cannot convert a list of $size to a pair. List: $this" }
    return Pair(transformer(this[0]), transformer(this[1]))
}

class Days {
    @Test
    fun `Day 1 - Part 1`() {
        val input = readFileLines("day01.txt")
        // Prepare the input for the final function's execution...
        val (left, right)  = input.map { it.split("\\s+".toRegex()).toPair(String::toInt) }.unzip()
        assert(Day01.totalDistance(left, right) == 1320851)
    }

    @Test
    fun `Day 1 - Part 2`() {
        val input = readFileLines("day01.txt")
        // Prepare the input for the final function's execution...
        val (left, right)  = input.map { it.split("\\s+".toRegex()).toPair(String::toInt) }.unzip()
        println(Day01.similarity(left, right))
    }

    @Test
    fun `Day 2 - Part 1`() {
        val input = readFileLines("day02.txt")
        with (Day02) {
            val reports = input.map {it.toReport() }
            val safeReports = reports.count { it.isSafe }
            assert(safeReports == 624)
        }
    }

    @Test
    fun `Day 2 - Part 2`() {
        val input = readFileLines("day02.txt")
        with (Day02) {
            val reports = input.map {it.toReport() }
            // Dampened reports are safe by their definition...
            val dampened = reports.mapNotNull { it.tryToDamp() }
            assert(dampened.size == 658)
        }
    }

    @Test
    fun `Day 3 - Part 1`() {
        val input = readFileContent("day03.txt")
        assert(Day03.scanMemory(input, false) == 185797128L)
    }

    @Test
    fun `Day 3 - Part 2`() {
        val input = readFileContent("day03.txt")
        assert(Day03.scanMemory(input, true) == 89798695L)
    }
}