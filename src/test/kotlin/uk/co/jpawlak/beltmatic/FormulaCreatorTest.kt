package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.availableNumber
import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.initialNumber
import uk.co.jpawlak.beltmatic.Operation.*

@Disabled
class FormulaCreatorTest {

    private val formulaCreator = FormulaCreator()

    @Test
    fun `creates formula for initial number`() {
        val targetNumber = initialNumber(5)

        assertEquals("5", formulaCreator.createFormula(targetNumber))
    }

    @Test
    fun `creates formula for a number derived from addition`() {
        val left = initialNumber(3)
        val right = initialNumber(2)
        val targetNumber = availableNumber(
            resultNumber = 5,
            operation = ADDITION,
            sourceNumberOne = left,
            sourceNumberTwo = right
        )

        assertEquals("3 + 2", formulaCreator.createFormula(targetNumber))
    }

    @Test
    fun `creates formula with addition and multiplication`() {
        val a = initialNumber(2)
        val b = initialNumber(3)
        val c = initialNumber(4)

        val ab = availableNumber(
            resultNumber = 5,
            operation = ADDITION,
            sourceNumberOne = a,
            sourceNumberTwo = b
        )

        val abc = availableNumber(
            resultNumber = 20,
            operation = MULTIPLICATION,
            sourceNumberOne = ab,
            sourceNumberTwo = c
        )

        assertEquals("(2 + 3) * 4", formulaCreator.createFormula(abc))
    }

    @Test
    fun `creates formula with two subtractions`() {
        val a = initialNumber(10)
        val b = initialNumber(5)
        val c = initialNumber(2)

        val bc = availableNumber(
            resultNumber = 3,
            operation = SUBTRACTION,
            sourceNumberOne = b,
            sourceNumberTwo = c
        )

        val abc = availableNumber(
            resultNumber = 7,
            operation = SUBTRACTION,
            sourceNumberOne = a,
            sourceNumberTwo = bc
        )

        assertEquals("10 - (5 - 2)", formulaCreator.createFormula(abc))
    }

}
