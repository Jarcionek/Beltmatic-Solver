package uk.co.jpawlak.beltmatic

class AvailableNumbers(initiallyAvailableNumbers: List<Int>) {

    private val allAvailableNumbers: MutableMap<Int, AvailableNumber> = initiallyAvailableNumbers.associateBy(
        { it },
        { AvailableNumber(it, "$it", 0) }
    ).toMutableMap()

    /**
     * If [newNumber] has smaller operation count than the already existing [AvailableNumber]
     * (or if no such number already exists),
     * stores the [newNumber] in [AvailableNumbers].
     */
    fun addIfBetter(newNumber: AvailableNumber) {
        val oldNumber = allAvailableNumbers[newNumber.number]
        if (oldNumber == null || newNumber.formulaOperationsCount < oldNumber.formulaOperationsCount) {
            allAvailableNumbers[newNumber.number] = newNumber
        }
    }

    fun getAll() = allAvailableNumbers.values.toList()

    fun get(targetNumber: Int): AvailableNumber? {
        return allAvailableNumbers[targetNumber]
    }

    fun getAllWithOperationCountEqualTo(operationCount: Int) =
        allAvailableNumbers.values.filter { it.formulaOperationsCount == operationCount }

}
