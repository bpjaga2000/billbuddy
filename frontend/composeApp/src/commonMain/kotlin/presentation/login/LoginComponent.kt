package presentation.login

import com.arkivanov.decompose.ComponentContext
import data.model.dto.UserDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import utils.DispatcherUtils.componentCoroutineScope

interface LoginComponent {
    val userLoginResponse: StateFlow<ApiResult<UserDto>?>

    fun onLoginClicked(email: String, password: String)
    fun onLoginSuccessful()
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
        coroutineScope.launch {
            RepositoryImpl().logIn(email, password).collect {
                _userLoginResponse.value = it
            }
        }
    }

    override fun onLoginSuccessful() {
        onLoginSuccessful.invoke()
    }

    override fun onRegisterClicked() {
        onRegisterClicked.invoke()
    }

}