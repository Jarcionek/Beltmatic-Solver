package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.availableNumber
import uk.co.jpawlak.beltmatic.AvailableNumber.Companion.initialNumber

class AvailableNumbers() {

    private val allAvailableNumbers: MutableMap<Int, AvailableNumber> = hashMapOf()

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
     * Stores the [newNumber] if it is better than already existing [AvailableNumber].
     */
    fun addIfBetter(newNumber: AvailableNumber) {
        val oldNumber: AvailableNumber? = allAvailableNumbers[newNumber.number]

        if (oldNumber == null) {
            put(newNumber)
            return
        }

        if (newNumber.formulaOperationsCount < oldNumber.formulaOperationsCount) {
            //TODO this is dead code, because of how this method is called from the BeltmaticSolver,
            put(newNumber)
            return
        }

        if (
            newNumber.formulaOperationsCount == oldNumber.formulaOperationsCount
            && newNumber.formulaOperationsCost < oldNumber.formulaOperationsCost
        ) {
            put(newNumber)
            return
        }
    }

    private fun put(newNumber: AvailableNumber) {
        allAvailableNumbers[newNumber.number] = newNumber
    }

    fun get(number: Int): AvailableNumber? {
        if (!allAvailableNumbers.containsKey(number)) {
            return null
        }

        return createOptimalNumber(number)
    }

    fun getAllWithOperationCountEqualTo(operationCount: Int): List<AvailableNumber> =
        allAvailableNumbers.values.filter { it.formulaOperationsCount == operationCount }

    /**
     * [AvailableNumber.leftNumber] and [AvailableNumber.rightNumber] are likely outdated.
     * This function will pick better ones from [allAvailableNumbers].
     */
    private fun createOptimalNumber(target: Int): AvailableNumber {
        val availableNumber = allAvailableNumbers[target]!!
        if (availableNumber.isInitialNumber()) {
            return availableNumber
        }
        return availableNumber(
            availableNumber.number,
            availableNumber.operation!!,
            createOptimalNumber(availableNumber.leftNumber!!.number),
            createOptimalNumber(availableNumber.rightNumber!!.number),
        )
    }

}
