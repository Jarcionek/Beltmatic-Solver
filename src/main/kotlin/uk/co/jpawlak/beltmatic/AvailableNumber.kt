package uk.co.jpawlak.beltmatic

data class AvailableNumber(
    val number: Int,
    val operation: Operation?,
    val formula: String,
    val formulaOperationsCount: Int,
)
