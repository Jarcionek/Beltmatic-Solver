package uk.co.jpawlak.beltmatic

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.SECONDS

object Config {
    /**
     * The solver will never create numbers bigger than this limit, or lower than negative value of this limit.
     *
     * Increasing this limit might result in finding a solution with fewer operations, however it will take
     * longer to find any solution.
     */
    const val NUMBER_LIMIT = 1_000_000

    /**
     * The solver will try to find the solution with the fewest possible operations. If it cannot find
     * a solution with N operations, it will search for different solutions that use N+1 operations.
     *
     * If it cannot find a solution with a number of operations lower or equal to this limit, an exception
     * will be thrown.
     *
     * Increasing this limit does not affect performance. It simply defines how long to keep searching
     * before giving up.
     */
    const val OPERATIONS_LIMIT = 5
}

fun main() {
    val assertionsEnabled = object {}.javaClass.enclosingClass.desiredAssertionStatus()
    println("Assertions are ${if (assertionsEnabled) "enabled" else "disabled"}")

    val availableNumbers = ((1..9) + (11..24)).toList() // 25 26 27 28 29 30 32 33 34 35 36 37 38 39 39 40 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 60 61 62 64 65 66 68 69 70 71 72 73 74 76 77 78 79 81 83 84 87 89 90 91 92 93 94 96 98 101 103 104 105 106 111 114 117 124 126 130 135 140 ... 991

    val start = LocalDateTime.now()
    val result = BeltmaticSolverFactory.create().solve(availableNumbers, 8339)
    val end = LocalDateTime.now()

    println(result)
    println("Solved in ${start.until(end, SECONDS)} seconds.")
}
