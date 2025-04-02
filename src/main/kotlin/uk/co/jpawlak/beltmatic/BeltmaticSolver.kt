package uk.co.jpawlak.beltmatic

class BeltmaticSolver {

    fun solve(availableNumbers: List<Int>, targetNumber: Int): String {
        val results: MutableMap<Int, Result> = availableNumbers.associateBy({ it }, { Result(it, 0, "$it") }).toMutableMap()

        val newResults: MutableMap<Int, Result> = mutableMapOf()
        for (a in results.values) {
            for (b in results.values) {
                val newResult = join(a, b)
                if (newResult.targetNumber == targetNumber) {
                    return newResult.formula
                }
                if (!newResults.containsKey(newResult.targetNumber)) {
                    newResults[newResult.targetNumber] = newResult
                }
            }
        }
        throw IllegalArgumentException("No formula found")
    }

    private fun join(a: Result, b: Result): Result {
        return Result(
            a.targetNumber + b.targetNumber,
            a.numberOfOperations + b.numberOfOperations,
            "${a.formula} + ${b.formula}"
        )
    }

}

data class Result(
    val targetNumber: Int,
    val numberOfOperations: Int,
    val formula: String,
)
