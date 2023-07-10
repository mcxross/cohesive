package xyz.mcxross.cohesive.designsystem.mellow

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ToastModel {

  var state: ToastState by mutableStateOf(initialState())
    private set

  fun onItemClicked(id: Long) {
    setState { copy(editingItemId = id) }
  }

  fun onItemCloseClicked(id: Long) {
    setState { copy(items = items.filterNot { it.id == id }) }
  }

  fun toast(title: String, message: String) {
    setState {
      val newItem =
        ToastItem(
          id = items.maxOfOrNull(ToastItem::id)?.plus(1L) ?: 1L,
          title = title,
          message = message,
        )

      copy(items = items + newItem, inputText = "")
    }
  }

  private fun initialState(): ToastState = ToastState()

  private inline fun setState(update: ToastState.() -> ToastState) {
    state = state.update()
  }

  data class ToastState(
    val items: List<ToastItem> = emptyList(),
    val inputText: String = "",
    val editingItemId: Long? = null,
  )

}
