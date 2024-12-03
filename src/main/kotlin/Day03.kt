package me.gabryon

// Corrupted memory:
// xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
// (2*4 + 5*5 + 11*8 + 8*5)=161 (summing the valid mul instructions)

// Valid instruction: 'mul(n,m)' : n, m \in [0, 999]
// instructions ::= list[instruction]
// instruction ::= mul(num,num) | do() | don't()
// num ::= n \in [0, 999]

object Day03 {

    sealed interface Instruction

    // Multiply two numbers
    data class Mul(val x: Long, val y: Long) : Instruction {
        companion object {
            const val MNEMONIC: String = "mul"
        }
    }

    // The do() instruction enables future mul instructions.
    data object Do : Instruction {
        const val MNEMONIC: String = "do"
    }

    // The don't() instruction disables future mul instructions.
    data object Dont : Instruction {
        const val MNEMONIC: String = "don't"
    }

    fun List<Instruction>.eval(): Long {
        var sum = 0L
        var disabled = false
        for (instruction in this) {
            when (instruction) {
                Dont -> disabled = true
                Do -> disabled = false
                is Mul -> {
                    if (disabled) continue
                    sum += (instruction.x * instruction.y)
                }
            }
        }
        return sum
    }

    data class ParserState(val cursor: Int)

    fun ParserState.succ(): ParserState = ParserState(this.cursor + 1)
    fun Int.toParserState(): ParserState = ParserState(this)

    fun scanMemory(memory: String): Long {

        fun number(state: ParserState): Pair<Long?, ParserState> {
            var newState = state
            val numAsString = buildString {
                while (newState.cursor < memory.length && length < 3) {
                    val current = memory[newState.cursor]
                    if (!current.isDigit()) break
                    append(current)
                    newState = ParserState(newState.cursor + 1)
                }
            }
            val num = numAsString.toLong()
            // Is the number a 3-digit valid number?
            if (num !in (0..999))
                return (null to ParserState(newState.cursor + 1))

            return (num to newState)
        }

        fun accept(state: ParserState, lexeme: Char): Boolean {
            return (memory[state.cursor] == lexeme)
        }

        fun mulInstruction(state: ParserState): Pair<Instruction?, ParserState> {
            val startRange = state.cursor
            val endRange = state.cursor + Mul.MNEMONIC.length
            if (endRange > memory.length)
                return (null to ParserState(memory.length + 1))

            if (memory.slice(startRange until endRange) != Mul.MNEMONIC)
                return (null to state.succ())
            if (!accept(endRange.toParserState(), '('))
                return (null to endRange.toParserState())

            // [endRange + 2] -- includes '(' and the first digit
            var (num1, newState) = number(ParserState(endRange + 1))

            if (!accept(newState, ','))
                return (null to newState.succ())

            var temp = number(newState.succ())
            val num2 = temp.first
            newState = temp.second

            if (!accept(newState, ')'))
                return (null to newState.succ())

            // [newState.cursor + 2] -- start from the next char after the ')'
            return (Mul(num1!!, num2!!) to newState.succ())
        }

        fun doInstruction(state: ParserState): Pair<Instruction?, ParserState> {
            val startRange = state.cursor
            val endRange = state.cursor + Do.MNEMONIC.length
            if (endRange > memory.length)
                return (null to ParserState(memory.length + 1))

            if (memory.slice(startRange until endRange) != Do.MNEMONIC)
                return (null to state.succ())

            if (!accept(endRange.toParserState(), '('))
                return (null to endRange.toParserState())
            if (!accept((endRange + 1).toParserState(), ')'))
                return (null to (endRange + 1).toParserState())

            return (Do to (endRange + 2).toParserState())
        }

        fun dontInstruction(state: ParserState): Pair<Instruction?, ParserState> {
            val startRange = state.cursor
            val endRange = state.cursor + Dont.MNEMONIC.length
            if (endRange > memory.length)
                return (null to ParserState(memory.length + 1))

            if (memory.slice(startRange until endRange) != Dont.MNEMONIC)
                return (null to state.succ())

            if (!accept(endRange.toParserState(), '('))
                return (null to endRange.toParserState())
            if (!accept((endRange + 1).toParserState(), ')'))
                return (null to (endRange + 1).toParserState())

            return (Dont to (endRange + 2).toParserState())
        }

        fun parse(state: ParserState): Pair<Instruction?, ParserState> {
            mulInstruction(state).also {
                if (it.first != null) return it
            }
            doInstruction(state).also {
                if (it.first != null) return it
            }
            dontInstruction(state).also {
                if (it.first != null) return it
            }
            return (null to state.succ())
        }

        var state = ParserState(cursor = 0)
        val instructions: List<Instruction> = buildList {
            while (state.cursor < memory.length) {
                val (instruction, newState) = parse(state)
                instruction?.let { add(it) }
                state = newState
            }
        }

        return instructions.eval()
    }

}