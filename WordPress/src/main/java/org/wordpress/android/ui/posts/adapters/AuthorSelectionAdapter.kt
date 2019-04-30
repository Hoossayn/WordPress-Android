package org.wordpress.android.ui.posts.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.CallSuper
import org.wordpress.android.R
import org.wordpress.android.WordPress
import org.wordpress.android.ui.posts.AuthorFilterListItemUIState
import org.wordpress.android.ui.posts.AuthorFilterSelection
import org.wordpress.android.ui.utils.UiHelpers
import org.wordpress.android.util.GravatarUtils
import org.wordpress.android.util.image.ImageManager
import javax.inject.Inject

class AuthorSelectionAdapter(context: Context) : BaseAdapter() {
    @Inject lateinit var imageManager: ImageManager
    @Inject lateinit var uiHelpers: UiHelpers

    private val items = mutableListOf<AuthorFilterListItemUIState>()

    init {
        (context.applicationContext as WordPress).component().inject(this)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val holder: DropdownViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(R.layout.post_list_author_selection_dropdown, parent, false)
            holder = DropdownViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as DropdownViewHolder
        }

        holder.bind(items[position], imageManager, uiHelpers)

        return view!!
    }

    override fun getItemId(position: Int): Long = items[position].id

    fun getIndexOfSelection(selection: AuthorFilterSelection): Int? {
        for ((index, item) in items.withIndex()) {
            if (item.id == selection.id) {
                return index
            }
        }

        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val holder: NormalViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(R.layout.post_list_author_selection, parent, false)
            holder = NormalViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as NormalViewHolder
        }

        holder.bind(items[position], imageManager, uiHelpers)

        return view!!
    }

    override fun hasStableIds(): Boolean = true

    override fun getItem(position: Int): Any = items[position]

    override fun getCount(): Int = items.count()

    fun updateItems(newItems: List<AuthorFilterListItemUIState>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    private open class NormalViewHolder(protected val itemView: View) {
        protected val image: AppCompatImageView = itemView.findViewById(R.id.post_list_author_selection_image)

        @CallSuper
        open fun bind(state: AuthorFilterListItemUIState, imageManager: ImageManager, uiHelpers: UiHelpers) {
            val avatarSize = image.resources.getDimensionPixelSize(R.dimen.avatar_sz_small)
            val url = GravatarUtils.fixGravatarUrl(state.avatarUrl, avatarSize)

            imageManager.loadIntoCircle(image, state.imageType, url)
        }
    }

    private class DropdownViewHolder(itemView: View) : NormalViewHolder(itemView) {
        private val text: AppCompatTextView = itemView.findViewById(R.id.post_list_author_selection_text)

        override fun bind(state: AuthorFilterListItemUIState, imageManager: ImageManager, uiHelpers: UiHelpers) {
            super.bind(state, imageManager, uiHelpers)
            val context = itemView.context
            text.text = uiHelpers.getTextOfUiString(context, state.text)
            itemView.setBackgroundColor(ContextCompat.getColor(context, state.dropDownBackground))
        }
    }
}
