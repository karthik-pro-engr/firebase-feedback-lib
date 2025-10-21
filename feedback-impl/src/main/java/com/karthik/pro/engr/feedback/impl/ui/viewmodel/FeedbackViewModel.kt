package com.karthik.pro.engr.feedback.impl.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthik.pro.engr.feedback.api.FeedbackSender
import com.karthik.pro.engr.feedback.api.ui.viewmodel.FeedbackEvent
import com.karthik.pro.engr.feedback.api.ui.viewmodel.FeedbackUiEffect
import com.karthik.pro.engr.feedback.api.ui.viewmodel.FeedbackUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackViewModel(private val feedbackSender: FeedbackSender) : ViewModel() {
    private val _uiEffect = MutableSharedFlow<FeedbackUiEffect.LaunchEmail>(
        0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEffect = _uiEffect.asSharedFlow()

    private val _uiState = MutableStateFlow(
        FeedbackUiState()
    )
    val uiState = _uiState.asStateFlow()


    fun startFeedback(feedbackMessage: Int) {
        viewModelScope.launch {
            setSubmitting("Opening Feedback")
            try {
                feedbackSender.startFeedback(feedbackMessage)
            } catch (t: Throwable) {
                _uiEffect.emit(FeedbackUiEffect.LaunchEmail)
            } finally {
                setSubmitting("Hello testers — tap FAB to send feedback")
            }
        }


    }

    fun setSubmitting(submittingMessage: String) {
        _uiState.update {
            Log.d("FeedbackVM", "setSubmitting on vm@${System.identityHashCode(this)} -> $submittingMessage")
            it.copy(feedBackStateText = submittingMessage)
        }

    }

    fun launchFeedbackEmail(context: Context, to: String = "karthik.pro.engr@gmail.com") {
        val pkg = context.packageName
        val pm = context.packageManager

        // app version info (safe access)
        val (versionName, versionCode) = try {
            val pi = pm.getPackageInfo(pkg, 0)
            val vName = pi.versionName ?: "unknown"
            val vCode =
                pi.longVersionCode.toString()
            vName to vCode
        } catch (t: Throwable) {
            "unknown" to "unknown"
        }

        // device & system info
        val androidVersion = "${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"
        val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}".trim()
        val locale = Locale.getDefault().toLanguageTag()
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val subject = "Beta feedback — $pkg (v:$versionName)"
        val body = """
        Please describe the issue and steps to reproduce.

        App package: $pkg
        App version: $versionName (code: $versionCode)
        Android version: $androidVersion
        Device model: $deviceModel
        Locale: $locale
        Timestamp: $timestamp

        Steps to reproduce:

        Expected:

        Actual:
    """.trimIndent()

        // 1) Preferred: ACTION_SENDTO with mailto (only email apps)
        val sendToIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri() // no direct recipients here
            putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        // 2) Fallback: ACTION_SEND with rfc822 (broad but widely supported)
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        // Use chooser and resolve handlers
        val chooser: Intent? = when {
            sendToIntent.resolveActivity(pm) != null -> Intent.createChooser(
                sendToIntent,
                "Send feedback using"
            )

            sendIntent.resolveActivity(pm) != null -> Intent.createChooser(
                sendIntent,
                "Send feedback using"
            )

            else -> null
        }

        if (chooser != null) {
            // If context isn't an Activity, ensure we can start the chooser.
            if (context !is Activity) chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } else {
            Toast.makeText(context, "No email app installed to send feedback", Toast.LENGTH_SHORT)
                .show()
            Log.w("Feedback", "No activity found to handle email intent")
        }
    }

    fun onEvent(event: FeedbackEvent) {
        when (event) {
            is FeedbackEvent.SendFeedback -> {
                setSubmitting("From SendFeedback Event")
                startFeedback(event.messageId)
            }
        }

    }
}