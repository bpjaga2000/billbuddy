package presentation.login

import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.set
import data.Repository
import data.remote.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DispatcherUtils.componentCoroutineScope

interface LoginComponent {
    val isLoading: StateFlow<Boolean>
    fun onLoginClicked(email: String, password: String)
    fun onRegisterClicked()
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onLoginSuccessful: () -> Unit,
            onRegisterClicked: () -> Unit
        ): LoginComponent
    }
}

class DefaultLoginComponent(
    private val componentContext: ComponentContext,
    private val onLoginSuccessful: () -> Unit,
    private val onRegisterClicked: () -> Unit,
    private val repository: Repository
) : LoginComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.componentCoroutineScope()
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override fun onLoginClicked(email: String, password: String) {
        _isLoading.value = true
        coroutineScope.launch(Dispatchers.Default) {
            repository.logIn(email, password).collect {
                when (it) {
                    is ApiResult.Success -> {
                        _isLoading.value = false
                        withContext(Dispatchers.Main) {
                            repository.getSettings()["token"] = it.data.token
                            repository.getSettings()["email"] = it.data.email
                            repository.getSettings()["id"] = it.data.id
                            syncData()
                        }
                    }

                    is ApiResult.Error -> withContext(Dispatchers.Main) { _isLoading.value = false }

                    else -> {}
                }
            }
        }
    }

    private fun syncData() {
        _isLoading.value = true
        coroutineScope.launch(Dispatchers.Default) {
            repository.sync()
                .collect {
                    when (it) {
                        is ApiResult.Success -> {
                            repository.saveSyncData(it.data).collectLatest { isDone ->
                                if (isDone)
                                    withContext(Dispatchers.Main) {
                                        _isLoading.value = false
                                        onLoginSuccessful()
                                    }
                            }
                        }

                        is ApiResult.Error -> withContext(Dispatchers.Main) {
                            _isLoading.value = false
                        }

                        else -> {}

                    }
                }
        }
    }

    override fun onRegisterClicked() {
        onRegisterClicked.invoke()
    }

    class Factory(
        val repository: Repository
    ) : LoginComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onLoginSuccessful: () -> Unit,
            onRegisterClicked: () -> Unit
        ): LoginComponent {
            return DefaultLoginComponent(
                componentContext,
                onLoginSuccessful,
                onRegisterClicked,
                repository
            )
        }
    }

}