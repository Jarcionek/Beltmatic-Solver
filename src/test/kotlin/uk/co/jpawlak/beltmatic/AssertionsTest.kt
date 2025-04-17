package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * Disable assertions with `-da` JVM argument in `build.gradle.kts` to speed up the tests.
 */
class AssertionsTest {

    @Test
    fun `assertions are enabled`() {
        assertTrue(javaClass.desiredAssertionStatus())
    }

}
