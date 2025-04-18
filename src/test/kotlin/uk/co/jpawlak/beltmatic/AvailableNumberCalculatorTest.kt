package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.params.provider.Arguments
import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.initialNumber
import java.util.stream.Stream

class AvailableNumberCalculatorTest {

    private val calculator = AvailableNumberCalculator()

    @TestFactory
    fun `exponentiation returns correct results`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(4, 3 , 64),
            Arguments.of(-3, 2, 9),
            Arguments.of(-3, 3, -27),
        ).map { arguments ->

            val base = arguments.get()[0] as Int
            val exponent = arguments.get()[1] as Int
            val expectedResult = arguments.get()[2] as Int

            dynamicTest("$base^$exponent = $expectedResult") {
                assertEquals(expectedResult, calculator.power(initialNumber(base), initialNumber(exponent))!!.number)
            }
        }
    }

    /**
     * There are easier ways in the game to get number 0 or 1. Let's ignore those results.
     */
    @TestFactory
    fun `exponentiation never returns 0 and 1 as a result`(): Stream<DynamicTest> {
        return Stream.of(
            Arguments.of(3, -2, 0), // = 1 / (3^2) = 0 (no fractions in the game)
            Arguments.of(-3, -2, 0),
            Arguments.of(0, 3, 0),
            Arguments.of(3, 0, 1),
            Arguments.of(0, 0, 1), // that's what the game does
        ).map { arguments ->

            val base = arguments.get()[0] as Int
            val exponent = arguments.get()[1] as Int
            val expectedResult = arguments.get()[2] as Int

            dynamicTest("$base^$exponent returns null instead of $expectedResult") {
                assertNull(calculator.power(initialNumber(base), initialNumber(exponent)))
            }
        }
    }

    @Nested
    inner class Exponentiation {

        /**
         * Once the algorithm is better optimised, and [Config.NUMBER_LIMIT] is removed, we want these tests to run.
         * This method references the constant to cause a compilation error to remind us to enable these tests.
         */
        @BeforeEach
        fun `number limit is defined`() {
            @Suppress("SENSELESS_COMPARISON")
            assumeTrue(Config.NUMBER_LIMIT == null)
        }

        @Test
        fun `returns max integer for very large exponent`() {
            assertEquals(Integer.MAX_VALUE, calculator.power(initialNumber(2), initialNumber(35))!!.number)
        }

        @Test
        fun `returns max integer for very large base`() {
            assertEquals(Integer.MAX_VALUE, calculator.power(initialNumber(70_000), initialNumber(2))!!.number)
        }

        @Test
        fun `returns min integer for negative base and large odd exponent`() {
            assertEquals(Integer.MIN_VALUE, calculator.power(initialNumber(-2), initialNumber(35))!!.number)
        }

        @Test
        fun `returns min integer for large negative base and odd exponent`() {
            assertEquals(Integer.MIN_VALUE, calculator.power(initialNumber(-1291), initialNumber(3))!!.number)
        }

    }

}
