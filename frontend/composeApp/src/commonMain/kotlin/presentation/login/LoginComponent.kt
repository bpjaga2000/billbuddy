package presentation.login

import com.arkivanov.decompose.ComponentContext

interface LoginComponent {
    fun onLoginClicked(email: String, password: String)
    fun onRegisterClicked()
}

class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val logInViewModel: LogInViewModel,
    private val onRegisterClicked: () -> Unit
) : LoginComponent, ComponentContext by componentContext {
    override fun onLoginClicked(email: String, password: String) {
        logInViewModel.logIn(email, password)
    }

    override fun onRegisterClicked() {
        onRegisterClicked.invoke()
    }

}