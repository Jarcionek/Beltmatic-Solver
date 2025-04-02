package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.script.ScriptEngineManager

class BeltmaticSolverTest {

    private val solver = BeltmaticSolver()

    @Test
    fun `returns 1 operation formula with simple addition`() {
        val formula = solver.solve(listOf(2, 3, 4), 7)

        val result = evaluate(formula)

        //TODO user proper testing library
        assertEquals(7, result)
        assertTrue(formula == "3 + 4" || formula == "4 + 3")
    }


    @Suppress("MoveVariableDeclarationIntoWhen")
    private fun evaluate(expression: String): Int {
        if (expression.contains("/")) {
            //TODO Need to handle integer division. I.e. "4 / 3 * 6" should 6, not 8.
            throw IllegalArgumentException("Division is not supported")
        }

        val engine = ScriptEngineManager().getEngineByName("js")
        val result = engine.eval(expression)

        // The result might be a Double or another numeric type, so we convert it to Int
        return when (result) {
            is Number -> result.toInt()
            else -> throw IllegalArgumentException("Expression did not evaluate to a number")
        }
    }

}
