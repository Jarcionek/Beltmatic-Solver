package uk.co.jpawlak.beltmatic

class BeltmaticSolver {

    fun solve(availableNumbers: List<Int>, targetNumber: Int): String {
        for (a in availableNumbers) {
            for (b in availableNumbers) {
                if (a + b == targetNumber) {
                    return "$a + $b"
                }
            }
        }
        throw IllegalArgumentException("No formula found")
    }

}
