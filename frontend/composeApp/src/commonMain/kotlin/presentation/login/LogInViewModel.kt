package presentation.login

import data.repository.RepositoryImpl
import data.model.dto.UserDto
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.call.body
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogInViewModel: ViewModel() {

    private var _userResponse = MutableStateFlow<UserDto?>(null)
    val userResponse = _userResponse.asStateFlow()

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            RepositoryImpl().logIn(email, password).collect {
                _userResponse.value = it.body()
            }
        }
    }

}