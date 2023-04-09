package nlevelcache.impl

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import nlevelcache.IEvictionPolicy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class MultiLevelCacheTest {

    private lateinit var evictionPolicy1: IEvictionPolicy<String>
    private lateinit var evictionPolicy2: IEvictionPolicy<String>
    private lateinit var cache1: MultiLevelCache<String, String>
    private lateinit var cache2: MultiLevelCache<String, String>

    @BeforeEach
    fun init() {
        evictionPolicy1 = mockk()
        evictionPolicy2 = mockk()
        cache2 = MultiLevelCache(evictionPolicy2, 10, null)
        cache1 = MultiLevelCache(evictionPolicy1, 5, cache2)
    }

    @AfterEach
    fun destroyMocks() {
        clearAllMocks()
    }

    @Test
    fun `key is evicted`() {
        every { evictionPolicy1.evictKey() } returns "a"

        every { evictionPolicy1.usedKey(any()) } just runs
        every { evictionPolicy2.usedKey(any()) } just runs
        cache1.set("a", "1")
        cache1.set("b", "2")
        cache1.set("c", "3")
        cache1.set("d", "4")
        cache1.set("e", "5")
        cache1.set("f", "5")

        verify(exactly = 1) { evictionPolicy1.evictKey() }
        verify(exactly = 6) { evictionPolicy1.usedKey(any()) }
        verify(exactly = 6) { evictionPolicy2.usedKey(any()) }
    }

    @Test
    fun `second cache returns result when first is empty`() {
        every { evictionPolicy1.evictKey() } returns "a"

        every { evictionPolicy1.usedKey(any()) } just runs
        every { evictionPolicy2.usedKey(any()) } just runs
        cache1.set("a", "1")
        cache1.set("b", "2")
        cache1.set("c", "3")
        cache1.set("d", "4")
        cache1.set("e", "5")
        cache1.set("f", "5")

        cache1.get("a")

        // one is while setting, one while fetching
        verify(exactly = 2) {
            evictionPolicy1.usedKey("a")
        }
        verify(exactly = 2) {
            evictionPolicy2.usedKey("a")
        }

        // now the first level cache should have "a" again,
        // so second cache should not be called again, count should be same

        cache1.get("a")

        verify(exactly = 3) {
            evictionPolicy1.usedKey("a")
        }
        verify(exactly = 2) {
            evictionPolicy2.usedKey("a")
        }
    }

    @Test
    fun `when item exists with same value in cache, it is not passed down`() {
        every { evictionPolicy1.evictKey() } returns "a"

        every { evictionPolicy1.usedKey(any()) } just runs
        every { evictionPolicy2.usedKey(any()) } just runs

        cache1.set("a", "1")

        // called while setting
        verify(exactly = 1) {
            evictionPolicy1.usedKey("a")
        }
        verify(exactly = 1) {
            evictionPolicy2.usedKey("a")
        }

        cache1.get("a")

        // verify second cache was not called because first had it
        verify(exactly = 2) {
            evictionPolicy1.usedKey("a")
        }
        verify(exactly = 1) {
            evictionPolicy2.usedKey("a")
        }
    }
}