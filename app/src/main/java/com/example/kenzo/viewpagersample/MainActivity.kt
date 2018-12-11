package com.example.kenzo.viewpagersample

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.left
import kotlinx.android.synthetic.main.activity_main.right
import kotlinx.android.synthetic.main.activity_main.star
import kotlinx.android.synthetic.main.activity_main.viewA
import kotlinx.android.synthetic.main.activity_main.viewI
import kotlinx.android.synthetic.main.activity_main.viewPager
import kotlinx.android.synthetic.main.activity_main.viewU

class MainActivity : AppCompatActivity() {

    private var prevDragPosition = 0
    private val onPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                updateTranslationYofA(position, positionOffset)
                updateTranslationYofI(position, positionOffset)
                updateTranslationYofU(position, positionOffset)
                updateStarAlpha(position, positionOffset)
            }

            override fun onPageSelected(position: Int) = Unit
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTranslation()
        viewPager.apply {
            adapter = ViewPagerAdapter(context)
            addOnPageChangeListener(onPageChangeListener)
        }
        left.setOnClickListener {
            // CustomViewPagerを使う場合
            // viewPager.currentItem--

            // fakeDragを使う場合
            if (viewPager.currentItem > 0) fakeDrag(false)
        }
        right.setOnClickListener {
            // CustomViewPagerを使う場合
            // viewPager.currentItem++

            // fakeDragを使う場合
            if (viewPager.currentItem + 1 < viewPager.adapter?.count ?: 0) fakeDrag(true)
        }
        star.setOnClickListener { viewPager.currentItem = 0 }
        viewA.setOnClickListener { /* 何かさせたかった */ }
        viewI.setOnClickListener { /* 何かさせたかった */ }
        viewU.setOnClickListener { /* 何かさせたかった */ }
    }

    private fun initTranslation() {
    }

    private fun updateTranslationYofA(position: Int, positionOffset: Float) {
        viewA.translationY = when (position) {
            0 -> viewA.height * positionOffset
            else -> viewA.height.toFloat()
        }
    }

    private fun updateTranslationYofI(position: Int, positionOffset: Float) {
        viewI.translationY = when (position) {
            0 -> viewI.height - viewI.height * positionOffset
            1 -> viewI.height * positionOffset
            else -> viewI.height.toFloat()
        }
        if (viewI.height == 0) viewI.translationY = Float.MAX_VALUE // 初期状態で表示されるのを防ぐ
    }

    private fun updateTranslationYofU(position: Int, positionOffset: Float) {
        viewU.translationY = when (position) {
            1 -> viewU.height - viewU.height * positionOffset
            2 -> viewU.height * positionOffset
            else -> viewU.height.toFloat()
        }
        if (viewU.height == 0) viewU.translationY = Float.MAX_VALUE // 初期状態で表示されるのを防ぐ
    }

    private fun updateStarAlpha(position: Int, positionOffset: Float) {
        star.alpha = when (position) {
            1 -> positionOffset
            2 -> 1f - positionOffset
            else -> 0f
        }
    }

    private fun fakeDrag(forward: Boolean) {
        if (prevDragPosition == 0 && viewPager.beginFakeDrag()) {
            ValueAnimator.ofInt(0, viewPager.width).apply {
                duration = 1000L
                interpolator = FastOutSlowInInterpolator()
                addListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator?) = Unit

                    override fun onAnimationEnd(animation: Animator?) {
                        viewPager.endFakeDrag()
                        prevDragPosition = 0
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        viewPager.endFakeDrag()
                        prevDragPosition = 0
                    }

                    override fun onAnimationRepeat(animation: Animator?) = Unit
                })
                addUpdateListener {
                    val dragPosition: Int = it.animatedValue as Int
                    val dragOffset: Float = ((dragPosition - prevDragPosition) * if (forward) -1 else 1).toFloat()
                    prevDragPosition = dragPosition
                    viewPager.fakeDragBy(dragOffset)
                }
            }.start()
        }
    }
}
