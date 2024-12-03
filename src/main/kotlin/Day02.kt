package me.gabryon

import kotlin.math.abs

object Day02 {
    @JvmInline
    value class Report(val data: IntArray) {
        override fun toString(): String = data.contentToString()
    }

    fun String.toReport(): Report = Report(this.split(' ').map(String::toInt).toIntArray())

    val IntArray.isIncreasing: Boolean
        get() = asSequence().zipWithNext().all { pair -> pair.first < pair.second }
    val IntArray.isDecreasing: Boolean
        get() = asSequence().zipWithNext().all { pair -> pair.first > pair.second }

    val Report.isSafe: Boolean
        get() {
            // The levels are either all increasing or all decreasing.
            // Any two adjacent levels differ by at least one and at most three...
            if (!data.isIncreasing && !data.isDecreasing) return false
            return data.asSequence().zipWithNext().all { (a, b) -> abs(a - b) in 1..3 }
        }

    fun Report.tryToDamp(): Report? {
        if (isSafe) return this
        for (index in data.indices) {
            val newData = data.toMutableList().apply { removeAt(index) }
            val newReport = Report(newData.toIntArray())
            if (newReport.isSafe) return newReport
        }
        return null
    }
}