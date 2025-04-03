package uk.co.jpawlak.beltmatic

/**
 * If the first iteration creates available number (1 + 2),
 * then second iteration can create available number ((1+2) + (1+2)), which has 3 operations already,
 * then third iteration can create available number (((1+2) + (1+2)) + ((1+2) + (1+2))), which has 7 operations.
 *
 * So max operations is actually equal (2 ^ iterations) - 1
 */
private const val MAX_ITERATIONS = 3 //TODO change it to MAX_OPERATIONS

class BeltmaticSolver {

    fun solve(initiallyAvailableNumbers: List<Int>, targetNumber: Int): String {
        val allAvailableNumbers: MutableMap<Int, AvailableNumber> = initiallyAvailableNumbers.associateBy(
            { it },
            { AvailableNumber(it, "$it", 0) }
        ).toMutableMap()

        for (i in 1..MAX_ITERATIONS) {

            calculateNewAvailableNumbers(allAvailableNumbers)
                .onEach {
                    //TODO with longer formulas, we should probably finish the iteration to find the best formula, but no test is catching this yet
                    if (it.key == targetNumber) {
                        return it.value.formula
                    }
                }
                .forEach { addIfBetter(allAvailableNumbers, it.value) }

//            val formula: String? = allAvailableNumbers[targetNumber]?.formula
//            if (formula != null) {
//                return formula
//            }
        }

        throw IllegalArgumentException("Could not find a formula to get $targetNumber using no more than $MAX_ITERATIONS iterations")
    }

    private fun calculateNewAvailableNumbers(availableNumbers: Map<Int, AvailableNumber>): MutableMap<Int, AvailableNumber> {
        val newNumbers: MutableMap<Int, AvailableNumber> = mutableMapOf()
        for (a in availableNumbers.values) {
            for (b in availableNumbers.values) {
                addIfBetter(newNumbers, a.add(b))
                addIfBetter(newNumbers, a.subtract(b))
                addIfBetter(newNumbers, a.multiply(b))
            }
        }
        return newNumbers
    }

    /**
     * If [newNumber] has smaller operation count than the [AvailableNumber] in [availableNumbers],
     * puts the [newNumber] into the [Map].
     */
    private fun addIfBetter(
        availableNumbers: MutableMap<Int, AvailableNumber>,
        newNumber: AvailableNumber
    ) {
        val oldNumber = availableNumbers[newNumber.number]
        if (oldNumber == null || newNumber.formulaOperationsCount < oldNumber.formulaOperationsCount) {
            availableNumbers[newNumber.number] = newNumber
        }
    }
}

private data class AvailableNumber(
    val number: Int,
    val formula: String,
    val formulaOperationsCount: Int,
) {

    fun add(that: AvailableNumber) = AvailableNumber(
        this.number + that.number,
        "(${this.formula}) + (${that.formula})", //TODO too many parenthesis, optimise it
        this.formulaOperationsCount + that.formulaOperationsCount
    )

    fun subtract(that: AvailableNumber) = AvailableNumber(
        this.number - that.number,
        "(${this.formula}) - (${that.formula})",
        this.formulaOperationsCount + that.formulaOperationsCount
    )

    fun multiply(that: AvailableNumber) = AvailableNumber(
        this.number * that.number,
        "(${this.formula}) * (${that.formula})",
        this.formulaOperationsCount + that.formulaOperationsCount
    )

}
