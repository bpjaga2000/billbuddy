package presentation.login

import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.set
import data.model.dto.UserDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope

interface LoginComponent {
    val isLoading: StateFlow<Boolean>
    fun onLoginClicked(email: String, password: String)
    fun onRegisterClicked()
}

class DefaultLoginComponent(
    private val componentContext: ComponentContext,
    private val onLoginSuccessful: () -> Unit,
    private val onRegisterClicked: () -> Unit
) : LoginComponent, ComponentContext by componentContext {

    private var _userLoginResponse = MutableStateFlow<ApiResult<UserDto>?>(null)

    private val coroutineScope = componentContext.componentCoroutineScope()
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override fun onLoginClicked(email: String, password: String) {
        _isLoading.value = true
        coroutineScope.launch(Dispatchers.Default) {
            RepositoryImpl().logIn(email, password).collect {
                when (it) {
                    is ApiResult.Success -> {
                        _isLoading.value = false
                        withContext(Dispatchers.Main) {
                            _userLoginResponse.value = it
                            DataStore.settings["token"] = it.data.token
                            DataStore.settings["email"] = it.data.email
                            DataStore.settings["id"] = it.data.id
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
            RepositoryImpl().sync((_userLoginResponse.value as ApiResult.Success<UserDto>).data.id)
                .collect {
                    when (it) {
                        is ApiResult.Success -> {
                            RepositoryImpl().saveSyncData(it.data).collectLatest { isDone ->
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

}