package com.karthik.pro.engr.feedback.impl

import android.widget.ViewSwitcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karthik.pro.engr.feedback.api.FeedbackSender
import com.karthik.pro.engr.feedback.impl.ui.viewmodel.FeedbackViewModel

class FeedbackViewModelFactory(private val feedbackSender: FeedbackSender) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
            return FeedbackViewModel(feedbackSender) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}