package uk.co.jpawlak.beltmatic

private const val MAX_OPERATIONS = 2

class BeltmaticSolver {

    fun solve(initiallyAvailableNumbers: List<Int>, targetNumber: Int): String {
        val allAvailableNumbers: MutableMap<Int, AvailableNumber> = initiallyAvailableNumbers.associateBy(
            { it },
            { AvailableNumber(it, "$it", 0) }
        ).toMutableMap()

        for (i in 1..MAX_OPERATIONS) {
            calculateNewAvailableNumbers(allAvailableNumbers)
                .onEach {
                    if (it.key == targetNumber) {
                        return it.value.formula
                    }
                }
                .forEach { addIfBetter(allAvailableNumbers, it.value) }
        }

        throw IllegalArgumentException("Could not find a formula to get $targetNumber using no more than $MAX_OPERATIONS operations")
    }

    private fun calculateNewAvailableNumbers(availableNumbers: Map<Int, AvailableNumber>): MutableMap<Int, AvailableNumber> {
        val newNumbers: MutableMap<Int, AvailableNumber> =  mutableMapOf()
        for (a in availableNumbers.values) {
            for (b in availableNumbers.values) {
                val newNumber = join(a, b)
                addIfBetter(newNumbers, newNumber)
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

    private fun join(a: AvailableNumber, b: AvailableNumber): AvailableNumber {
        return AvailableNumber(
            a.number + b.number,
            "${a.formula} + ${b.formula}", //TODO will need parenthesis here
            a.formulaOperationsCount + b.formulaOperationsCount
        )
    }

}

data class AvailableNumber(
    val number: Int,
    val formula: String,
    val formulaOperationsCount: Int,
)
