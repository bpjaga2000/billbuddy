package presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import data.model.dto.ProfileUpdateDto
import data.remote.ApiResult
import data.repository.RepositoryImpl
import dev.bpj4.billbuddy.tableandmigrations.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.DataStore
import utils.DispatcherUtils.componentCoroutineScope
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface ProfileComponent {
    var user: Users
    val name: MutableState<String>
    val email: MutableState<String>
    val password: MutableState<String>
    val phone: MutableState<String>
    fun onSaveClick()
    fun onLogoutClick()
}

@OptIn(ExperimentalTime::class)
class DefaultProfileComponent(
    val componentContext: ComponentContext,
    val onLogoutClick: () -> Unit
) : ProfileComponent, ComponentContext by componentContext {
    override lateinit var user: Users
    override val name = mutableStateOf("")
    override val email = mutableStateOf("")
    override val password = mutableStateOf("")
    override val phone = mutableStateOf("")

    init {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            RepositoryImpl().getCurrentUser().collect {
                user = it
                name.value = it.name
                email.value = it.email
                password.value = it.password
                phone.value = it.phone?.toString() ?: ""
            }
        }
    }

    override fun onSaveClick() {
        componentContext.componentCoroutineScope().launch(Dispatchers.Default) {
            val profileUpdateDto = ProfileUpdateDto(
                user.id,
                name.value,
                user.mobileCountryCode,
                phone.value.toLongOrNull(),
                Clock.System.now().epochSeconds
            )
            RepositoryImpl().updateProfile(profileUpdateDto).collect {
                when (it) {
                    is ApiResult.Success -> {}

                    else -> {}
                }
            }
        }
    }

    override fun onLogoutClick() {
        componentContext.componentCoroutineScope().launch {
            RepositoryImpl().logout().collect {
                when (it) {
                    is ApiResult.Success -> {
                        withContext(Dispatchers.Main) {
                            RepositoryImpl().clearDb()
                            DataStore.settings.clear()
                            onLogoutClick.invoke()
                        }
                    }

                    else -> {}
                }
            }
        }
    }

}