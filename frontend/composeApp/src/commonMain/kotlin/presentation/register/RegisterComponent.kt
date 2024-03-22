package presentation.register

import com.arkivanov.decompose.ComponentContext

interface RegisterComponent {
    fun onLoginClicked()
}

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val onLoginClicked: () -> Unit
) : RegisterComponent, ComponentContext by componentContext {

    override fun onLoginClicked() {
        onLoginClicked.invoke()
    }

}