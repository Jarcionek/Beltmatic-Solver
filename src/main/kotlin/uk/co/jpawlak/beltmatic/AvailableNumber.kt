package uk.co.jpawlak.beltmatic

@Suppress("DataClassPrivateConstructor")
data class AvailableNumber private constructor(
    val number: Int,
    val operation: Operation?,
    val leftNumber: AvailableNumber?,
    val rightNumber: AvailableNumber?,
    val formulaOperationsCount: Int,
    val formulaOperationsCost: Int,
) {

    companion object {

        fun initialNumber(number: Int): AvailableNumber {
            return AvailableNumber(
                number = number,
                operation = null,
                leftNumber = null,
                rightNumber = null,
                formulaOperationsCount = 0,
                formulaOperationsCost = 0
            )
        }

        fun availableNumber(
            resultNumber: Int,
            operation: Operation,
            leftNumber: AvailableNumber,
            rightNumber: AvailableNumber
        ): AvailableNumber {
            return AvailableNumber(
                number = resultNumber,
                operation = operation,
                leftNumber = leftNumber,
                rightNumber = rightNumber,
                formulaOperationsCount = leftNumber.formulaOperationsCount + rightNumber.formulaOperationsCount + 1,
                formulaOperationsCost = leftNumber.formulaOperationsCost + rightNumber.formulaOperationsCost + operation.cost
            )
        }

    }

    fun isInitialNumber() = operation == null

}
