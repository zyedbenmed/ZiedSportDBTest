package ziedsportdb.test.fdjtest.core

interface DataBaseEntityMapper<T : Any> {
    fun mapToDbEntity(): T
}
