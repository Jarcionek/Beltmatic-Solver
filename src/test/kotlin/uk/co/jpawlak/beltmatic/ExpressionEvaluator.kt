package uk.co.jpawlak.beltmatic

import javax.script.ScriptEngineManager

object ExpressionEvaluator {

    //TODO this does not support infix exponentiation operation, switch to Javaluator: https://javaluator.fathzer.com/en/doc/tutorial.php?chapter=extending
    @Suppress("MoveVariableDeclarationIntoWhen")
    fun evaluate(expression: String): Int {
        if (expression.contains("/")) {
            //TODO Need to handle integer division. I.e. "4 / 3 * 6" should 6, not 8.
            throw IllegalArgumentException("Division is not supported")
        }

        val engine = ScriptEngineManager().getEngineByName("js")
        val result = engine.eval(expression)

        // The result might be a Double or another numeric type, so we convert it to Int
        return when (result) {
            is Number -> result.toInt()
            else -> throw IllegalArgumentException("Expression did not evaluate to a number")
        }
    }

}
