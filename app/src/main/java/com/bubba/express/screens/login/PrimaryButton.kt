package com.bubba.express.screens.login

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(text)
        }
    }
}
