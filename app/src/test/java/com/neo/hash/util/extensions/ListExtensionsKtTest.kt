package com.neo.hash.util.extensions

import com.neo.hash.model.Hash
import org.junit.Assert
import org.junit.Test

internal class ListExtensionsKtTest {

    @Test
    fun `mostRecurring is correct`() {
        val blocks = listOf(
            Hash.Block(1, 1),
            Hash.Block(2, 2),
            Hash.Block(3, 3),
            Hash.Block(1, 1),
        )

        val result = listOf(
            Hash.Block(1, 1)
        )

        Assert.assertEquals(result, blocks.recurring())
    }
}