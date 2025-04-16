package uk.co.jpawlak.beltmatic

class BeltmaticSolver(
    private val allAvailableNumbers: AvailableNumbers,
    private val combiner: AvailableNumbersCombiner,
    private val formulaCreator: FormulaCreator,
) {

    fun solve(initiallyAvailableNumbers: List<Int>, targetNumber: Int): String {
        allAvailableNumbers.reset(initiallyAvailableNumbers)

        // find all numbers obtainable with 1 operation

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 0))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve formulaCreator.createFormula(it)
        }

        // find all numbers obtainable with 2 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 1))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve formulaCreator.createFormula(it)
        }

        // find all numbers obtainable with 3 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 2)),
            combiner.calculateNewAvailableNumbers(Product(1, 1))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve formulaCreator.createFormula(it)
        }

        // find all numbers obtainable with 4 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 3)),
            combiner.calculateNewAvailableNumbers(Product(1, 2))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve formulaCreator.createFormula(it)
        }

        // find all numbers obtainable with 5 operations

        sequenceOf(
            combiner.calculateNewAvailableNumbers(Product(0, 4)),
            combiner.calculateNewAvailableNumbers(Product(1, 3)),
            combiner.calculateNewAvailableNumbers(Product(2, 2))
        ).flatMap { it }
            .forEach { allAvailableNumbers.addIfBetter(it) }

        allAvailableNumbers.get(targetNumber)?.let {
            return@solve formulaCreator.createFormula(it)
        }

        throw IllegalArgumentException("Could not find a formula to get $targetNumber using no more than 5 operations")
    }

}

