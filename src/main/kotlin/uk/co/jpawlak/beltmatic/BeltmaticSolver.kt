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

        sequenceOf(
            calculateExponentiationsOnly(allAvailableNumbers.getAll()),
            combiner.calculateNewAvailableNumbers(Product(0, 0))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 2 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 1))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 3 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 2)),
            combiner.calculateNewAvailableNumbers(Product(1, 1))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 4 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 3)),
            combiner.calculateNewAvailableNumbers(Product(1, 2))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 5 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 4)),
            combiner.calculateNewAvailableNumbers(Product(1, 3)),
            combiner.calculateNewAvailableNumbers(Product(2, 2))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

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

