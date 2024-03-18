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

    init{
        viewModelScope.launch {
           RepositoryImpl().logIn("abc@xyz.com", "12345678").collect{
               _userResponse.value = it.body()
           }
        }
    }

}