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

    private fun calculateNewAvailableNumbers(availableNumbers: List<AvailableNumber>): Sequence<AvailableNumber> {
        return availableNumbers.asSequence().flatMap { a ->
            if (Thread.currentThread().isInterrupted) {
                throw InterruptedException()
            }
            availableNumbers.asSequence().flatMap { b ->
                val newNumbers: MutableList<AvailableNumber?> = mutableListOf()
                //TODO optimize - even if the target number was found, this will continue the whole iteration
                newNumbers.add(availableNumberCalculator.add(a, b))
                newNumbers.add(availableNumberCalculator.subtract(a, b))
                newNumbers.add(availableNumberCalculator.multiply(a, b))
                if (b.number in 2..3) { //TODO without this restriction it runs out of heap
                    if (a.number < 65536) { // this is 2^31, square it, and we get MAX INT; for cube the limit is around 1625; for ^4 the limit is 256
                        newNumbers.add(availableNumberCalculator.power(a, b))
                    }
                }
                newNumbers.asSequence().filterNotNull()
            }
        }
    }

}

