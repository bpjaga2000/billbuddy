package presentation.register

import com.arkivanov.decompose.ComponentContext
import data.model.dto.UserDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import utils.DispatcherUtils
import utils.DispatcherUtils.componentCoroutineScope

interface RegisterComponent {
    fun onRegisterClicked(email: String, password: String)
    fun onRegistrationSuccessful()
    fun onLoginClicked()
}

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val onRegistrationSuccessful: () -> Unit,
    private val onLoginClicked: () -> Unit
) : RegisterComponent, ComponentContext by componentContext {
    private var _userRegisterResponse = MutableStateFlow<ApiResult<UserDto>?>(null)
    val userRegisterResponse = _userRegisterResponse.asStateFlow()

    private val coroutineScope = componentContext.componentCoroutineScope()
    override fun onRegisterClicked(email: String, password: String) {
        coroutineScope.launch {
            RepositoryImpl().register(email, password).collect{
                _userRegisterResponse.value = it
            }
        }
    }

    override fun onRegistrationSuccessful() {
        onRegistrationSuccessful.invoke()
    }

    override fun onLoginClicked() {
        onLoginClicked.invoke()
    }

}