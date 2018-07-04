package com.quang.googleio.hanoi.ui

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.quang.googleio.hanoi.R

class MaskEditText : AppCompatEditText {

    private var selfChange: Boolean = false
    private var mask: String? = null
    val rawText get() = unformat(text)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MaskEditText)
            if (typedArray.hasValue(R.styleable.MaskEditText_met_mask)) {
                mask = typedArray.getString(R.styleable.MaskEditText_met_mask)
            }
            typedArray?.recycle()
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (text.isNullOrEmpty() || selfChange) return

        selfChange = true
        format(text)
        setCursorPosition(start, lengthBefore, lengthAfter)
        selfChange = false
    }

    private fun format(source: CharSequence?) {
        if (source.isNullOrEmpty() || mask.isNullOrEmpty()) return

        val builder = StringBuilder()
        val textLength = source!!.length
        var textIndex = 0
        mask?.forEach {
            if (textIndex >= textLength) return@forEach
            var c = source[textIndex]
            if (it.isPlaceHolder()) {
                if (c.isLetterOrDigit()) {
                    builder.append(c)
                    textIndex++
                } else {
                    // find closest letter or digit
                    for (i in textIndex until textLength) {
                        c = source[i]
                        if (c.isLetterOrDigit()) {
                            builder.append(c)
                            textIndex = i + 1
                            break
                        }
                    }
                }
            } else {
                builder.append(it)
                if (c == it) textIndex++
            }
        }

        setText(builder)
    }

    private fun unformat(source: CharSequence?): String? {
        if (source.isNullOrEmpty() || mask.isNullOrEmpty()) return null

        val builder = StringBuilder()
        val textLength = source!!.length
        mask?.forEachIndexed { index, m ->
            if (index >= textLength) return@forEachIndexed
            val c = source[index]
            if (m.isPlaceHolder()) {
                builder.append(c)
            }
        }

        return builder.toString()
    }

    private fun setCursorPosition(start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (text.isNullOrEmpty()) return

        val end = text.length
        val cursor = when {
            lengthBefore > lengthAfter -> start
            lengthAfter > 1 -> end
            start < end -> findNextPlaceHolderPosition(start, end)
            else -> end
        }
        setSelection(cursor)
    }

    private fun findNextPlaceHolderPosition(start: Int, end: Int): Int {
        mask?.let {
            for (i in start until end) {
                val m = it[i]
                val c = text[i]
                if (m.isPlaceHolder() && c.isLetterOrDigit()) {
                    return i + 1
                }
            }
        }
        return start + 1
    }
}