package stu.chandan

fun <T> Sequence<T>.asCachedListIterator() = CachedListIterator(this)

class CachedListIterator<T>(sequence: Sequence<T>) : ListIterator<T> {
    private val cachedList = mutableListOf<T>()
    val iterator = sequence.iterator()
    private var index = 0
    override fun hasNext(): Boolean {
        return when {
            index < cachedList.size -> true
            else -> iterator.hasNext()
        }
    }

    override fun hasPrevious(): Boolean {
        return index > 0
    }

    override fun next(): T {
        return when {
            index < cachedList.size -> cachedList[index++]
            else -> iterator.next().also {
                cachedList.add(it)
                index++
            }
        }
    }

    override fun nextIndex(): Int {
        return index
    }

    override fun previous(): T {
        return when {
            index <= 0 -> throw NoSuchElementException()
            else -> cachedList[--index]
        }
    }

    override fun previousIndex(): Int {
        return index - 1
    }

    operator fun get(index: Int): T {
        return when {
            index < cachedList.size -> cachedList[index]
            else -> {
                val left = index - cachedList.size + 1
                asSequence().take(left).count()
                cachedList[index]
            }
        }
    }
}