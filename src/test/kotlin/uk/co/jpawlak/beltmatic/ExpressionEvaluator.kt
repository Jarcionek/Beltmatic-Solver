package uk.co.jpawlak.beltmatic

import com.fathzer.soft.javaluator.BracketPair
import com.fathzer.soft.javaluator.DoubleEvaluator
import com.fathzer.soft.javaluator.DoubleEvaluator.DIVIDE
import com.fathzer.soft.javaluator.DoubleEvaluator.EXPONENT
import com.fathzer.soft.javaluator.Operator
import com.fathzer.soft.javaluator.Parameters
import kotlin.math.pow

private const val SCIENTIFIC_NOTATION_NOT_SUPPORTED = false

object ExpressionEvaluator {

    /**
     * This is a workaround for a [Javaluator bug](https://github.com/fathzer/javaluator/issues/13).
     */
    private val RIGHT_EXPONENT: Operator = Operator(EXPONENT.symbol, EXPONENT.operandCount, Operator.Associativity.RIGHT, EXPONENT.precedence)

    private val INTEGER_DIVIDE: Operator = Operator(DIVIDE.symbol, DIVIDE.operandCount, DIVIDE.associativity, DIVIDE.precedence)

    private val evaluator: DoubleEvaluator = object : DoubleEvaluator(
        Parameters().apply {
            add(RIGHT_EXPONENT)
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
            if (operator == RIGHT_EXPONENT) {
                return operands!!.next().pow(operands.next())
            }
            if (operator == INTEGER_DIVIDE) {
                return (operands!!.next().toInt() / operands.next().toInt()).toDouble()
            }
            return super.evaluate(operator, operands, evaluationContext)
        }

    }

    fun evaluate(expression: String): Int {
        return evaluator.evaluate(expression).toInt()
    }

}
