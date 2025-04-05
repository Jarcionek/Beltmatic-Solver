package uk.co.jpawlak.beltmatic

class BeltmaticSolver(
    private val availableNumberCalculator: AvailableNumberCalculator = AvailableNumberCalculator()
) {

    private lateinit var allAvailableNumbers: AvailableNumbers

    fun solve(initiallyAvailableNumbers: List<Int>, targetNumber: Int): String {
        allAvailableNumbers = AvailableNumbers(initiallyAvailableNumbers)

        // find all numbers obtainable with 1 operation

        calculateExponentiationsOnly(
            allAvailableNumbers.getAll()
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 2 operations

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(1)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 3 operations

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(2)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(1)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 4 operations

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(3)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(1),
            allAvailableNumbers.getAllWithOperationCountEqualTo(2)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve it.formula
        }

        // find all numbers obtainable with 5 operations

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(0),
            allAvailableNumbers.getAllWithOperationCountEqualTo(4)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        calculateNewAvailableNumbers(
            allAvailableNumbers.getAllWithOperationCountEqualTo(1),
            allAvailableNumbers.getAllWithOperationCountEqualTo(3)
        ).forEach { allAvailableNumbers.addIfBetter(it) }

        calculateNewAvailableNumbers(
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

    private fun calculateNewAvailableNumbers(listOne: List<AvailableNumber>, listTwo: List<AvailableNumber>): Sequence<AvailableNumber> {
        return listOne.asSequence().flatMap { a ->
            checkThreadInterrupted()
            listTwo.asSequence().flatMap { b ->
                //TODO optimize - even if the target number was found, this will continue the whole iteration
                sequenceOf(
                    availableNumberCalculator.multiply(a, b),
                    availableNumberCalculator.add(a, b),
                    availableNumberCalculator.subtract(a, b),
                    availableNumberCalculator.subtract(b, a),
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

