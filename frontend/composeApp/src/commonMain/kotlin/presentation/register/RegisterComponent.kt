package presentation.register

import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.set
import data.Repository
import data.remote.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope

interface RegisterComponent {
    fun onRegisterClicked(email: String, password: String)
    fun onLoginClicked()
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onRegistrationSuccessful: () -> Unit,
            onLoginClicked: () -> Unit
        ): RegisterComponent
    }
}

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val onRegistrationSuccessful: () -> Unit,
    private val onLoginClicked: () -> Unit,
    private val repository: Repository
) : RegisterComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.componentCoroutineScope()
    override fun onRegisterClicked(email: String, password: String) {
        coroutineScope.launch(Dispatchers.Default) {
            repository.register(email, password).collect {
                when (it) {
                    is ApiResult.Success -> {
                        withContext(Dispatchers.Main) {
                            repository.getSettings()["token"] = it.data.token
                            repository.getSettings()["email"] = it.data.email
                            repository.getSettings()["id"] = it.data.id
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

    class Factory(
        val repository: Repository,
    ) : RegisterComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext, onRegistrationSuccessful: () -> Unit,
            onLoginClicked: () -> Unit
        ): RegisterComponent {
            return DefaultRegisterComponent(
                componentContext,
                onRegistrationSuccessful,
                onLoginClicked,
                repository
            )
        }
    }

}