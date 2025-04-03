package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import javax.script.ScriptEngineManager

class BeltmaticSolverTest {

    private val solver = BeltmaticSolver()

    @Suppress("UNCHECKED_CAST")
    @TestFactory
    fun `returns 1 operation formula with simple addition`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(listOf(2, 3, 4), 7),
            Arguments.of(listOf(3, 1, 5), 4),
            Arguments.of(listOf(10, 5, 2), 12),
            Arguments.of(listOf(3, 4, 5), 6),
        ).map { arguments ->
            DynamicTest.dynamicTest("${arguments.get()[0]} -> ${arguments.get()[1]}") {
                val availableNumbers = arguments.get()[0] as List<Int>
                val targetNumber = arguments.get()[1] as Int

                val formula = solver.solve(availableNumbers, targetNumber)
                val result = evaluate(formula)

                //TODO user proper testing library
                assertEquals(targetNumber, result)
                //TODO assert on the number of operations used
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @TestFactory
    fun `returns 2 operations formula with additions only`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(listOf(5), 15),
            Arguments.of(listOf(3, 10, 40), 53),
            Arguments.of(listOf(3, 10, 40), 16),
        ).map { arguments ->
            DynamicTest.dynamicTest("${arguments.get()[0]} -> ${arguments.get()[1]}") {
                val availableNumbers = arguments.get()[0] as List<Int>
                val targetNumber = arguments.get()[1] as Int

                val formula = solver.solve(availableNumbers, targetNumber)
                val result = evaluate(formula)

                assertEquals(targetNumber, result)
            }
        }
    }

    @Test
    fun `throws exception when it was not possible to find a formula`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            solver.solve(listOf(0), 3)
        }
        assertEquals("Could not find a formula to get 3 using no more than 2 operations", exception.message)
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
