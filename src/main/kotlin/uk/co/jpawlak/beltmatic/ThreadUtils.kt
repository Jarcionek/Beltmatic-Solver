package uk.co.jpawlak.beltmatic

object ThreadUtils {

    /**
     * Calling this method on regular intervals allows the algorithm to be force-stopped.
     */
    fun checkThreadInterrupted() {
        if (Thread.currentThread().isInterrupted) {
            throw InterruptedException()
        }
    }

}
