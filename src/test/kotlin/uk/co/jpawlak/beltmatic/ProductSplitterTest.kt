package uk.co.jpawlak.beltmatic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProductSplitterTest {

    private val productSplitter = ProductSplitter()

    @Test
    fun `returns products for operations limit 1`() {
        val products = productSplitter.split(1).toList()
        
        assertEquals(listOf(Product(0, 0)), products)
    }

    @Test
    fun `returns products for operations limit 2`() {
        val products = productSplitter.split(2).toList()
        
        assertEquals(listOf(Product(0, 1)), products)
    }

    @Test
    fun `returns products for operations limit 3`() {
        val products = productSplitter.split(3).toList()
        
        assertEquals(listOf(Product(0, 2), Product(1, 1)), products)
    }

    @Test
    fun `returns products for operations limit 4`() {
        val products = productSplitter.split(4).toList()
        
        assertEquals(listOf(Product(0, 3), Product(1, 2)), products)
    }

    @Test
    fun `returns products for operations limit 5`() {
        val products = productSplitter.split(5).toList()
        
        assertEquals(listOf(Product(0, 4), Product(1, 3), Product(2, 2)), products)
    }
}
