package com.quicklycoding.rocketkotlin.util

import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.quicklycoding.rocketkotlin.R
import kotlinx.android.synthetic.main.custom_toast.view.*

fun String.toHTML() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)

// set arguments for DialogFragment
inline fun <T : Fragment> T.withArgs(
    argsBuilder: Bundle.() -> Unit
): T = this.apply {
    arguments = Bundle().apply(argsBuilder)
}

//Toast
fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

//Toast
fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast(context)

    val view = layoutInflater.inflate(R.layout.custom_toast, null)
    view.text_view_msg.text = message

    toast.view = view
    toast.duration = duration
    toast.show()
}

// Copy
fun Context.copyText(text: Any) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", "" + text)
    clipboard.setPrimaryClip(clip)
}

// Share
fun Context.shareText(title: String, message: String, appPackage: String? = null) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, message)
    }

    when {
        appPackage.isNullOrEmpty() -> {
            startActivity(intent)
        }
        isPackageInstalled(appPackage) -> {
            intent.setPackage(appPackage)
            startActivity(intent)
        }
        else -> {
            toast("App not installed")
        }
    }
}


// Check Package Installed
fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

// Send WhatsApp Message
fun Context.specificWhatsAppMessage(mob: String) {
    if (isPackageInstalled(PACKAGE_WHATSAPP)) {
        Intent("android.intent.action.MAIN").apply {
            component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            //phone number without "+" prefix
            putExtra("jid", PhoneNumberUtils.stripSeparators("91$mob") + "@s.whatsapp.net")
            startActivity(this)
        }
    } else toast("App Not Installed")
}


fun Context.openDialer(mobile: String? = null) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$mobile")
    startActivity(intent)
}

fun Context.openMessageApp(mobile: String?, message: String? = null) {
    try {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("address", "$mobile")
        smsIntent.putExtra("sms_body", message)
        startActivity(smsIntent)
    } catch (e: ActivityNotFoundException) {
    }

}

// Send Email
fun Context.sendEmail(email: String, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, "")
    startActivity(Intent.createChooser(intent, "Choose an Email client :"))
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.beginTransaction()
        .add(frameId, fragment)
        .commit()
}


fun Fragment.addFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.beginTransaction()
        .add(frameId, fragment)
        .commit()
}


// View Visible
fun View.show() {
    visibility = View.VISIBLE
}

// View Invisible
fun View.hide() {
    visibility = View.GONE
}

// hide keyboard
fun View.hideKeyboard() {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

//snackbar
fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration)
        .setAction("X") {}
        .setActionTextColor(Color.WHITE)
        .setBackgroundTint(Color.DKGRAY)
        .show()
}

/*
*
*
* LiveData : Extension
*
* */
class NonNullMediatorLiveData<T> : MediatorLiveData<T>()

fun <T> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    val mediator: NonNullMediatorLiveData<T> = NonNullMediatorLiveData()
    mediator.addSource(this) { it?.let { mediator.value = it } }
    return mediator
}


fun <T> NonNullMediatorLiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}