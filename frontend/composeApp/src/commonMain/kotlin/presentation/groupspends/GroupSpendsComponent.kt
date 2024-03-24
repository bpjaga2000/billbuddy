package presentation.groupspends

import com.arkivanov.decompose.ComponentContext

interface GroupSpendsComponent

class DefaultGroupSpendsComponent(
    private val componentContext: ComponentContext
) : GroupSpendsComponent {
}