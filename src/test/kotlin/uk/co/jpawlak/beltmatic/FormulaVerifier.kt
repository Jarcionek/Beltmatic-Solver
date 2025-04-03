package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals

object FormulaVerifier {

    /**
     * Verifies that [formula] contains only [availableNumbers], uses only [expectedOperations operations],
     * and evaluates to [targetNumber].
     */
    fun verify(formula: String, availableNumbers: List<Int>, targetNumber: Int, expectedOperations: Int) {
        val illegalNumbers = Regex("\\d+")
            .findAll(formula)
            .map { it.value.toInt() }
            .filter { it !in availableNumbers }
            .toList()
        if (illegalNumbers.isNotEmpty()) {
            throw AssertionError("""Example formula "$formula" contains numbers $illegalNumbers which are not in available numbers $availableNumbers""")
        }

        val result = ExpressionEvaluator.evaluate(formula)

        assertEquals(targetNumber, result, "Example formula does not evaluate to $targetNumber")
        assertEquals(expectedOperations, operationCount(formula), "Example formula contains more operations than expected")
    }

    fun operationCount(formula: String) = formula.count { it in listOf('+', '-', '*', '/', '^',) }

}
