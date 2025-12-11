package com.example.tcgtracker.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tcgtracker.MainActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Displays the sign-in screen.
 *
 * @param context - The Context used for accessing resources and system services.
 * @param modifier - Modifier for styling or layout adjustments.
 */
@Composable
fun SignInScreen(
    context: Context,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // sign in
        Button(
            onClick = {
                val trimmedEmail = email.trim()
                if (trimmedEmail.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                keyboardController?.hide()
                signInWithEmail(trimmedEmail, password, context) { isLoading = false }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Sign In")
        }

        // create account
        Button(
            onClick = {
                val trimmedEmail = email.trim()
                if (trimmedEmail.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                keyboardController?.hide()
                createAccount(trimmedEmail, password, context) { isLoading = false }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Create Account")
        }

        // guest
        Button(
            onClick = {
                isLoading = true
                keyboardController?.hide()
                signInAsGuest(context) { isLoading = false }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Continue as Guest")
        }
    }
}

// --- Firebase Functions ---
private fun signInWithEmail(email: String, password: String, context: Context, onComplete: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToMain(context, auth.currentUser?.uid)
            } else {
                Toast.makeText(context, "Sign In Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
            onComplete()
        }
}

private fun createAccount(email: String, password: String, context: Context, onComplete: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToMain(context, auth.currentUser?.uid)
            } else {
                Toast.makeText(context, "Account Creation Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
            onComplete()
        }
}

private fun signInAsGuest(context: Context, onComplete: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToMain(context, auth.currentUser?.uid)
            } else {
                Toast.makeText(context, "Guest Sign In Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
            onComplete()
        }
}

private fun navigateToMain(context: Context, userId: String?) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.putExtra("userID", userId)
    context.startActivity(intent)
}
