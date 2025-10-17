package com.karthik.pro.engr.feedbacklib

import com.google.firebase.appdistribution.FirebaseAppDistribution

object FeedbackHelper {

    fun startFeedback(x: Any): {

        FirebaseAppDistribution.getInstance().startFeedback(R.string.feedback_prompt)


    }
}