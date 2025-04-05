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

    //TODO extract it, replace this kdoc with unit tests.
    /**
     * This method is iterating over the same list twice, and there is no need to calculate both X * Y and Y * X.
     *
     * Therefore, the inner iteration starts from the current value of the outer iteration. I.e. instead of this:
     * ```
     * a = 1, b in [1, 2, 3]
     * a = 2, b in [1, 2, 3]
     * a = 3, b in [1, 2, 3]
     * ```
     *
     * It does this:
     * ```
     * a = 1, b in [1, 2, 3]
     * a = 2, b in    [2, 3]
     * a = 3, b in       [3]
     * ```
     *
     * However, this means that commutative operations have to be performed both ways. This method needs to return
     * both 2 - 3 and 3 - 2. There will be no iteration where a = 3 and b = 2, therefore it performs both operations when
     * a = 2 and b = 3.
     */
    private fun calculateNewAvailableNumbers(availableNumbers: List<AvailableNumber>): Sequence<AvailableNumber> {
        return availableNumbers.asSequence().flatMapIndexed { i, a ->
            checkThreadInterrupted()
            availableNumbers.asSequence().drop(i).flatMap { b ->
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
     * [listOne] and [listTwo] have no common elements. If number X is obtainable in 1 operation, it will not be returned
     * in a list of numbers obtainable in 2 operations.
     *
     * No shortcuts here. In particular, for commutative properties it needs to perform the operation both ways.
     */
    private fun calculateNewAvailableNumbers(listOne: List<AvailableNumber>, listTwo: List<AvailableNumber>): Sequence<AvailableNumber> {
        //TODO assert lists have no duplicates, and their intersection is empty
        return listOne.asSequence().flatMap { a ->
            checkThreadInterrupted()
            listTwo.asSequence().flatMap { b ->
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

