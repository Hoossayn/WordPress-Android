package org.wordpress.android.ui.bloggingreminders

import androidx.annotation.DrawableRes
import org.wordpress.android.fluxc.model.BloggingRemindersModel
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.CAPTION
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.DAY_BUTTONS
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.HIGH_EMPHASIS_TEXT
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.ILLUSTRATION
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.LOW_EMPHASIS_TEXT
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.TIP
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Type.TITLE
import org.wordpress.android.ui.utils.ListItemInteraction
import org.wordpress.android.ui.utils.UiString

sealed class BloggingRemindersItem(val type: Type) {
    enum class Type {
        ILLUSTRATION, TITLE, HIGH_EMPHASIS_TEXT, LOW_EMPHASIS_TEXT, CAPTION, DAY_BUTTONS, TIP
    }

    data class Illustration(@DrawableRes val illustration: Int) : BloggingRemindersItem(ILLUSTRATION)
    data class Title(val text: UiString) : BloggingRemindersItem(TITLE)
    data class Caption(val text: UiString) : BloggingRemindersItem(CAPTION)
    data class HighEmphasisText(val text: EmphasizedText) : BloggingRemindersItem(
            HIGH_EMPHASIS_TEXT
    ) {
        constructor(uiString: UiString) : this(EmphasizedText(uiString))
    }

    data class MediumEmphasisText(val text: EmphasizedText?, val isInvisible: Boolean = false) : BloggingRemindersItem(
            LOW_EMPHASIS_TEXT
    ) {
        constructor(uiString: UiString) : this(EmphasizedText(uiString, false))
    }

    data class EmphasizedText(val text: UiString, val emphasizeTextParams: Boolean = true)

    data class DayButtons(val dayItems: List<DayItem>) : BloggingRemindersItem(DAY_BUTTONS) {
        init {
            assert(dayItems.size == BloggingRemindersModel.Day.values().size) {
                "7 days need to be defined"
            }
        }

        data class DayItem(val text: UiString, val isSelected: Boolean, val onClick: ListItemInteraction)
    }

    data class Tip(val title: UiString, val message: UiString) : BloggingRemindersItem(TIP)
}