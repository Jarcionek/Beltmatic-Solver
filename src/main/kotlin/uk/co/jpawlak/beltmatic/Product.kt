package uk.co.jpawlak.beltmatic

/**
 * An instruction that defines what numbers to use to produce new numbers.
 *
 * For example, if [operationCountOne] = 1 and [operationCountTwo] = 2, it instructs to take all
 * available numbers obtainable in 1 operation, and all available numbers obtainable in 2 operations,
 * and perform all possible operations on these two lists. This means that all newly produced numbers
 * will have operation count equal 3.
 *
 * @see AvailableNumbersCombiner
 */
data class Product(
    val operationCountOne: Int,
    val operationCountTwo: Int,
)
