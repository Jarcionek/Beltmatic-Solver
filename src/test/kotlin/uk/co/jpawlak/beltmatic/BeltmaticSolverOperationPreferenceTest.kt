package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

/**
 * These tests focus on returning "simpler" (easier to build in the game) formulas,
 * when multiple formulas with equal operation count are available.
 *
 * Additions and multiplications are commutative (the order of numbers doesn't change the result),
 * which means that when placing a machine in game, we don't have to distinguish between the two machine's inputs,
 * which makes it much easier to supply numbers to it.
 *
 * The smallest operation count is still the most important factor, as we want to build in the game as few machines
 * as necessary.
 */
@Timeout(value = 30, unit = TimeUnit.SECONDS)
class BeltmaticSolverOperationPreferenceTest {

    private val solver = BeltmaticSolverFactory.create()

    @Test
    fun `3955 is solved with multiplications and additions only`() {
        val availableNumbers: List<Int> = (1..9) + (11..24)
        val targetNumber = 3955
        val operationsLimit = 3

        FormulaVerifier.verify("(24^2 - 11) * 7", availableNumbers, targetNumber, operationsLimit)
        FormulaVerifier.verify("13 * 16 * 19 + 3", availableNumbers, targetNumber, operationsLimit)

        val formula = solver.solve(availableNumbers, targetNumber)
        val result = ExpressionEvaluator.evaluate(formula)

        assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
        assertEquals(operationsLimit, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")

        assertTrue(!formula.contains('^') && !formula.contains('/') && !formula.contains('-'),
            "Formula should only contain multiplications and additions: $formula")
    }

    @Test
    fun `40137 is solved with only one exponentiation and no subtractions`() {
        val availableNumbers: List<Int> = (1..9) + (11..24)
        val targetNumber = 40137
        val operationsLimit = 4

        FormulaVerifier.verify("12^3 - (7 - 14^4)", availableNumbers, targetNumber, operationsLimit)
        FormulaVerifier.verify("(12^3 + 17) * 23 + 2", availableNumbers, targetNumber, operationsLimit)

        val formula = solver.solve(availableNumbers, targetNumber)
        val result = ExpressionEvaluator.evaluate(formula)

        assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
        assertEquals(operationsLimit, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")

        assertTrue(formula.count { it == '^' } == 1 && !formula.contains('-'),
            "Formula should contain exactly one exponentiation and no subtractions: $formula")
    }

}
