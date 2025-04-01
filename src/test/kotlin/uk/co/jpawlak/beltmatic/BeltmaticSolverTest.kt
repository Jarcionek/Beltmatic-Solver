package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.script.ScriptEngineManager

class BeltmaticSolverTest {

    private val solver = BeltmaticSolver()

    @Test
    fun test() {
        val formula = solver.solve(listOf(2, 3, 4), 7)

        val result = evaluate(formula)

        assertEquals(7, result)

        //TODO also assert that formula is "3 + 4" or "4 + 3"
    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    private fun evaluate(expression: String): Int {
        val engine = ScriptEngineManager().getEngineByName("js")
        val result = engine.eval(expression)

        // The result might be a Double or another numeric type, so we convert it to Int
        return when (result) {
            is Number -> result.toInt()
            else -> throw IllegalArgumentException("Expression did not evaluate to a number")
        }
    }

}
