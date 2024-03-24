package data.remote

sealed class ApiResult<T> {

    class Loading<T> : ApiResult<T>()
    class Success<T>(val data : T) : ApiResult<T>()
    class Error<T>(val message : String? = null) : ApiResult<T>()

    companion object {
        fun <T> loading() = Loading<T>()

        fun <T> success(data : T) = Success(data)

        fun <T> error(message : String?) = Error<T>(message)
    }
}