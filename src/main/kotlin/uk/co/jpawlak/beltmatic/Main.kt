package uk.co.jpawlak.beltmatic

fun main() {
    val availableNumbers = ((1..9) + (11..24)).toList() // 25 26 27 28 29 30 32 33 34 35 36 37 38 39 39 40 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 60 61 62 64 65 66 68 69 70 71 72 73 74 76 77 78 79 81 83 84 87 89 90 91 92 93 94 96 98 101 103 104 105 106 111 114 117 124 126 130 135 140 ... 991

    val result = BeltmaticSolver().solve(availableNumbers, 8339)

    println(result)
}
