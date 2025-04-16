package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.provider.Arguments
import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.availableNumber
import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.initialNumber
import uk.co.jpawlak.beltmatic.Operation.*
import java.util.stream.Stream

class FormulaCreatorTest {

    private val formulaCreator = FormulaCreator()

    @Test
    fun `creates formula for initial number`() {
        val targetNumber = initialNumber(5)

        assertEquals("5", formulaCreator.createFormula(targetNumber))
    }

    @Test
    fun `creates formula for a number derived from addition`() {
        val targetNumber = availableNumber(
            5,
            ADDITION,
            initialNumber(3),
            initialNumber(2)
        )

        assertEquals("3 + 2", formulaCreator.createFormula(targetNumber))
    }

    @Test
    fun `creates formula with addition and multiplication`() {
        val targetNumber = availableNumber(
            20,
            MULTIPLICATION,
            availableNumber(
                5,
                ADDITION,
                initialNumber(2),
                initialNumber(3)
            ),
            initialNumber(4)
        )

        assertEquals("(2 + 3) * 4", formulaCreator.createFormula(targetNumber))
    }

    @Test
    fun `creates formula with two subtractions`() {
        val targetNumber = availableNumber(
            7,
            SUBTRACTION,
            initialNumber(10),
            availableNumber(
                3,
                SUBTRACTION,
                initialNumber(5),
                initialNumber(2)
            )
        )

        assertEquals("10 - (5 - 2)", formulaCreator.createFormula(targetNumber))
    }

    @Test
    fun `there are no spaces around exponentiation`() {
        val targetNumber = availableNumber(
            8,
            EXPONENTIATION,
            initialNumber(2),
            initialNumber(3)
        )
        assertEquals("2^3", formulaCreator.createFormula(targetNumber))
    }

    /**
     * These tests test the expression `A X B Y C`, where `X` and `Y` are various operations,
     * and `A`, `B` and `C` are different numbers.
     * If the result of expression `A X (B Y C)` would be different, then parentheses are needed.
     * If both expressions evaluate to the same value, then parentheses are redundant.
     */
    @TestFactory
    fun `parentheses on the right expression are present if they would change the result of the expression`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of('^', '^', true), // although not needed, let's add for clarity
            Arguments.of('^', '*', true),
//            Arguments.of('^', '/', true), // TODO uncomment once division is implemented
            Arguments.of('^', '+', true),
            Arguments.of('^', '-', true),

            Arguments.of('*', '^', false),
            Arguments.of('*', '*', false),
//            Arguments.of('*', '/', true),
            Arguments.of('*', '+', true),
            Arguments.of('*', '-', true),

//            Arguments.of('/', '^', false),
//            Arguments.of('/', '*', true),
//            Arguments.of('/', '/', true),
//            Arguments.of('/', '+', true),
//            Arguments.of('/', '-', true),

            Arguments.of('+', '^', false),
            Arguments.of('+', '*', false),
//            Arguments.of('+', '/', false),
            Arguments.of('+', '+', false),
            Arguments.of('+', '-', false),

            Arguments.of('-', '^', false),
            Arguments.of('-', '*', false),
//            Arguments.of('-', '/', false),
            Arguments.of('-', '+', true),
            Arguments.of('-', '-', true),
        ).map { arguments ->

            val leftOperation = arguments.get()[0] as Char
            val rightOperation = arguments.get()[1] as Char
            val expectParentheses = arguments.get()[2] as Boolean

            DynamicTest.dynamicTest("'A $leftOperation B $rightOperation C' vs 'A $leftOperation (B $rightOperation C)'. Parentheses needed: $expectParentheses") {
                val targetNumber = availableNumber(
                    0, // ignored
                    Operation.fromSymbol(leftOperation),
                    initialNumber(4),
                    availableNumber(
                        0, // ignored
                        Operation.fromSymbol(rightOperation),
                        initialNumber(3),
                        initialNumber(2)
                    )
                )

                val formula = formulaCreator.createFormula(targetNumber)

                if (expectParentheses) {
                    assertTrue(
                        formula.count { it == '(' } == 1 && formula.count { it == ')' } == 1,
                        "Formula should contain parentheses: $formula"
                    )
                } else {
                    assertTrue(
                        formula.count { it == '(' } == 0 && formula.count { it == ')' } == 0,
                        "Formula should not contain parentheses: $formula"
                    )
                }
            }
        }
    }

    /**
     * These tests test the expression `A X B Y C`, where `X` and `Y` are various operations,
     * and `A`, `B` and `C` are different numbers.
     * If the result of expression `(A X B) Y C` would be different, then parentheses are needed.
     * If both expressions evaluate to the same value, then parentheses are redundant.
     */
    @TestFactory
    fun `parentheses on the left expression are present if they would change the result of the expression`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of('^', '^', true), // although not needed, let's add for clarity
            Arguments.of('^', '*', false),
//            Arguments.of('^', '/', false), // TODO uncomment once division is implemented
            Arguments.of('^', '+', false),
            Arguments.of('^', '-', false),

            Arguments.of('*', '^', true),
            Arguments.of('*', '*', false),
//            Arguments.of('*', '/', false),
            Arguments.of('*', '+', false),
            Arguments.of('*', '-', false),

//            Arguments.of('/', '^', true),
//            Arguments.of('/', '*', false),
//            Arguments.of('/', '/', false),
//            Arguments.of('/', '+', false),
//            Arguments.of('/', '-', false),

            Arguments.of('+', '^', true),
            Arguments.of('+', '*', true),
//            Arguments.of('+', '/', true),
            Arguments.of('+', '+', false),
            Arguments.of('+', '-', false),

            Arguments.of('-', '^', true),
            Arguments.of('-', '*', true),
//            Arguments.of('-', '/', true),
            Arguments.of('-', '+', false),
            Arguments.of('-', '-', false),
        ).map { arguments ->

            val leftOperation = arguments.get()[0] as Char
            val rightOperation = arguments.get()[1] as Char
            val expectParentheses = arguments.get()[2] as Boolean

            DynamicTest.dynamicTest("'A $leftOperation B $rightOperation C' vs '(A $leftOperation B) $rightOperation C'. Parentheses needed: $expectParentheses") {
                val targetNumber = availableNumber(
                    0, // ignored
                    Operation.fromSymbol(rightOperation),
                    availableNumber(
                        0, // ignored
                        Operation.fromSymbol(leftOperation),
                        initialNumber(4),
                        initialNumber(3)
                    ),
                    initialNumber(2)
                )

                val formula = formulaCreator.createFormula(targetNumber)

                if (expectParentheses) {
                    assertTrue(
                        formula.count { it == '(' } == 1 && formula.count { it == ')' } == 1,
                        "Formula should contain parentheses: $formula"
                    )
                } else {
                    assertTrue(
                        formula.count { it == '(' } == 0 && formula.count { it == ')' } == 0,
                        "Formula should not contain parentheses: $formula"
                    )
                }
            }
        }
    }

}
