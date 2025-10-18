package com.karthik.pro.engr.feedbacklib.ui.viewmodel

sealed class FeedbackEvent {
    data class SendFeedback(val message: Int) : FeedbackEvent()
}