package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import javax.script.ScriptEngineManager

@Suppress("UNCHECKED_CAST")
class BeltmaticSolverTest {

    private val solver = BeltmaticSolver()

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
                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(1, operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

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

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(2, operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

    @TestFactory
    fun `returns formula with additions and subtractions only`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(1, listOf(7), 0), // 7 - 7
            Arguments.of(1, listOf(5, 8), 3), // 8 - 5
            Arguments.of(1, listOf(8, 5), 3), // 8 - 5
            Arguments.of(2, listOf(10, 100, 1000), 890), // 1000 - 100 - 10
            Arguments.of(2, listOf(1000, 100, 10), 890), // 1000 - 100 - 10
            Arguments.of(2, listOf(3, 10, 40), 47), // 40 + 10 - 3
            Arguments.of(3, listOf(1200, 11, 100, 7), 1104), // (1200 + 11) - (100 + 7) //TODO test all permutations of available numbers?
            Arguments.of(3, listOf(7, 1200, 11, 100), 1104), // (1200 + 11) - (100 + 7)
            Arguments.of(3, listOf(100, 7, 1200, 11), 1104), // (1200 + 11) - (100 + 7)
            Arguments.of(3, listOf(11, 100, 7, 1200), 1104), // (1200 + 11) - (100 + 7)
            Arguments.of(3, listOf(100, 1200, 7, 11), 1104), // (1200 + 11) - (100 + 7)
        ).map { arguments ->
            DynamicTest.dynamicTest("${arguments.get()[1]} -> ${arguments.get()[2]} in ${arguments.get()[0]} operations") {
                val expectedOperations = arguments.get()[0] as Int
                val availableNumbers = arguments.get()[1] as List<Int>
                val targetNumber = arguments.get()[2] as Int

                val formula = solver.solve(availableNumbers, targetNumber)
                val result = evaluate(formula)

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(expectedOperations, operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

    @TestFactory
    fun `returns formula with multiplications, additions and subtractions only`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(4, listOf(3, 4, 6, 13), 258), // 13 * 6 * 3 + 4 * 6
            Arguments.of(4, listOf(1, 4, 5, 23), 2301), // 5 * 5 * 4 * 23 + 1
            Arguments.of(5, listOf(1, 2, 9, 11, 19, 24), 4164), // (19 * 9 + 2) * 24 + 11 + 1
//            Arguments.of(5, listOf(5, 7, 8, 11, 13), 12973), // 13 * 13 * 7 * 11 - 5 * 8 //TODO it is finding it in 6 operations: 5 * (5 - 13) + 7 * 11 * 13 * 13
        ).map { arguments ->
            DynamicTest.dynamicTest("${arguments.get()[1]} -> ${arguments.get()[2]} in ${arguments.get()[0]} operations") {
                val expectedOperations = arguments.get()[0] as Int
                val availableNumbers = arguments.get()[1] as List<Int>
                val targetNumber = arguments.get()[2] as Int

                val formula = solver.solve(availableNumbers, targetNumber)
                val result = evaluate(formula)

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(expectedOperations, operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

    @Test
    fun `throws exception when it was not possible to find a formula`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            solver.solve(listOf(0), 3)
        }

        //TODO user proper testing library
        assertTrue(
            exception.message!!.startsWith("Could not find a formula to get 3 using no more than "),
            "Unexpected exception message: ${exception.message}"
        )
    }

    //TODO this does not support infix exponentiation operation, switch to Javaluator: https://javaluator.fathzer.com/en/doc/tutorial.php?chapter=extending
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

    private fun operationCount(formula: String) = formula.count { it in listOf('+', '-', '*', '/', '^',) }

}
