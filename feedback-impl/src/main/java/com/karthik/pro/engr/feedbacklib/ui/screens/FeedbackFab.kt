package com.karthik.pro.engr.feedbacklib.ui.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.karthik.pro.engr.feedbacklib.R
import com.karthik.pro.engr.feedbacklib.ui.viewmodel.FeedbackEvent
import com.karthik.pro.engr.feedbacklib.ui.viewmodel.FeedbackViewModel

@Composable
fun FeedbackFab(
    modifier: Modifier,
    feedbackViewModel: FeedbackViewModel,
    @StringRes feedbackMessageRes: Int = R.string.feedback_prompt,

) {
    FloatingActionButton(
        modifier = modifier,
        onClick = {
            feedbackViewModel.onEvent(FeedbackEvent.SendFeedback(feedbackMessageRes))
        }) {
        Icon(Icons.Filled.Feedback, contentDescription = "Send Feedback")
    }

}