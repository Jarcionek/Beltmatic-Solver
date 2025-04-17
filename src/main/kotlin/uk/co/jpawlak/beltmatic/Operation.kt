package uk.co.jpawlak.beltmatic

enum class Operation(
    val symbol: Char,
    /**
     * Defines in which order the operations are executed in an expression without parentheses.
     * Higher value means that it's first.
     */
    val precedence: Int,
    /**
     * Beltmatic's Adder and Multiplier machines are easier to place, as they are symmetrical.
     * Divider machine is the worst, as it requires extra machines to destroy the remainder.
     * If multiple solutions are available, the algorithm should prefer operations with the higher preference.
     */
    val preference: Int,
) {
    EXPONENTIATION('^', 3, 2),
    MULTIPLICATION('*', 2, 3),
    DIVISION('/', 2, 1),
    ADDITION('+', 1, 3),
    SUBTRACTION('-', 1, 2);

    companion object {

        fun fromSymbol(symbol: Char): Operation {
            return entries.find { it.symbol == symbol } ?: throw IllegalArgumentException("Unknown operation: $symbol")
        }

    }

}
