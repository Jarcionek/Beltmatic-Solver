package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.Operation.EXPONENTIATION
import uk.co.jpawlak.beltmatic.Operation.SUBTRACTION

class FormulaCreator {

    fun createFormula(targetNumber: AvailableNumber): String {
        val formula = internalCreateFormula(targetNumber)

        return formula
    }

    private fun internalCreateFormula(number: AvailableNumber): String {
        if (number.isInitialNumber()) {
            return "${number.number}"
        }

        val operation = number.operation!!
        val symbol = number.operation.symbol
        //TODO rename a/b and one/two to left/right
        val a = createFormula(number.sourceNumberOne!!)
        val b = createFormula(number.sourceNumberTwo!!)

        val outMostOperationOfA = outMostOperation(a)
        val outMostOperationOfB = outMostOperation(b)

        val aNeedsParentheses = leftSideNeedsParentheses(outMostOperationOfA, operation)
        val bNeedsParentheses = rightSideNeedsParentheses(outMostOperationOfB, operation)

        val aSubFormula = if (aNeedsParentheses) "($a)" else a
        val bSubFormula = if (bNeedsParentheses) "($b)" else b
        val symbolSubFormula = if (operation == EXPONENTIATION) symbol else " $symbol "

        val result = "$aSubFormula$symbolSubFormula$bSubFormula"
        return result
    }

    private fun leftSideNeedsParentheses(
        outMostOperationOfA: Operation?,
        operation: Operation
    ): Boolean {
        if (outMostOperationOfA != null) {
            if (outMostOperationOfA == EXPONENTIATION && operation == EXPONENTIATION) return true
            if (outMostOperationOfA.precedence < operation.precedence) return true
        }
        return false
    }

    private fun rightSideNeedsParentheses(
        outMostOperationOfB: Operation?,
        operation: Operation
    ): Boolean {
        if (outMostOperationOfB != null) {
            if (outMostOperationOfB == EXPONENTIATION && operation == EXPONENTIATION) return true
            if (outMostOperationOfB.precedence < operation.precedence) return true
            if (outMostOperationOfB.precedence == operation.precedence && operation == SUBTRACTION) return true
        }
        return false
    }

    //TODO extract it and unit test it!
    /**
     * Returns the out most operation in the formula. Returns the weakest linking operation (i.e. with
     * the lowest precedence) if two operations are found. Returns null for initial numbers (i.e. formulas
     * without any operations).
     *
     * Examples:
     * - `3 * (4 + 5)` -> `*`
     * - `3 + (4 * 5)` -> `+`
     * - `3 + 4 * 5` -> `+`
     * - `3 * 4 + 5` -> `+`
     * - `3 * 4` -> `*`
     * - `3` -> `null`
     */
    private fun outMostOperation(formula: String): Operation? {
        if (formula.matches(Regex("\\d+"))) {
            return null
        }

        val operationPattern = Regex("[-+/*^()]")
        val leftMostOperation: Char = operationPattern.find(formula)!!.value[0]
        val rightMostOperation: Char = operationPattern.findAll(formula).last().value[0]

        return sequenceOf(leftMostOperation, rightMostOperation)
            .filter { it != '(' && it != ')' }
            .map { Operation.fromSymbol(it) }
            .sortedBy { it.precedence }
            .first() // we are not expecting a call with formula like (3+4), let it throw
    }

}
