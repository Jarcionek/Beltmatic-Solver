package uk.co.jpawlak.beltmatic

import com.fathzer.soft.javaluator.BracketPair
import com.fathzer.soft.javaluator.DoubleEvaluator
import com.fathzer.soft.javaluator.DoubleEvaluator.DIVIDE
import com.fathzer.soft.javaluator.Operator
import com.fathzer.soft.javaluator.Parameters


private const val SCIENTIFIC_NOTATION_NOT_SUPPORTED = false

object ExpressionEvaluator {

    private val INTEGER_DIVIDE: Operator = Operator(DIVIDE.symbol, DIVIDE.operandCount, DIVIDE.associativity, DIVIDE.precedence)

    private val evaluator: DoubleEvaluator = object : DoubleEvaluator(
        Parameters().apply {
            add(EXPONENT)
            add(MULTIPLY)
            add(INTEGER_DIVIDE)
            add(PLUS)
            add(MINUS)
            addExpressionBracket(BracketPair.PARENTHESES)
        },
        SCIENTIFIC_NOTATION_NOT_SUPPORTED
    ) {

        override fun evaluate(
            operator: Operator?,
            operands: MutableIterator<Double>?,
            evaluationContext: Any?
        ): Double {
            return if (operator == INTEGER_DIVIDE) {
                (operands!!.next().toInt() / operands.next().toInt()).toDouble()
            } else {
                super.evaluate(operator, operands, evaluationContext)
            }
        }

    }

    fun evaluate(expression: String): Int {
        return evaluator.evaluate(expression).toInt()
    }

}
