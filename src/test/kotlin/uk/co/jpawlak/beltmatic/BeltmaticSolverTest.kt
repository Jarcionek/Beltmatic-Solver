package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import javax.script.ScriptEngineManager

class BeltmaticSolverTest {

    private val solver = BeltmaticSolver()

    class MathTestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(listOf(2, 3, 4), 7),
                Arguments.of(listOf(3, 1, 5), 4),
                Arguments.of(listOf(10, 5, 2), 12)
            )
        }
    }

    //TODO improve tests names
    @ParameterizedTest
    @ArgumentsSource(MathTestArgumentsProvider::class)
    fun `returns 1 operation formula with simple addition`(availableNumbers: List<Int>, targetNumber: Int) {
        val formula = solver.solve(availableNumbers, targetNumber)

        val result = evaluate(formula)

        //TODO user proper testing library
        assertEquals(targetNumber, result)
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
