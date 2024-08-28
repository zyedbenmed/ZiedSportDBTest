package ziedsportdb.test.fdjtest.core

sealed class FDJResult<out T> {
    data class Success<out T>(val data: T) : FDJResult<T>()
    data class Failure<T>(val error: Throwable?) : FDJResult<T>()
}
