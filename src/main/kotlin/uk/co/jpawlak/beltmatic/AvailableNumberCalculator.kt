package uk.co.jpawlak.beltmatic

import kotlin.math.pow

//TODO handle integer overflow
// in the game 24^24 = 2,147,483,647
// for negative numbers minimum is -2,147,483,648 (subtracting positive numbers from it results in the same number being returned)
private const val NUMBER_LIMIT = 100_000

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
            "(${a.formula}) * (${b.formula})",
            a.formulaOperationsCount + b.formulaOperationsCount + 1
        )
    }

    fun power(a: AvailableNumber, b: AvailableNumber): AvailableNumber? {
        val result = a.number.toDouble().pow(b.number).toInt() //TODO implement it without casts
        if (result < -NUMBER_LIMIT || NUMBER_LIMIT < result) {
            return null
        }
        return AvailableNumber(
            result,
            "(${a.formula}) ^ (${b.formula})",
            a.formulaOperationsCount + b.formulaOperationsCount + 1
        )
    }

}
