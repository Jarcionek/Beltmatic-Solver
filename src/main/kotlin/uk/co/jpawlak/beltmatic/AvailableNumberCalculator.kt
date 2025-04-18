package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.availableNumber
import uk.co.jpawlak.beltmatic.Config.NUMBER_LIMIT
import uk.co.jpawlak.beltmatic.Operation.*

//TODO handle integer overflow
// in the game 24^24 = 2,147,483,647
// for negative numbers minimum is -2,147,483,648 (subtracting positive numbers from it results in the same number being returned)
/**
 * Merges two numbers, using provided operations, to calculate new numbers.
 */
class AvailableNumberCalculator {
    
    fun add(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number + b.number

        return limitedAvailableNumber(result, ADDITION, a, b)
    }

    fun subtract(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number - b.number

        return limitedAvailableNumber(result, SUBTRACTION, a, b)
    }

    fun multiply(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number * b.number

        return limitedAvailableNumber(result, MULTIPLICATION, a, b)
    }

    // TODO add remainder to available numbers - this requires a feature of a single use numbers
    fun divide(dividend: AvailableNumber, divisor: AvailableNumber): AvailableNumber? {
        if (divisor.number == 0) return null // TODO in case of division by 0, the game returns 0 as a result and dividend as a remainder

        val result = dividend.number / divisor.number

        return limitedAvailableNumber(result, DIVISION, dividend, divisor)
    }

    fun power(base: AvailableNumber, exponent: AvailableNumber): AvailableNumber? {
        if (exponent.number < 2) {
            return null // in the game these are useless operations that return 0, 1, or the base number
        }
        if (base.number == 0) {
            return null // in the game 0^x=0 is a useless operation
        }

        if (exponent.number > 32) { // this is an incredible optimisation
            return null
        }

        var result = 1
        for (i in 1..exponent.number) {
            result *= base.number
        }

        return limitedAvailableNumber(result, EXPONENTIATION, base, exponent)
    }

    private fun limitedAvailableNumber(
        result: Int,
        operation: Operation,
        left: AvailableNumber,
        right: AvailableNumber
    ): AvailableNumber? {
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return availableNumber(result, operation, left, right)
    }

}
