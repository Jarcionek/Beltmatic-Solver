package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

@Suppress("UNCHECKED_CAST")
class BeltmaticSolverTest {

    private val solver = BeltmaticSolver()

    @Test
    fun `throws exception when it was not possible to find a formula`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            val result = solver.solve(listOf(0), 17)
            println(result)
        }

        //TODO user proper testing library
        assertTrue(
            exception.message!!.startsWith("Could not find a formula to get 17 using no more than "),
            "Unexpected exception message: ${exception.message}"
        )
    }

    //TODO edge case of target number being in available numbers is not handled

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
                val result = ExpressionEvaluator.evaluate(formula)

                //TODO user proper testing library
                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(1, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")
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
                val result = ExpressionEvaluator.evaluate(formula)

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(2, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

    @TestFactory
    fun `returns formula with additions and subtractions only`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(1, listOf(7), 0, "7 - 7"),
            Arguments.of(1, listOf(5, 8), 3, "8 - 5"),
            Arguments.of(1, listOf(8, 5), 3, "8 - 5"),
            Arguments.of(2, listOf(10, 100, 1000), 890, "1000 - 100 - 10"),
            Arguments.of(2, listOf(1000, 100, 10), 890, "1000 - 100 - 10"),
            Arguments.of(2, listOf(3, 10, 40), 47, "40 + 10 - 3"),
            Arguments.of(3, listOf(1200, 11, 100, 7), 1104, "(1200 + 11) - (100 + 7)"),  //TODO test all permutations of available numbers?
            Arguments.of(3, listOf(7, 1200, 11, 100), 1104, "(1200 + 11) - (100 + 7)"),
            Arguments.of(3, listOf(100, 7, 1200, 11), 1104, "(1200 + 11) - (100 + 7)"),
            Arguments.of(3, listOf(11, 100, 7, 1200), 1104, "(1200 + 11) - (100 + 7)"),
            Arguments.of(3, listOf(100, 1200, 7, 11), 1104, "(1200 + 11) - (100 + 7)"),
        ).map {
            val arguments = it.get()

            val expectedOperations = arguments[0] as Int
            val availableNumbers = arguments[1] as List<Int>
            val targetNumber = arguments[2] as Int
            val exampleFormula = arguments[3] as String
            FormulaVerifier.verify(exampleFormula, availableNumbers, targetNumber, expectedOperations)

            DynamicTest.dynamicTest("$availableNumbers -> $targetNumber in $expectedOperations operations") {
                val formula = solver.solve(availableNumbers, targetNumber)
                val result = ExpressionEvaluator.evaluate(formula)

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(expectedOperations, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

    @TestFactory
    fun `returns formula with multiplications, additions and subtractions only`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(3, listOf(3, 4, 6, 13),         258,   "(3 * 13 + 4) * 6"            ),
            Arguments.of(4, listOf(1, 4, 5, 23),         2301,  "5 * 5 * 4 * 23 + 1"          ),
            Arguments.of(5, listOf(1, 2, 9, 11, 19, 24), 4164,  "(19 * 9 + 2) * 24 + 11 + 1"  ),
            Arguments.of(5, listOf(5, 7, 8, 11, 13),     12973, "13 * 13 * 7 * 11 - 5 * 8"    ),
            Arguments.of(3, listOf(3, 13, 16, 19),       3955,  "13 * 16 * 19 + 3"            ),
            Arguments.of(4, listOf(3, 5, 17, 20),        9775,  "(3 + 20) * 5 * 5 * 17"       ),
            Arguments.of(5, listOf(1, 11, 20),           830,   "(20 + 20) * 20 + 20 + 11 - 1"),
        ).map {
            val arguments = it.get()

            val expectedOperations = arguments[0] as Int
            val availableNumbers = arguments[1] as List<Int>
            val targetNumber = arguments[2] as Int
            val exampleFormula = arguments[3] as String
            FormulaVerifier.verify(exampleFormula, availableNumbers, targetNumber, expectedOperations)

            DynamicTest.dynamicTest("$availableNumbers -> $targetNumber in $expectedOperations operations") {
                val formula = solver.solve(availableNumbers, targetNumber)
                val result = ExpressionEvaluator.evaluate(formula)

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(expectedOperations, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

    @TestFactory
    fun `returns formula with any operations`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(4, listOf(1, 3, 6, 9),         766,   "9^3 + 6*6 + 1"         ),
            Arguments.of(4, listOf(2, 3, 7, 11),        8770,  "((2 * 3 ^ 7) + 11) * 2"),
//            Arguments.of(3, listOf(2, 5, 16),           1369,  "(2*16 + 5)^2"          ), //TODO fix it - this is because of the constraint to only use exponentiations on initial numbers
            Arguments.of(4, listOf(2, 3, 5, 13, 23),    1738,  "5^2 * 3*23 + 13"       ),
            Arguments.of(3, listOf(3, 7, 11, 13),       4470,  "7^3 * 13 + 11"         ),
            Arguments.of(4, listOf(2, 3, 7, 8, 19),     7445,  "8*19*7^2 - 3"          ),
            Arguments.of(5, listOf(2, 4, 5, 7, 11),     31801, "7^2 * 11 * (11*5 + 4)" ),
        ).map {
            val arguments = it.get()

            val expectedOperations = arguments[0] as Int
            val availableNumbers = arguments[1] as List<Int>
            val targetNumber = arguments[2] as Int
            val exampleFormula = arguments[3] as String
            FormulaVerifier.verify(exampleFormula, availableNumbers, targetNumber, expectedOperations)

            DynamicTest.dynamicTest("$availableNumbers -> $targetNumber in $expectedOperations operations") {
                val formula = solver.solve(availableNumbers, targetNumber)
                val result = ExpressionEvaluator.evaluate(formula)

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
                assertEquals(expectedOperations, FormulaVerifier.operationCount(formula), "Unexpected number of operations: $formula")
            }
        }
    }

}
