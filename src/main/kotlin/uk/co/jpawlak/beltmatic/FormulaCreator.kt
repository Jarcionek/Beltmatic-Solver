package uk.co.jpawlak.beltmatic

import uk.co.jpawlak.beltmatic.Operation.EXPONENTIATION
import uk.co.jpawlak.beltmatic.Operation.SUBTRACTION

class FormulaCreator {

    fun createFormula(number: AvailableNumber): String {
        if (number.isInitialNumber()) {
            return "${number.number}"
        }

        val operation = number.operation!!
        val symbol = number.operation.symbol
        val leftFormula = createFormula(number.leftNumber!!)
        val rightFormula = createFormula(number.rightNumber!!)

        val leftSideNeedsParentheses = leftSideNeedsParentheses(outMostOperation(leftFormula), operation)
        val rightSideNeedsParentheses = rightSideNeedsParentheses(outMostOperation(rightFormula), operation)

        val leftFormulaWithParentheses = if (leftSideNeedsParentheses) "($leftFormula)" else leftFormula
        val rightFormulaWithParentheses = if (rightSideNeedsParentheses) "($rightFormula)" else rightFormula
        val operationWithSpacesAround = if (operation == EXPONENTIATION) symbol else " $symbol "

        return "$leftFormulaWithParentheses$operationWithSpacesAround$rightFormulaWithParentheses"
    }

    private fun leftSideNeedsParentheses(
        leftSubFormulaOperation: Operation?,
        operation: Operation
    ): Boolean {
        if (leftSubFormulaOperation != null) {
            if (leftSubFormulaOperation == EXPONENTIATION && operation == EXPONENTIATION) return true
            if (leftSubFormulaOperation.precedence < operation.precedence) return true
        }
        return false
    }

    private fun rightSideNeedsParentheses(
        rightSubFormulaOperation: Operation?,
        operation: Operation
    ): Boolean {
        if (rightSubFormulaOperation != null) {
            if (rightSubFormulaOperation == EXPONENTIATION && operation == EXPONENTIATION) return true
            if (rightSubFormulaOperation.precedence < operation.precedence) return true
            if (rightSubFormulaOperation.precedence == operation.precedence && operation == SUBTRACTION) return true
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
