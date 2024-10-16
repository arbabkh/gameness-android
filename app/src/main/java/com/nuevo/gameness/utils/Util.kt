package com.nuevo.gameness.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.nuevo.gameness.R
import com.nuevo.gameness.data.model.websocket.WebSocketResponse
import com.nuevo.gameness.databinding.DialogInfoBinding
import com.nuevo.gameness.databinding.DialogQuestionBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import okhttp3.*
import java.text.SimpleDateFormat
import java.util.*

fun Context?.setTabColor(textView: TextView, view: View, resId: Int) {
    textView.setMyTextColor(this, resId)
    view.setMyBackgroundTintList(this, resId)
}

fun TextView.setMyTextColor(context: Context?, resId: Int) {
    context?.let { setTextColor(ContextCompat.getColor(it, resId)) }
}

fun Button.setMyTextColor(context: Context?, resId: Int) {
    context?.let { setTextColor(ContextCompat.getColor(it, resId)) }
}

fun View.setMyBackgroundTintList(context: Context?, resId: Int) {
    context?.let { backgroundTintList = ContextCompat.getColorStateList(it, resId) }
}

fun String?.myContains(text: String?, locale: Locale = Locale.getDefault()): Boolean {
    return if (this != null && !text.isNullOrEmpty()) lowercase(locale).contains(
        text.lowercase(
            locale
        )
    )
    else false
}

fun ImageView.load(context: Context, url: String?) {
    Glide.with(context).load(url).into(this)
}

fun ImageView.loadWithBlurringImage(
    context: Context, url: String?, radius: Int = 17, sampling: Int = 2
) {
    Glide.with(context).load(url)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(radius, sampling)))
        .into(this)
}

fun ImageView.loadWithBlurringImage(
    context: Context, resId: Int, radius: Int = 17, sampling: Int = 2
) {
    Glide.with(context).load(resId)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(radius, sampling)))
        .into(this)
}

fun Date?.myToString(format: String, locale: Locale = Locale.getDefault()): String? {
    return try {
        SimpleDateFormat(format, locale).format(this!!)
    } catch (e: Exception) {
        Log.e("toCalendar", "ERROR: ${e.message}")
        null
    }
}

fun String?.toCalendar(
    pattern: String = "yyyy-MM-dd'T'HH:mm:ss",
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getDefault()
): Calendar? {
    return try {
        val calendar = Calendar.getInstance().clone() as Calendar
        SimpleDateFormat(pattern, locale).apply {
            this.timeZone = timeZone
        }.parse(this!!)?.let { calendar.time = it }
        calendar
    } catch (e: Exception) {
        Log.e("toCalendar", "ERROR: ${e.message}")
        null
    }
}

fun toDate(
    datee: String
): String? {
    return try {
        val daysArray =
            arrayOf("", "Pazar", "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi")
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = simpleDateFormat.parse(datee)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val day = calendar[(Calendar.DAY_OF_WEEK)]
        val dateList = datee.split("-")
        val monthName = SimpleDateFormat("MMMM").format(calendar.time)
        return dateList[2] + " " + monthName + " " + daysArray[day]
    } catch (e: Exception) {
        Log.e("toCalendar", "ERROR: ${e.message}")
        null
    }
}

fun Uri?.getFilePath(context: Context): String? {
    if (this != null && path != null) {
        var realPath: String? = null
        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?

        if (path!!.contains("/document/image:")) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(this).split(":")[1])
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = this
            selection = null
            selectionArgs = null
        }

        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DATE_TAKEN
        ) // some example data you can query
        val cursor = context.contentResolver.query(
            databaseUri,
            projection, selection, selectionArgs, null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                realPath = cursor.getString(columnIndex)
            }
            cursor.close()
            return realPath
        }
    }
    return null
}

fun Fragment.showDialog(text: String) {
    val dialogBinding = DialogInfoBinding.inflate(layoutInflater)
    val builder = AlertDialog.Builder(requireContext())
    builder.setView(dialogBinding.root)
    val dialog = builder.create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogBinding.textViewDialogInfo.text = text
    dialogBinding.textViewDialogInfoButton.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

fun Fragment.showQuestionDialog(question: String, positiveButtonClickListener: (() -> Unit)) {
    val dialogBinding = DialogQuestionBinding.inflate(layoutInflater)
    val builder = AlertDialog.Builder(requireContext())
    builder.setView(dialogBinding.root)
    val dialog = builder.create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogBinding.textViewQuestion.text = question
    dialogBinding.textViewNegative.setOnClickListener {
        dialog.dismiss()
    }
    dialogBinding.textViewPositive.setOnClickListener {
        positiveButtonClickListener.invoke()
        dialog.dismiss()
    }
    dialog.show()
}

fun Fragment.openUrlInBrowser(url: String) {
    try {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    } catch (ex: Exception) {
        throw Exception(getString(R.string.unsupported_url))
    }
}

fun openWebSocketListener(
    url: String,
    target: String,
    onMessageListener: ((response: WebSocketResponse) -> Unit)
): WebSocket {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    val webSocketListener = object : WebSocketListener() {
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            try {
                webSocket.close(1000, "")
            } catch (ex: Exception) {
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            try {
                val jsonList = text.split("""""")
                for (json in jsonList) {
                    val response = Gson().fromJson(json, WebSocketResponse::class.java)
                    if (response.target == target) onMessageListener.invoke(response)
                }
            } catch (ex: Exception) {
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("""{"protocol":"json","version":1}""")
        }
    }
    return client.newWebSocket(request, webSocketListener)
}

fun Fragment.setLocale(language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    resources.configuration.setLocale(locale)
    resources.updateConfiguration(resources.configuration, resources.displayMetrics)
}

fun openCustomChromeTab(context: Context, url: String) {
    val intent: CustomTabsIntent = CustomTabsIntent.Builder()
        .build()
    intent.launchUrl(context, Uri.parse(url))

}
fun isYesterday(timeInMillis:Long):Boolean = DateUtils.isToday(timeInMillis + DateUtils.DAY_IN_MILLIS)
