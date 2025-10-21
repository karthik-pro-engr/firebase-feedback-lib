package com.karthik.pro.engr.feedback.impl

import com.karthik.pro.engr.feedback.api.FeedbackSender

class NoOpFeedback: FeedbackSender {
    override fun startFeedback(messageId: Int) {
        // No operation
    }
}