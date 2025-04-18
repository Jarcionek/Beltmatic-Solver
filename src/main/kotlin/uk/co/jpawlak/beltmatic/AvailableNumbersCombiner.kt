package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.ThreadUtils.checkThreadInterrupted

/**
 * Combines two lists of [AvailableNumber]s to produce a new list of [AvailableNumber]s.
 *
 * @see Product
 */
class AvailableNumbersCombiner(
    private val allAvailableNumbers: AvailableNumbers,
    private val calculator: AvailableNumberCalculator,
) {

    fun calculateNewAvailableNumbers(product: Product): Sequence<AvailableNumber> {
        if (product.operationCountOne == product.operationCountTwo) {
            val list = allAvailableNumbers.getAllWithOperationCountEqualTo(product.operationCountOne)
            return calculateNewAvailableNumbers(list)
        } else {
            val listOne = allAvailableNumbers.getAllWithOperationCountEqualTo(product.operationCountOne)
            val listTwo = allAvailableNumbers.getAllWithOperationCountEqualTo(product.operationCountTwo)
            return calculateNewAvailableNumbers(listOne, listTwo)
        }
    }

    //TODO add unit tests
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
                    calculator.multiply(a, b),
                    calculator.add(a, b),
                    calculator.power(a, b),
                    calculator.power(b, a),
                    calculator.subtract(a, b),
                    calculator.subtract(b, a),
                )
                    .plus(calculator.divide(a, b))
                    .plus(calculator.divide(b, a))
                    .filterNotNull()
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
        assert(listOne.size == listOne.distinct().size) {
            "List one contains duplicates"
        }

        assert(listTwo.size == listTwo.distinct().size) {
            "List two contains duplicates"
        }

        assert(listOne.intersect(listTwo.toSet()).isEmpty()) {
            "Lists have common elements: ${listOne.intersect(listTwo.toSet())}"
        }

        return listOne.asSequence().flatMap { a ->
            checkThreadInterrupted()
            listTwo.asSequence().flatMap { b ->
                sequenceOf(
                    calculator.multiply(a, b),
                    calculator.add(a, b),
                    calculator.power(a, b),
                    calculator.power(b, a),
                    calculator.subtract(a, b),
                    calculator.subtract(b, a),
                )
                    .plus(calculator.divide(a, b))
                    .plus(calculator.divide(b, a))
                    .filterNotNull()
            }
        }
    }
}
