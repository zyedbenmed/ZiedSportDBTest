package ziedsportdb.test.fdjtest.core

interface DomainModelMapper<T : Any> {
    fun mapToDomainModel(): T
}
