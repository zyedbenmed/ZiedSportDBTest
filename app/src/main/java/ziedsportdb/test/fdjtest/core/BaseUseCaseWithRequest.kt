package ziedsportdb.test.fdjtest.core

interface BaseUseCaseWithRequest<in Request, out Type> {
    suspend operator fun invoke(request: Request): Type
}
