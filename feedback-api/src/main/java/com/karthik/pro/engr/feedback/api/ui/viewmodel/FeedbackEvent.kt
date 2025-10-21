package com.karthik.pro.engr.feedback.api.ui.viewmodel

sealed class FeedbackEvent {
    data class SendFeedback(val messageId: Int) : FeedbackEvent()
}