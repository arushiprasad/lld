package nlevelcache.impl

import nlevelcache.ICache
import nlevelcache.IEvictionPolicy
class MultiLevelCache<K, V>(
    override val evictionPolicy: IEvictionPolicy<K>,
    override val size: Int,
    val next: ICache<K, V>?
) : ICache<K, V> {

    private val map = mutableMapOf<K, V>()
    override fun set(key: K, value: V) {
        val containsKey = map[key]
        if (containsKey == value) {
            evictionPolicy.usedKey(key)
            return
        }
        if (map.size == size) {
            val toRemove = evictionPolicy.evictKey()
            map.remove(toRemove)
        }
        map[key] = value
        evictionPolicy.usedKey(key)
        next?.set(key, value)
    }

    override fun get(key: K): V? {
        val found = map[key]
        if (found == null) {
            val nextHas = next?.get(key)
            if (nextHas != null) {
                map[key] = nextHas
            }
        }
        evictionPolicy.usedKey(key)
        return map[key]
    }
}