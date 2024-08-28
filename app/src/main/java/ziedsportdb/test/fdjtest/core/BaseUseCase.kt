package ziedsportdb.test.fdjtest.core

interface BaseUseCase<out Type> {
    suspend operator fun invoke(): Type
}
