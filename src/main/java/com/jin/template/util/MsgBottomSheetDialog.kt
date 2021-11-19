package com.jin.template.util

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jin.template.TemplateController.cancelMsg
import com.jin.template.TemplateController.confirmMsg
import com.jin.template.databinding.DialogMsgOneBtnBinding
import com.jin.template.databinding.DialogMsgTwoBtnBinding

@Suppress("UNUSED")
object MsgBottomSheetDialog {
    private var bottomSheetDialog: BottomSheetDialog? = null

    fun withOneBtn(context: Context): OneBtnBSDialog {
        try {
            bottomSheetDialog?.cancel()
        } catch (e: Exception) {
            Debug.e(e.message.toString())
        }
        bottomSheetDialog = OneBtnBSDialog(context)
        return bottomSheetDialog!! as OneBtnBSDialog
    }

    fun withTwoBtn(context: Context): TwoBtnBSDialog {
        try {
            bottomSheetDialog?.cancel()
        } catch (e: Exception) {
            Debug.e(e.message.toString())
        }
        bottomSheetDialog = TwoBtnBSDialog(context)
        return bottomSheetDialog!! as TwoBtnBSDialog
    }

    fun dismiss() = try {
        bottomSheetDialog?.dismiss()
    } catch (e: Exception) {
        Debug.e(e.message.toString())
    }

    class OneBtnBSDialog(context: Context) : BottomSheetDialog(context) {
        private val binding = DialogMsgOneBtnBinding.inflate(layoutInflater)
        private var autoDismiss = true

        init {
            setContentView(binding.root)
            behavior.skipCollapsed = true
            setOnShowListener { behavior.state = BottomSheetBehavior.STATE_EXPANDED }
            binding.tvDlgMsg1Description.setOnClickListener { if (autoDismiss) dismiss() }

            binding.tvDlgMsg1Done.text = confirmMsg
        }

        fun autoDismiss(autoDismiss: Boolean) = apply { this.autoDismiss = autoDismiss }

        fun setMessage(msg: String) = apply { binding.tvDlgMsg1Description.text = msg }
        fun setMessage(resId: Int) =
            apply { binding.tvDlgMsg1Description.text = context.getString(resId) }

        fun setDoneText(text: String) = apply { binding.tvDlgMsg1Done.text = text }
        fun setDoneText(resId: Int) =
            apply { binding.tvDlgMsg1Done.text = context.getString(resId) }

        fun setOnDoneListener(l: (View) -> Unit) = apply {
            binding.tvDlgMsg1Done.setOnClickListener {
                l.invoke(it)
                if (autoDismiss) dismiss()
            }
        }
    }

    class TwoBtnBSDialog(context: Context) : BottomSheetDialog(context) {
        private val binding = DialogMsgTwoBtnBinding.inflate(layoutInflater)
        private var autoDismiss = true

        init {
            setContentView(binding.root)
            behavior.skipCollapsed = true
            setOnShowListener { behavior.state = BottomSheetBehavior.STATE_EXPANDED }
            binding.tvDlgMsg2Cancel.setOnClickListener { dismiss() }
            binding.tvDlgMsg2Done.setOnClickListener(null)

            binding.tvDlgMsg2Done.text = confirmMsg
            binding.tvDlgMsg2Cancel.text = cancelMsg
        }

        fun autoDismiss(autoDismiss: Boolean) = apply { this.autoDismiss = autoDismiss }

        fun setMessage(msg: String) = apply { binding.tvDlgMsg2Description.text = msg }
        fun setMessage(resId: Int) =
            apply { binding.tvDlgMsg2Description.text = context.getString(resId) }

        fun setDoneText(text: String) = apply { binding.tvDlgMsg2Done.text = text }
        fun setDoneText(resId: Int) =
            apply { binding.tvDlgMsg2Done.text = context.getString(resId) }

        fun setCancelText(text: String) = apply { binding.tvDlgMsg2Cancel.text = text }
        fun setCancelText(resId: Int) =
            apply { binding.tvDlgMsg2Cancel.text = context.getString(resId) }

        fun setOnDoneListener(l: (View) -> Unit) = apply {
            binding.tvDlgMsg2Done.setOnClickListener {
                l.invoke(it)
                if (autoDismiss) dismiss()
            }
        }

        fun setOnCancelListener(l: (View) -> Unit) = apply {
            binding.tvDlgMsg2Done.setOnClickListener {
                l.invoke(it)
                dismiss()
            }
        }
    }
}