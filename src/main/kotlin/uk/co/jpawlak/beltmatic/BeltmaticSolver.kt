package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.ThreadUtils.checkThreadInterrupted

class BeltmaticSolver(
    private val calculator: AvailableNumberCalculator,
    private val allAvailableNumbers: AvailableNumbers,
    private val combiner: AvailableNumbersCombiner,
) {

    fun solve(initiallyAvailableNumbers: List<Int>, targetNumber: Int): String {
        allAvailableNumbers.reset(initiallyAvailableNumbers)

        // find all numbers obtainable with 1 operation

        calculateExponentiationsOnly(
            allAvailableNumbers.getAll()
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 2 operations

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(1)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 3 operations

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(2)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(1)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 4 operations

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(3)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(1),
            allAvailableNumbers.getAllWithOperationCountEqualTo(2)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 5 operations

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(4)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(1),
            allAvailableNumbers.getAllWithOperationCountEqualTo(3)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        combiner.calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(2)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        throw IllegalArgumentException("Could not find a formula to get $targetNumber using no more than 5 operations")
    }

    private fun calculateExponentiationsOnly(availableNumbers: List<AvailableNumber>): Sequence<AvailableNumber> {
        return availableNumbers.asSequence().flatMap { a ->
            checkThreadInterrupted()
            availableNumbers.asSequence().mapNotNull { b ->
                calculator.power(a, b)
            }
        }
    }

}

