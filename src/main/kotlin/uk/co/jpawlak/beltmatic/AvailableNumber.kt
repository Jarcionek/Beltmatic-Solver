package uk.co.jpawlak.beltmatic

@Suppress("DataClassPrivateConstructor")
data class AvailableNumber private constructor(
    val number: Int,
    val operation: Operation?,
    val sourceNumberOne: AvailableNumber?,
    val sourceNumberTwo: AvailableNumber?,
    val formulaOperationsCount: Int,
) {

    companion object {

        fun initialNumber(number: Int): AvailableNumber {
            return AvailableNumber(
                number = number,
                operation = null,
                sourceNumberOne = null,
                sourceNumberTwo = null,
                formulaOperationsCount = 0
            )
        }

        fun availableNumber(
            resultNumber: Int,
            operation: Operation,
            sourceNumberOne: AvailableNumber,
            sourceNumberTwo: AvailableNumber
        ): AvailableNumber {
            return AvailableNumber(
                number = resultNumber,
                operation = operation,
                sourceNumberOne = sourceNumberOne,
                sourceNumberTwo = sourceNumberTwo,
                formulaOperationsCount = sourceNumberOne.formulaOperationsCount + sourceNumberTwo.formulaOperationsCount + 1
            )
        }

    }

    fun isInitialNumber() = operation == null

}
