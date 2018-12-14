package ru.hh.android.plugin.feature_module.core.ui.custom_view

import com.intellij.ui.ClickListener
import com.intellij.ui.CollectionListModel
import ru.hh.android.plugin.feature_module.core.ui.model.CheckBoxListViewItem
import ru.hh.android.plugin.feature_module.extensions.EMPTY
import ru.hh.android.plugin.feature_module.extensions.SPACE
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import javax.swing.JCheckBox
import javax.swing.JList


/**
 * List view with checkboxes and force enabled items support.
 */
class CheckBoxListView<T>(
        private val onItemSelectedListener: ((T?) -> Unit)? = null,
        private val onItemToggleChangedListener: ((T) -> Unit)? = null
) : JList<T>() where T : CheckBoxListViewItem {

    private var items: List<T> = emptyList()


    init {
        setupOnSelectedListener()
        setupClickListenerOnCheckBoxes()
        setupKeyboardListenerForSpaceButton()
    }


    fun setItems(items: List<T>) {
        this.items = items

        cellRenderer = CheckBoxListViewItemRenderer<T>()
        model = CollectionListModel(this.items)
        selectedIndex = 0
    }


    private fun setupOnSelectedListener() {
        addListSelectionListener { onItemSelectedListener?.invoke(getSelectedItem()) }
    }

    private fun setupClickListenerOnCheckBoxes() {
        val clickableArea = JCheckBox(String.EMPTY).minimumSize.width
        (object : ClickListener() {
            override fun onClick(event: MouseEvent, clickCount: Int): Boolean {
                if (event.x < clickableArea) {
                    toggleSelection()
                }

                return true
            }
        }).installOn(this)
    }

    private fun setupKeyboardListenerForSpaceButton() {
        addKeyListener(object : KeyAdapter() {
            override fun keyTyped(e: KeyEvent) {
                if (e.keyChar == Char.SPACE) {
                    toggleSelection()
                }
            }
        })
    }


    private fun toggleSelection() {
        val currentItem = getSelectedItem() ?: return
        val isSelectedItemChecked = currentItem.isChecked

        for (selectedItem in selectedValuesList) {
            selectedItem.isChecked = !isSelectedItemChecked
            onItemToggleChangedListener?.invoke(selectedItem)
        }

        repaint()
    }

    private fun getSelectedItem(): T? {
        val leadSelectionIndex = selectionModel.leadSelectionIndex
        return if (leadSelectionIndex < 0) null else items[leadSelectionIndex]
    }

}