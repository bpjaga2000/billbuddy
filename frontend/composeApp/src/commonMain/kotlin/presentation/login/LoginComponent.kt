package presentation.login

import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.set
import data.model.dto.UserDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope

interface LoginComponent {
    val userLoginResponse: StateFlow<ApiResult<UserDto>?>
    fun onLoginClicked(email: String, password: String)
    fun onRegisterClicked()
}

class DefaultLoginComponent(
    private val componentContext: ComponentContext,
    private val onLoginSuccessful: () -> Unit,
    private val onRegisterClicked: () -> Unit
) : LoginComponent, ComponentContext by componentContext {

    private var _userLoginResponse = MutableStateFlow<ApiResult<UserDto>?>(null)
    override val userLoginResponse = _userLoginResponse.asStateFlow()

    private val coroutineScope = componentContext.componentCoroutineScope()
    override fun onLoginClicked(email: String, password: String) {
        _userLoginResponse.value = ApiResult.loading()
        coroutineScope.launch(Dispatchers.IO) {
            RepositoryImpl().logIn(email, password).collect {
                when (it) {
                    is ApiResult.Success -> {
                        withContext(Dispatchers.Main) {
                            _userLoginResponse.value = it
                            DataStore.settings["token"] = it.data.token
                            DataStore.settings["email"] = it.data.email
                            DataStore.settings["id"] = it.data.id
                            onLoginSuccessful()
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onRegisterClicked() {
        onRegisterClicked.invoke()
    }

}