package uk.co.jpawlak.beltmatic

enum class Operation(
    /**
     * Defines in which order the operations are executed in an expression without parentheses.
     * Higher value means that it's first.
     */
    val precedence: Int,
    /**
     * Beltmatic's Adder and Multiplier machines are easier to place, as they are symmetrical.
     * If multiple solutions are available, the algorithm should prefer operations with the higher preference.
     */
    val preference: Int,
) {
    EXPONENTIATION(3, 1),
    MULTIPLICATION(2, 2),
//    DIVISION(2, 1), // TODO implement division (will need to increment the max number limit)
    ADDITION(1, 2),
    SUBTRACTION(1, 1)
}
