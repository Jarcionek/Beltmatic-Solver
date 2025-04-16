package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.initialNumber

class AvailableNumbers() {

    private val allAvailableNumbers: MutableMap<Int, AvailableNumber> = mutableMapOf()

    /**
     * Used for initialisation. Wipes all the number collected so far.
     */
    fun reset(initiallyAvailableNumbers: List<Int>) {
        allAvailableNumbers.clear()
        initiallyAvailableNumbers
            .map { initialNumber(it) }
            .forEach { allAvailableNumbers[it.number] = it }
    }

    /**
     * If [newNumber] has smaller operation count than the already existing [AvailableNumber]
     * (or if no such number already exists), stores the [newNumber].
     */
    fun addIfBetter(newNumber: AvailableNumber) {
        val oldNumber = allAvailableNumbers[newNumber.number]

        if (oldNumber == null) {
            put(newNumber)
            return
        }

        if (oldNumber.operation == null) {
            // this is an initial number, new number cannot be better
            return
        }

        if (newNumber.formulaOperationsCount < oldNumber.formulaOperationsCount) {
            put(newNumber)
            return
        }

        if (
            newNumber.formulaOperationsCount == oldNumber.formulaOperationsCount
            && newNumber.operation!!.preference > oldNumber.operation.preference
        ) {
            put(newNumber)
            return
        }
    }

    private fun put(newNumber: AvailableNumber) {
        allAvailableNumbers[newNumber.number] = newNumber
    }

    fun getAll() = allAvailableNumbers.values.toList()

    fun get(targetNumber: Int): AvailableNumber? {
        return allAvailableNumbers[targetNumber]
    }

    fun getAllWithOperationCountEqualTo(operationCount: Int) =
        allAvailableNumbers.values.filter { it.formulaOperationsCount == operationCount }

}
