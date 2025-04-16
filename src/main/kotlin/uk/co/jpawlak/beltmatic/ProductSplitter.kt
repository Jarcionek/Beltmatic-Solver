package uk.co.jpawlak.beltmatic

/**
 * Finds all available [Product]s for a given operations limit.
 *
 * For example, if operations limit is 3, it will produce [Product]s `(0, 2)` and `(1, 1)`.
 *
 * @see Product
 * @see AvailableNumbersCombiner
 */
class ProductSplitter {

    fun split(operationsLimit: Int): Sequence<Product> = sequence {
        val maxOperationCount = operationsLimit - 1

        for (firstOperationCount in 0..maxOperationCount / 2) {
            val secondOperationCount = maxOperationCount - firstOperationCount
            yield(Product(firstOperationCount, secondOperationCount))
        }
    }

}
