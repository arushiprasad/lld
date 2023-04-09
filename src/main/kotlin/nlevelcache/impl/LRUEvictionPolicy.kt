package nlevelcache.impl

import nlevelcache.IEvictionPolicy
import java.util.LinkedList

class LRUEvictionPolicy<K>(
    val size: Int
) : IEvictionPolicy<K> {

    private val map = mutableMapOf<K, Int>()
    private val queue = LinkedList<K>()
    override fun usedKey(key: K) {
        map[key]?.let {
            queue.removeAt(it)
        }
        queue.add(key)
        map[key] = queue.size - 1
    }

    override fun evictKey(): K {
        val toRemove = queue.remove()
        map.remove(toRemove)
        return toRemove
    }
}