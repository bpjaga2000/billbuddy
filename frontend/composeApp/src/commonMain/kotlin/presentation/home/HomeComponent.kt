package presentation.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface HomeComponent {

}

class DefaultHomeComponent(
    componentContext: ComponentContext
//    private val homeViewModel: HomeViewModel
) : HomeComponent, ComponentContext by componentContext {

}