package nlevelcache

// this should basically determine what element should be evicted
// this should also be informed if an element is added
interface IEvictionPolicy<K> {

    fun usedKey(key: K)

    fun evictKey(): K
}