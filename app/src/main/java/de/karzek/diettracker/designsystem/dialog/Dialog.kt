package de.karzek.diettracker.designsystem.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.util.StringUtils
import kotlinx.android.synthetic.main.component_dialog.*
import kotlinx.android.synthetic.main.component_dialog.view.*

class Dialog(
        context: Context,
        private val listener: DialogButtonListener,
        private var drawable: Drawable?,
        private var titleText: String?,
        private var messageText: String?,
        private var positiveText: String?,
        private var positiveColor: Int?,
        private var negativeText: String?,
        private var isCancelableDialog: Boolean
) : android.app.Dialog(context, R.style.Dialog) {

    private constructor(builder: Builder) : this(
            builder.context,
            builder.listener,
            builder.drawable,
            builder.titleText,
            builder.messageText,
            builder.positiveText,
            builder.positiveColor,
            builder.negativeText,
            builder.isCancelableDialog
    )

    class Builder(var context: Context) {
        internal lateinit var listener: DialogButtonListener
        internal var drawable: Drawable? = null
        internal var titleText: String? = null
        internal var messageText: String? = null
        internal var positiveText: String? = null
        internal var negativeText: String? = null
        internal var isCancelableDialog: Boolean = true
        @ColorInt
        internal var positiveColor: Int? = null

        //image
        fun setImage(@DrawableRes drawableId: Int) = apply { this.drawable = context.getDrawable(drawableId) }

        //title
        fun setTitle(@StringRes stringId: Int) = apply { this.titleText = context.getString(stringId) }

        fun setTitle(text: String?) = apply { this.titleText = text }

        //message
        fun setMessage(@StringRes text: Int) = apply { this.messageText = context.getString(text) }

        fun setMessage(text: String) = apply { this.messageText = text }

        //controls
        fun setPositiveText(@StringRes stringId: Int) = apply { this.positiveText = context.getString(stringId) }

        fun setPositiveText(text: String) = apply { this.positiveText = text }

        fun setNegativeText(@StringRes stringId: Int) = apply { this.negativeText = context.getString(stringId) }
        fun setNegativeText(text: String) = apply { this.negativeText = text }

        fun setPositiveColor(@ColorRes argb: Int) = apply { this.positiveColor = ContextCompat.getColor(context, argb) }

        //listener
        fun setListener(listener: DialogButtonListener) = apply { this.listener = listener }

        fun setCancelable(cancelable: Boolean) = apply { this.isCancelableDialog = cancelable }

        fun show(): Dialog {
            val dialog = Dialog(this)
            dialog.show()
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.component_dialog)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setupImage(drawable)
        setupTitle(titleText)
        setupMessage(messageText)
        setupControls(positiveText, positiveColor, negativeText)

        setCancelable(isCancelableDialog)
    }

    private fun setupImage(drawable: Drawable?) {
        drawable?.run {
            image.setImageDrawable(drawable)
            image.visibility = View.VISIBLE
        }
    }

    private fun setupTitle(titleText: String?) {
        title.text = titleText
        if (StringUtils.isNullOrEmpty(titleText)) {
            title.visibility = View.GONE
        }
    }

    private fun setupMessage(messageText: String?) {
        message.text = messageText
        if (StringUtils.isNullOrEmpty(messageText)) {
            message.visibility = View.GONE
        }
    }

    private fun setupControls(
            positiveText: String?,
            positiveColor: Int?,
            negativeText: String?
    ) {
        positiveText?.run {
            positive_action.text = this
        }
        positiveColor?.run {
            positive_action.setTextColor(this)
        }
        negativeText?.run {
            negative_action.text = this
        }
    }

    //listener

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        positive_action.setOnClickListener {
            listener.onPositiveButtonClicked()
            dismiss()
        }
        negative_action.setOnClickListener {
            dismiss()
            listener.onNegativeButtonClicked()
        }
        setOnCancelListener { listener.onNegativeButtonClicked() }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        negative_action.setOnClickListener(null)
        positive_action.setOnEditorActionListener(null)
        setOnCancelListener(null)
    }

    companion object {
        interface DialogButtonListener {
            fun onPositiveButtonClicked()
            fun onNegativeButtonClicked() {}
        }
    }

}