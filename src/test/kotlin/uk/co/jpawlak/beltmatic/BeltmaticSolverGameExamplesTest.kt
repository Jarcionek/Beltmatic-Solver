package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

/**
 * These tests assert on the exact formula, and try to find the numbers that the game requires to progress.
 *
 * They are intentionally asserting on the formula to highlight any changes when the algorithm is modified.
 */
class BeltmaticSolverGameExamplesTest {

    private val solver = BeltmaticSolverFactory.create()

    private val availableNumbers = ((1..9) + (11..24)).toList()

    @TestFactory
    fun `returns formula with any operations`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(1138,    "2 * (17 + 23 * 24)"         ),
            Arguments.of(14842,   "2 * (15 + 14 * 23 * 23)"    ),
            Arguments.of(16358,   "2 * (2^13 - 13)"           ),
            Arguments.of(18778,   "2 * (5 + 17 * 23 * 24)"    ),
            Arguments.of(20656,   "16 * (6^4 - 5)"            ),
            Arguments.of(23784,   "2 + 22 * 23 * (23 + 24)"   ),
            Arguments.of(24880,   "8 * (5^5 - 15)"            ),
            Arguments.of(27921,   "20 + 5^8 / 14"             ),
            Arguments.of(28256,   "4 * (8 + 14 * 21 * 24)"    ),
            Arguments.of(31510,   "5 * (2 + 15 * 20 * 21)"    ),
            Arguments.of(39489,   "21 + 6 * 13 * 22 * 23"     ),
            Arguments.of(43419,   "17 + 5^8 / 9"              ),
            Arguments.of(49430,   "11 + 9 * 17 * 17 * 19"     ),
            Arguments.of(54154,   "4 + 6 * (5 * 19)^2"        ),
            Arguments.of(94168,   "13 * 15 * 21 * 23 - 17"     ),
            Arguments.of(70647,   "5 + 13 * 13 * 19 * 22"      ),
            Arguments.of(73830,   "15 * (9 + 17^3)"            ),
            Arguments.of(853605,  "9 * ((14 * 22)^2 - 19)"     ),
//            Arguments.of(2572951, ""), // TODO target number above 1M, need to increase number limit
        ).map { arguments ->

            val targetNumber = arguments.get()[0] as Int
            val expectedFormula = arguments.get()[1] as String
            val operationCount = FormulaVerifier.operationCount(expectedFormula)

            DynamicTest.dynamicTest("$operationCount:   $targetNumber = $expectedFormula") {
                val formula = solver.solve(availableNumbers, targetNumber)
                val result = ExpressionEvaluator.evaluate(formula)

                println("$targetNumber = $formula")

                assertEquals(targetNumber, result, "The result of $formula is not $targetNumber")
            }
        }
    }

}
