package com.karthik.pro.engr.feedbacklib

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FeedbackFab(modifier: Modifier) {
    FloatingActionButton(onClick = {}){
        Icon(Icons.Filled.Feedback, contentDescription = "Send Feedback")
    }

}