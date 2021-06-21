package com.jin.template.util

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jin.template.R
import com.jin.template.TemplateController.cancelMsg
import com.jin.template.TemplateController.confirmMsg
import kotlinx.android.synthetic.main.dialog_msg_one_btn.*
import kotlinx.android.synthetic.main.dialog_msg_two_btn.*

@Suppress("UNUSED")
object MsgBottomSheetDialog {
    private var bottomSheetDialog: BottomSheetDialog? = null

    fun withOneBtn(context: Context): OneBtnBSDialog {
        bottomSheetDialog?.cancel()
        bottomSheetDialog = OneBtnBSDialog(context)
        return bottomSheetDialog!! as OneBtnBSDialog
    }

    fun withTwoBtn(context: Context): TwoBtnBSDialog {
        bottomSheetDialog?.cancel()
        bottomSheetDialog = TwoBtnBSDialog(context)
        return bottomSheetDialog!! as TwoBtnBSDialog
    }

    fun dismiss() = try {
        bottomSheetDialog?.dismiss()
    } catch (e: Exception) {
        Debug.i(e.message.toString())
    }

    class OneBtnBSDialog(context: Context) : BottomSheetDialog(context) {
        private var autoDismiss = true

        init {
            setContentView(R.layout.dialog_msg_one_btn)
            behavior.skipCollapsed = true
            setOnShowListener { behavior.state = BottomSheetBehavior.STATE_EXPANDED }
            tv_dlg_msg1_done.setOnClickListener { if (autoDismiss) dismiss() }

            tv_dlg_msg1_done.text = confirmMsg
        }

        fun autoDismiss(autoDismiss: Boolean) = apply { this.autoDismiss = autoDismiss }

        fun setMessage(msg: String) = apply { tv_dlg_msg1_description.text = msg }
        fun setMessage(resId: Int) =
            apply { tv_dlg_msg1_description.text = context.getString(resId) }

        fun setDoneText(text: String) = apply { tv_dlg_msg1_done.text = text }
        fun setDoneText(resId: Int) = apply { tv_dlg_msg1_done.text = context.getString(resId) }

        fun setOnDoneListener(l: (View) -> Unit) = apply {
            tv_dlg_msg1_done.setOnClickListener {
                l.invoke(it)
                if (autoDismiss) dismiss()
            }
        }
    }

    class TwoBtnBSDialog(context: Context) : BottomSheetDialog(context) {
        private var autoDismiss = true

        init {
            setContentView(R.layout.dialog_msg_two_btn)
            behavior.skipCollapsed = true
            setOnShowListener { behavior.state = BottomSheetBehavior.STATE_EXPANDED }
            tv_dlg_msg2_cancel.setOnClickListener { dismiss() }
            tv_dlg_msg2_done.setOnClickListener(null)

            tv_dlg_msg2_done.text = confirmMsg
            tv_dlg_msg2_cancel.text = cancelMsg
        }

        fun autoDismiss(autoDismiss: Boolean) = apply { this.autoDismiss = autoDismiss }

        fun setMessage(msg: String) = apply { tv_dlg_msg2_description.text = msg }
        fun setMessage(resId: Int) =
            apply { tv_dlg_msg2_description.text = context.getString(resId) }

        fun setDoneText(text: String) = apply { tv_dlg_msg2_done.text = text }
        fun setDoneText(resId: Int) = apply { tv_dlg_msg2_done.text = context.getString(resId) }

        fun setCancelText(text: String) = apply { tv_dlg_msg2_cancel.text = text }
        fun setCancelText(resId: Int) =
            apply { tv_dlg_msg2_cancel.text = context.getString(resId) }

        fun setOnDoneListener(l: (View) -> Unit) = apply {
            tv_dlg_msg2_done.setOnClickListener {
                l.invoke(it)
                if (autoDismiss) dismiss()
            }
        }

        fun setOnCancelListener(l: (View) -> Unit) = apply {
            tv_dlg_msg2_cancel.setOnClickListener {
                l.invoke(it)
                dismiss()
            }
        }
    }
}