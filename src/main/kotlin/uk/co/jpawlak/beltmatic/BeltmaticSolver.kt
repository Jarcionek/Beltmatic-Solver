package uk.co.jpawlak.beltmatic

class BeltmaticSolver {

    fun solve(availableNumbers: List<Int>, targetNumber: Int): String {
        val results: MutableMap<Int, AvailableNumber> = availableNumbers.associateBy({ it }, { AvailableNumber(
            it,
            "$it",
            0
        ) }).toMutableMap()

        for (i in 1..2) { //TODO extract to MAX_OPERATIONS constant; update exception message
            val newResults: MutableMap<Int, AvailableNumber> = calculateNewResults(results)
            if (newResults.containsKey(targetNumber)) {
                return newResults[targetNumber]!!.formula
            }
            for (newResult in newResults) {
                val oldResult = results[newResult.key]
                if (oldResult == null || oldResult.formulaOperationsCount > newResult.value.formulaOperationsCount) {
                    results[newResult.key] = newResult.value
                }
            }
        }

        throw IllegalArgumentException("No formula found")
    }

    private fun calculateNewResults(results: Map<Int, AvailableNumber>): MutableMap<Int, AvailableNumber> {
        val newResults: MutableMap<Int, AvailableNumber> =  mutableMapOf()
        for (a in results.values) {
            for (b in results.values) {
                val newResult = join(a, b)
                newResults[newResult.number] = newResult
                //TODO when inserting into the map, we need to pick the one with fewest operations - this code is already in the function above
            }
        }
        return newResults
    }

    private fun join(a: AvailableNumber, b: AvailableNumber): AvailableNumber {
        return AvailableNumber(
            a.number + b.number,
            "${a.formula} + ${b.formula}", //TODO will need parenthesis here
            a.formulaOperationsCount + b.formulaOperationsCount
        )
    }

}

data class AvailableNumber(
    val number: Int,
    val formula: String,
    val formulaOperationsCount: Int,
)
