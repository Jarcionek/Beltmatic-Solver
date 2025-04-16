package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.Config.OPERATIONS_LIMIT

class BeltmaticSolver(
    private val allAvailableNumbers: AvailableNumbers,
    private val productSplitter: ProductSplitter,
    private val combiner: AvailableNumbersCombiner,
    private val formulaCreator: FormulaCreator,
) {

    fun solve(initiallyAvailableNumbers: List<Int>, targetNumber: Int): String {
        allAvailableNumbers.reset(initiallyAvailableNumbers)

        for (operationsLimit in 1 .. OPERATIONS_LIMIT) {

            productSplitter.split(operationsLimit)
                .flatMap { combiner.calculateNewAvailableNumbers(it) }
                .forEach { allAvailableNumbers.addIfBetter(it) }

            //TODO optimisation - we wait for the whole iteration before returning a solution
            // however it will be the best solution, e.g. 3+4 instead of 8-1 (see Operation preference)
            // we could inject a listener to notify about the first find, but keep searching for better solutions
            allAvailableNumbers.get(targetNumber)?.let {
                return@solve formulaCreator.createFormula(it)
            }
        }

        throw IllegalArgumentException("Could not find a formula to get $targetNumber using no more than $OPERATIONS_LIMIT operations")
    }

}

