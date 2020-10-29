package com.quicklycoding.rocketkotlin

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Extension method to simplify view binding.
 */

fun Double.toRoundOffDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toDouble()
}


fun Context.hideSoftKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isAcceptingText) imm.hideSoftInputFromWindow(view.windowToken, 0)
}

val String.toHtml get() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)

val <T : Any> T.TAG get() = this::class.simpleName

// set arguments for DialogFragment
inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T = this.apply {
    arguments = Bundle().apply(argsBuilder)
}

//Toast
fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, message, duration).show()

fun Context.toast(message: Int, duration: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, message, duration).show()

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) =
    Toast.makeText(requireContext(), message, duration).show()

fun Fragment.toast(message: Int, duration: Int = Toast.LENGTH_LONG) =
    Toast.makeText(requireContext(), message, duration).show()


// Copy
fun Context.copy(text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", text)
    clipboard.setPrimaryClip(clip)
    toast("Copy Successfully")
}


fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

// Share
fun share(context: Context, message: String, appId: String? = null) {
//    Log.d("ExtensionFun", "share: $context, $message, $appId")

    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, message)
    intent.putExtra(Intent.EXTRA_TITLE, message)

    when {
        appId.isNullOrEmpty() -> context.startActivity(intent)

        else -> {
            if (context.isPackageInstalled(appId)) {
                intent.setPackage(appId)
                context.startActivity(intent)
            } else context.toast("App not installed")
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
fun Context.specificWhatsAppMessage(mobile: String) {
    if (isPackageInstalled("com.whatsapp")) {
        Intent("android.intent.action.MAIN").apply {
            component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            //phone number without "+" prefix
            putExtra("jid", PhoneNumberUtils.stripSeparators("91$mobile") + "@s.whatsapp.net")
            startActivity(this)
        }
    } else toast("App Not Installed")
}


fun Context.dialer(mobile: String? = null) = Intent(Intent.ACTION_DIAL).run {
    data = Uri.parse("tel:$mobile")
    startActivity(this)
}

fun sms(context: Context, mobile: String?, message: String? = null) = try {
    val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$mobile"))
    smsIntent.putExtra("sms_body", message)
    context.startActivity(smsIntent)
} catch (e: ActivityNotFoundException) {
}

// Send Email
fun Context.email(emails: Array<String>, subject: String, message: String? = null) {
    with(Intent(Intent.ACTION_SENDTO)) {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, emails)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)
        if (resolveActivity(packageManager) != null) {
            startActivity(this)
        }
    }
}


//Open URL
fun Context.openURL(url: String) = startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))

