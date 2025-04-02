package uk.co.jpawlak.beltmatic

class BeltmaticSolver {

    fun solve(availableNumbers: List<Int>, targetNumber: Int): String {
        val results: MutableMap<Int, Result> = availableNumbers.associateBy({ it }, { Result(it, 0, "$it") }).toMutableMap()

        for (i in 1..2) { //TODO extract to MAX_OPERATIONS constant; update exception message
            val newResults: MutableMap<Int, Result> = calculateNewResults(results)
            if (newResults.containsKey(targetNumber)) {
                return newResults[targetNumber]!!.formula
            }
            for (newResult in newResults) {
                val oldResult = results[newResult.key]
                if (oldResult == null || oldResult.numberOfOperations > newResult.value.numberOfOperations) {
                    results[newResult.key] = newResult.value
                }
            }
        }

        throw IllegalArgumentException("No formula found")
    }

    private fun calculateNewResults(results: Map<Int, Result>): MutableMap<Int, Result> {
        val newResults: MutableMap<Int, Result> =  mutableMapOf()
        for (a in results.values) {
            for (b in results.values) {
                val newResult = join(a, b)
                newResults[newResult.targetNumber] = newResult
                //TODO when inserting into the map, we need to pick the one with fewest operations - this code is already in the function above
            }
        }
        return newResults
    }

    private fun join(a: Result, b: Result): Result {
        return Result(
            a.targetNumber + b.targetNumber,
            a.numberOfOperations + b.numberOfOperations,
            "${a.formula} + ${b.formula}" //TODO will need parenthesis here
        )
    }

}

//TODO rename it - how to call it? AvailableNumber?
data class Result(
    val targetNumber: Int,
    val numberOfOperations: Int,
    val formula: String,
)
