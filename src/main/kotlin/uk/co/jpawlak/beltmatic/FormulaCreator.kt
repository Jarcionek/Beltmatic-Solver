package uk.co.jpawlak.beltmatic

class FormulaCreator {

    fun createFormula(targetNumber: AvailableNumber): String {
        return if (targetNumber.isInitialNumber()) {
            "${targetNumber.number}"
        } else {
            val symbol = targetNumber.operation!!.symbol
            val a = createFormula(targetNumber.sourceNumberOne!!)
            val b = createFormula(targetNumber.sourceNumberTwo!!)
            "($a $symbol $b)"
        }
    }

}
