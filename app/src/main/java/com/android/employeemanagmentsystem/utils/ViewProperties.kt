package com.android.employeemanagmentsystem.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.android.employeemanagmentsystem.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


inline fun EditText.search(crossinline work: (query: String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            work.invoke(s.toString())
        }

    })
}

infix fun<T> Boolean.then(first: T): T? = if (this) first else null

fun View.snackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

@SuppressLint("ResourceAsColor")
inline fun SearchView.searchQuery(crossinline work: (query: String) -> Unit) {


    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
                work.invoke(query)
            }
            this@searchQuery.clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }

    })

}


fun Context.toast(msg: String) {

    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

}

fun View.visible(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}


fun LottieAnimationView.setUpAnimation(): LottieAnimationView {
    this.apply {
        visibility = View.VISIBLE
        playAnimation()
        progress = 0F
        repeatMode = LottieDrawable.RESTART

    }
    return this
}

fun Activity.hideKeyboard() {
    try {
        val imm: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
    }
}

fun Activity.changeStatusBarColor(color: Int = R.color.extra_light_blue) {
    this.window.statusBarColor = ContextCompat.getColor(this, color)
}
val TAG = "xyz"
suspend fun Context.handleException(e: Exception){
    when (e) {
        //handling exception in api
        is ApiException -> {
            withContext(Dispatchers.Main){
                this@handleException.toast(e.message ?: "null message in api exception")
                Log.e(TAG, "handleException: " + e.localizedMessage )
            }
        }
        //handling internet exception
        is NoInternetException -> {
            withContext(Dispatchers.Main){
                this@handleException.toast(e.message ?: "null message in api exception")
                Log.e(TAG, "handleException: " + e.localizedMessage )
            }
        }
        else -> {
            withContext(Dispatchers.Main){
                this@handleException.toast(e.message ?: "unknown error in else case")
                Log.e(TAG, "handleException: " + e.localizedMessage )
            }
        }
    }
}

fun String.toMultipartReq() = this.toRequestBody("text/plain".toMediaTypeOrNull())

fun ByteArray.toPdfRequestBody(): RequestBody {
    return RequestBody.create(
        "application/pdf".toMediaTypeOrNull(),
        this
    )
}