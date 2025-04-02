package uk.co.jpawlak.beltmatic

class BeltmaticSolver {

    fun solve(availableNumbers: List<Int>, targetNumber: Int): String {
        val results: MutableMap<Int, Result> = availableNumbers.associateBy({ it }, { Result(it, 0, "$it") }).toMutableMap()

        val newResults: MutableMap<Int, Result> = calculateNewResults(results)
        if (newResults.containsKey(targetNumber)) {
            return newResults[targetNumber]!!.formula
        }

        throw IllegalArgumentException("No formula found")
    }

    private fun calculateNewResults(results: MutableMap<Int, Result>): MutableMap<Int, Result> {
        val newResults: MutableMap<Int, Result> =  mutableMapOf()
        for (a in results.values) {
            for (b in results.values) {
                val newResult = join(a, b)
                newResults[newResult.targetNumber] = newResult
                //TODO when inserting into the map, we need to pick the one with fewest operations
            }
        }
        return newResults
    }

    private fun join(a: Result, b: Result): Result {
        return Result(
            a.targetNumber + b.targetNumber,
            a.numberOfOperations + b.numberOfOperations,
            "${a.formula} + ${b.formula}"
        )
    }

}

//TODO rename it - how to call it? AvailableNumber?
data class Result(
    val targetNumber: Int,
    val numberOfOperations: Int,
    val formula: String,
)
