package com.example.kenzo.viewpagersample

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ViewPagerAdapter(context: Context) : PagerAdapter() {

    private val list: List<String> = listOf(
        context.resources.getString(R.string.a),
        context.resources.getString(R.string.i),
        context.resources.getString(R.string.u)
    )

    override fun instantiateItem(container: ViewGroup, position: Int): View =
        LayoutInflater.from(container.context).inflate(R.layout.item_text, container, false)
            .also {
                (it as TextView).text = list[position]
                container.addView(it)
            }

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    override fun getCount(): Int = list.size

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}