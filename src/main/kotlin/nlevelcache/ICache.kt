package nlevelcache

interface ICache<K, V> {

    val size: Int
    val evictionPolicy: IEvictionPolicy<K>
    fun set(key: K, value: V)
    fun get(key: K): V?
}