package com.karthik.pro.engr.feedback.impl

import com.google.firebase.appdistribution.FirebaseAppDistribution
import com.karthik.pro.engr.feedback.api.FeedbackSender

class FirebaseFeedbackSender: FeedbackSender {
    override fun startFeedback(messageId: Int) {
        FirebaseAppDistribution.getInstance().startFeedback(messageId)
    }
}