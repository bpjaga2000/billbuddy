package presentation.register

import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.set
import data.model.dto.UserDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import utils.DataStore
import utils.DispatcherUtils
import utils.DispatcherUtils.componentCoroutineScope

interface RegisterComponent {
    fun onRegisterClicked(email: String, password: String)
    fun onLoginClicked()
    val userRegisterResponse: StateFlow<ApiResult<UserDto>?>
}

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val onRegistrationSuccessful: () -> Unit,
    private val onLoginClicked: () -> Unit
) : RegisterComponent, ComponentContext by componentContext {
    private var _userRegisterResponse = MutableStateFlow<ApiResult<UserDto>?>(null)
    override val userRegisterResponse = _userRegisterResponse.asStateFlow()

    private val coroutineScope = componentContext.componentCoroutineScope()
    override fun onRegisterClicked(email: String, password: String) {
        coroutineScope.launch(Dispatchers.Default) {
            RepositoryImpl().register(email, password).collect{
                _userRegisterResponse.value = it
                when(it) {
                    is ApiResult.Success -> {
                        DataStore.settings["token"] = userRegisterResponse.value
                        onRegistrationSuccessful()
                    }

                    is ApiResult.Error -> {

                    }

                    else -> {}
                }
            }
        }
    }

    override fun onLoginClicked() {
        onLoginClicked.invoke()
    }

}