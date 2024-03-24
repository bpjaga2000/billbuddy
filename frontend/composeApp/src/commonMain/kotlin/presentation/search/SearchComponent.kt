package presentation.search

import com.arkivanov.decompose.ComponentContext

interface SearchComponent

class DefaultSearchComponent(
    private val componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {
}