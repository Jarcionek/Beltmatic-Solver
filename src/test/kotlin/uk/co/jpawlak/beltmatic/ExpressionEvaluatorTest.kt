package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExpressionEvaluatorTest {
    
    @Test
    fun `returns correct result for valid arithmetic expressions`() {
        val expression = "2 + 3 * 4"

        val result = ExpressionEvaluator.evaluate(expression)

        assertEquals(14, result)
    }
    
    @Test
    fun `supports infix exponentiation`() {
        val expression = "2 ^ 5"

        val result = ExpressionEvaluator.evaluate(expression)

        assertEquals(32, result)
    }

    @Test
    fun `returns integer result of division`() {
        val expression = "4 / 3"

        val result = ExpressionEvaluator.evaluate(expression)

        assertEquals(1, result)
    }
    
    @Test
    fun `discards remainder of division`() {
        val expression = "4 / 3 * 6"

        val result = ExpressionEvaluator.evaluate(expression)

        assertEquals(6, result)
    }

    @Test
    fun `evaluates multiple exponentiations right to left`() {
        val expression = "4^3^2"

        val result = ExpressionEvaluator.evaluate(expression)

        assertEquals(262144, result)
    }
}
