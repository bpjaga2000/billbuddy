package data.remote

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import multiplatform.network.cmptoast.ToastGravity
import multiplatform.network.cmptoast.showToast

sealed class ApiResult<T> {

    class Loading<T> : ApiResult<T>()
    class Success<T>(val data: T) : ApiResult<T>()
    class Error<T>(val message: String? = null) : ApiResult<T>() {
        init {
            message?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    showToast(message, ToastGravity.Bottom, Color(100, 0, 0))
                }
            }
        }
    }

    companion object {
        fun <T> loading() = Loading<T>()

        fun <T> success(data: T) = Success(data)

        fun <T> error(message: String?) = Error<T>(message)
    }
}