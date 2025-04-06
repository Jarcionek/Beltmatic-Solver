package uk.co.jpawlak.beltmatic

object BeltmaticSolverFactory {

    fun create(): BeltmaticSolver {
        val calculator = AvailableNumberCalculator()
        val allAvailableNumbers = AvailableNumbers()
        val combiner = AvailableNumbersCombiner(allAvailableNumbers, calculator)
        return BeltmaticSolver(calculator, allAvailableNumbers, combiner)
    }

}
