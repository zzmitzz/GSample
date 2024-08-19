package com.example.apiretrofitktor.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.example.apiretrofitktor.R

class LoadingDialog(var context: Context) {
    private var dialog: Dialog? = null
    private var run: Runnable? = null
    private var handler: Handler? = null

    init {
        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.custom_dialog_loading)
        val window: Window? = dialog?.window
        window?.let {
            it.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = window.attributes
            layoutParams.gravity = Gravity.CENTER

            it.attributes = layoutParams

            dialog?.setCancelable(false)

            handler = Handler()
            run = Runnable {
                try {
                    if (dialog != null && dialog?.isShowing == true) {
                        dialog?.hide()
                    }
                } catch (e: Exception) {
                    ////LogVnp.Shape1(Shape1);
                }
            }
        }

    }

    fun show() {
        dialog?.show()
        run?.let { handler?.postDelayed(it, 90000) }
    }

    fun hide() {
        dialog?.dismiss()

        try {
            run?.let { handler?.removeCallbacks(it) }
        } catch (e: Exception) {

        }
    }

}