package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * This test focuses on checking that only necessary parentheses are present. For example, `3 + 4` should be returned,
 * instead of `(3) + (4)` or `(3 + 4)`.
 */
@Disabled
class BeltmaticSolverParenthesesRedundancyTest {

    private val solver = BeltmaticSolverFactory.create()

    @Test
    fun `formula with 1 operation has no parentheses`() {
        val availableNumbers: List<Int> = listOf(3, 4)
        val targetNumber = 7
        val operationsLimit = 1

        FormulaVerifier.verify("3 + 4", availableNumbers, targetNumber, operationsLimit)

        val formula = solver.solve(availableNumbers, targetNumber)
        val result = ExpressionEvaluator.evaluate(formula)

        assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
        assertEquals(operationsLimit, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")

        assertTrue(
            formula.count { it == '(' } == 0 && formula.count { it == ')' } == 0,
            "Formula should not contain any parentheses: $formula"
        )
    }

    @Test
    fun `formula with 2 identical commutative operations has no parentheses`() {
        val availableNumbers: List<Int> = listOf(3, 5, 7)
        val targetNumber = 105
        val operationsLimit = 2

        FormulaVerifier.verify("3 * 5 * 7", availableNumbers, targetNumber, operationsLimit)

        val formula = solver.solve(availableNumbers, targetNumber)
        val result = ExpressionEvaluator.evaluate(formula)

        assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
        assertEquals(operationsLimit, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")

        assertTrue(
            formula.count { it == '(' } == 0 && formula.count { it == ')' } == 0,
            "Formula should not contain any parentheses: $formula"
        )
    }

}
