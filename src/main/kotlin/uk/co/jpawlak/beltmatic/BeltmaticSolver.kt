package uk.co.jpawlak.beltmatic

/**
 * If the first iteration creates available number (1 + 2),
 * then second iteration can create available number ((1+2) + (1+2)), which has 3 operations already,
 * then third iteration can create available number (((1+2) + (1+2)) + ((1+2) + (1+2))), which has 7 operations.
 *
 * So max operations is actually equal (2 ^ iterations) - 1
 */
private const val MAX_ITERATIONS = 3 //TODO change it to MAX_OPERATIONS

class BeltmaticSolver(
    private val availableNumberCalculator: AvailableNumberCalculator = AvailableNumberCalculator()
) {

    private lateinit var allAvailableNumbers: AvailableNumbers

    fun solve(initiallyAvailableNumbers: List<Int>, targetNumber: Int): String {
        allAvailableNumbers = AvailableNumbers(initiallyAvailableNumbers)

        calculateExponentiationsOnly(allAvailableNumbers.getAll())
            .forEach { allAvailableNumbers.addIfBetter(it) }

        for (i in 1..MAX_ITERATIONS) {

            calculateNewAvailableNumbers(allAvailableNumbers.getAll())
                .forEach { allAvailableNumbers.addIfBetter(it) }

            //TODO suboptimal - waiting for the iteration to finish, even if the target number was already found
            val formula: String? = allAvailableNumbers.get(targetNumber)?.formula
            if (formula != null) {
                return formula
            }
        }

        throw IllegalArgumentException("Could not find a formula to get $targetNumber using no more than $MAX_ITERATIONS iterations")
    }

    private fun calculateExponentiationsOnly(availableNumbers: List<AvailableNumber>): Sequence<AvailableNumber> {
        return availableNumbers.asSequence().flatMap { a ->
            checkThreadInterrupted()
            availableNumbers.asSequence().mapNotNull { b ->
                availableNumberCalculator.power(a, b)
            }
        }
    }

    private fun calculateNewAvailableNumbers(availableNumbers: List<AvailableNumber>): Sequence<AvailableNumber> {
        return availableNumbers.asSequence().flatMap { a ->
            checkThreadInterrupted()
            availableNumbers.asSequence().flatMap { b ->
                //TODO optimize - even if the target number was found, this will continue the whole iteration
                sequenceOf(
                    availableNumberCalculator.add(a, b),
                    availableNumberCalculator.subtract(a, b),
                    availableNumberCalculator.multiply(a, b),
                ).filterNotNull()
            }
        }
    }

    /**
     * Calling this method on regular intervals allows the algorithm to be force-stopped.
     */
    private fun checkThreadInterrupted() {
        if (Thread.currentThread().isInterrupted) {
            throw InterruptedException()
        }
    }

}

