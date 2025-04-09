package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.Operation.*
import kotlin.math.pow

//TODO handle integer overflow
// in the game 24^24 = 2,147,483,647
// for negative numbers minimum is -2,147,483,648 (subtracting positive numbers from it results in the same number being returned)
private const val NUMBER_LIMIT = 1_000_000

/**
 * Merges two numbers, using provided operations, to calculate new numbers.
 */
class AvailableNumberCalculator {
    
    fun add(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number + b.number
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return AvailableNumber(
            result,
            ADDITION,
            "(${a.formula}) + (${b.formula})", //TODO too many parenthesis, optimise it
            a.formulaOperationsCount + b.formulaOperationsCount + 1
        )
    }

    fun subtract(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number - b.number
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return AvailableNumber(
            result,
            SUBTRACTION,
            "(${a.formula}) - (${b.formula})",
            a.formulaOperationsCount + b.formulaOperationsCount + 1
        )
    }

    fun multiply(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number * b.number
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return AvailableNumber(
            result,
            MULTIPLICATION,
            "(${a.formula}) * (${b.formula})",
            a.formulaOperationsCount + b.formulaOperationsCount + 1
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
        return AvailableNumber(
            result,
            EXPONENTIATION,
            "(${base.formula}) ^ (${exponent.formula})",
            base.formulaOperationsCount + exponent.formulaOperationsCount + 1
        )
    }

}
