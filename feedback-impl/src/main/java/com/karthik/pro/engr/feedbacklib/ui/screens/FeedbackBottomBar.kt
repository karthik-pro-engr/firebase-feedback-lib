package com.karthik.pro.engr.feedbacklib.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.karthik.pro.engr.feedbacklib.ui.viewmodel.FeedbackUiState

@Composable
fun FeedbackStateText(
    modifier: Modifier,
    feedbackUiState: FeedbackUiState
) {
    Log.d(
        "FeedbackStateText",
        "recompose -> ${feedbackUiState.feedBackState} vmHash=${
            System.identityHashCode(
                feedbackUiState
            )
        }"
    )
    Text(
        feedbackUiState.feedBackState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        textAlign = TextAlign.Center
    )
}
