package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.availableNumber
import uk.co.jpawlak.beltmatic.Config.NUMBER_LIMIT
import uk.co.jpawlak.beltmatic.Operation.*
import kotlin.math.pow

//TODO handle integer overflow
// in the game 24^24 = 2,147,483,647
// for negative numbers minimum is -2,147,483,648 (subtracting positive numbers from it results in the same number being returned)
/**
 * Merges two numbers, using provided operations, to calculate new numbers.
 */
class AvailableNumberCalculator {
    
    fun add(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number + b.number
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return availableNumber(
            result,
            ADDITION,
            a,
            b,
        )
    }

    fun subtract(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number - b.number
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return availableNumber(
            result,
            SUBTRACTION,
            a,
            b,
        )
    }

    fun multiply(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number * b.number
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return availableNumber(
            result,
            MULTIPLICATION,
            a,
            b,
        )
    }

    // TODO add remainder to available numbers - this requires a feature of a single use numbers
    fun divide(dividend: AvailableNumber, divisor: AvailableNumber): AvailableNumber? {
        if (divisor.number == 0) return null // TODO in case of division by 0, the game returns 0 as a result and dividend as a remainder

        val result = dividend.number / divisor.number
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return availableNumber(
            result,
            DIVISION,
            dividend,
            divisor,
        )
    }

    fun power(base: AvailableNumber, exponent: AvailableNumber): AvailableNumber? {
        // TODO implement extra optimisations and checks
        //
        // maximum base is 65536 this is 2^31, square it, and we get MAX INT
        // for cube the maximum base is around 1625
        // for power of four maximum base is 256
        //
        // maximum exponent is 32
        //
        // consider edge cases around base/exponent being 0 or 1
        //
        // consider edge cases around negative base/exponent
        val result = base.number.toDouble().pow(exponent.number).toInt() //TODO implement it without casts
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return availableNumber(
            result,
            EXPONENTIATION,
            base,
            exponent,
        )
    }

}
