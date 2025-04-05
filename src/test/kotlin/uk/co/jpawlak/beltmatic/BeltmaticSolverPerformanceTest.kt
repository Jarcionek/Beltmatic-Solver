package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

class BeltmaticSolverPerformanceTest {

    private val solver = BeltmaticSolver()

    /**
     * Best result: 30 sec
     */
    @Test
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    fun `with initial numbers up to 24, finds the solution to 8339 within 2 minutes`() {
        val availableNumbers: List<Int> = (1..9) + (11..24)
        val targetNumber = 8339
        val operationsLimit = 3

        FormulaVerifier.verify("18 * 21 * 22 + 23", availableNumbers, targetNumber, operationsLimit)

        val formula = solver.solve(availableNumbers, targetNumber)
        val result = ExpressionEvaluator.evaluate(formula)

        assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
        assertEquals(operationsLimit, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")
    }

}
