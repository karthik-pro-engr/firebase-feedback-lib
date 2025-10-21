package com.karthik.pro.engr.feedback.api.ui.screens

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.feedback.api.ui.viewmodel.FeedbackUiState
import com.karthik.pro.engr.feedback.api.R


@Composable
fun FeedbackStateText(
    modifier: Modifier = Modifier,
    feedbackUiState: FeedbackUiState
) {
    // lightweight debug log (safe, doesn't reference ViewModel class)
    Log.d(
        "FeedbackStateText",
        "recompose -> ${feedbackUiState.feedBackStateText} uiStateHash=${
            System.identityHashCode(
                feedbackUiState
            )
        }"
    )

    Text(
        text = feedbackUiState.feedBackStateText,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        textAlign = TextAlign.Center
    )
}

/**
 * Minimal FAB composable that is independent of ViewModel or resource ids.
 *
 * Consumer should pass the proper callback (and message resource if needed).
 */
@Composable
fun FeedbackFab(
    modifier: Modifier = Modifier,
    onSendFeedback: (messageResId: Int) -> Unit,
    @StringRes defaultMessageRes: Int = R.string.feedback_prompt
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { onSendFeedback(defaultMessageRes) }
    ) {
        Icon(Icons.Filled.Feedback, contentDescription = "Send Feedback")
    }
}
