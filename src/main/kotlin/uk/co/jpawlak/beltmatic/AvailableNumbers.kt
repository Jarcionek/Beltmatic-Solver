package uk.co.jpawlak.beltmatic

class AvailableNumbers() {

    private val allAvailableNumbers: MutableMap<Int, AvailableNumber> = mutableMapOf()

    /**
     * Used for initialisation. Wipes all the number collected so far.
     */
    fun reset(initiallyAvailableNumbers: List<Int>) {
        allAvailableNumbers.clear()
        initiallyAvailableNumbers
            .map { AvailableNumber(it, "$it", 0) }
            .forEach { allAvailableNumbers[it.number] = it }
    }

    /**
     * If [newNumber] has smaller operation count than the already existing [AvailableNumber]
     * (or if no such number already exists), stores the [newNumber].
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
