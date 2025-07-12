package presentation.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.set
import data.model.dto.UserDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope

interface RegisterComponent {
    fun onRegisterClicked(email: String, password: String)
    fun onLoginClicked()
}

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val onRegistrationSuccessful: () -> Unit,
    private val onLoginClicked: () -> Unit
) : RegisterComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.componentCoroutineScope()
    override fun onRegisterClicked(email: String, password: String) {
        coroutineScope.launch(Dispatchers.Default) {
            RepositoryImpl().register(email, password).collect {
                when (it) {
                    is ApiResult.Success -> {
                        withContext(Dispatchers.Main) {
                            DataStore.settings["token"] = it.data.token
                            DataStore.settings["email"] = it.data.email
                            DataStore.settings["id"] = it.data.id
                            onRegistrationSuccessful()
                        }
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