package me.gabryon

import kotlin.math.abs

object Day01 {

    /**
     * Pair up the smallest number in the left list with the smallest number in the right list,
     * then the second-smallest left number with the second-smallest right number, and so on.
     */
    infix fun <T : Comparable<T>> List<T>.sortedZip(rhs: List<T>): List<Pair<T, T>> {
        require(this.size == rhs.size) { "The input lists must have the same size." }
        return this.sorted() zip rhs.sorted()
    }

    fun totalDistance(lhs: List<Int>, rhs: List<Int>): Int =
        (lhs sortedZip rhs).sumOf { (l, r) -> abs(l - r) }

    fun similarity(lhs: List<Int>, rhs: List<Int>): Int {
        val counts: Map<Int, Int> = buildMap {
            // Start inserting keys
            for (l in lhs) put(l, 0)
            // Now, for each occurrence of the key, increase the value by one
            for (r in rhs) {
                // If the value is a valid key on the map, increase the occurrence
                get(r)?.let { put(r, it + 1) }
            }
        }
        return counts.entries.sumOf { it -> it.key * it.value }
    }
}