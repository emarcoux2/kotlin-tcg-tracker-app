package com.example.tcgtracker.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tcgtracker.MainActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Composable that displays the sign-in screen for the app.
 *
 * This screen allows users to:
 * Sign in with email and password.
 * Create a new account with email and password.
 * Continue as a guest (anonymous sign-in).
 *
 * It provides input fields for email and password, along with buttons for each action.
 *
 * Shows loading indicators while authentication is in progress and displays error
 * messages via Toasts.
 *
 * @param onSignInSuccess - Lambda invoked when the user successfully signs in or creates an account.
 * @param modifier - Optional Modifier for styling or layout adjustments.
 *
 * State:
 * email: Current email input.
 * password: Current password input.
 * isLoading: Tracks whether an authentication request is in progress.
 *
 * Notes:
 * Uses FirebaseAuth for authentication.
 * Hides the software keyboard when buttons are pressed.
 * Navigates to MainActivity on successful sign-in or account creation.
 *
 */
@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
                    Toast.makeText(
                        context,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                isLoading = true
                keyboardController?.hide()
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(trimmedEmail, password)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) onSignInSuccess()
                        else Toast.makeText(
                            context,
                            "Sign In Failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
                    Toast.makeText(
                        context,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                isLoading = true
                keyboardController?.hide()
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(trimmedEmail, password)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) onSignInSuccess()
                        else Toast.makeText(
                            context,
                            "Account Creation Failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
                val auth = FirebaseAuth.getInstance()
                auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) onSignInSuccess()
                        else Toast.makeText(
                            context,
                            "Guest Sign In Failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Continue as Guest")
        }
    }
}

/**
 * Signs in a user with email and password using Firebase Authentication.
 *
 * @param email - The user's email address.
 * @param password - The user's password.
 * @param context - Context for displaying Toasts and navigating to MainActivity.
 * @param onComplete - Callback invoked after the sign-in attempt completes, regardless of success.
 *
 * On success, navigates to MainActivity with the signed-in user's ID.
 * On failure, displays a Toast with the error message.
 */
private fun signInWithEmail(email: String, password: String, context: Context, onComplete: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToMain(context, auth.currentUser?.uid)
            } else {
                Toast.makeText(
                    context,
                    "Sign In Failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            onComplete()
        }
}

/**
 * Creates a new Firebase account with the provided email and password.
 *
 * @param email - The user's email address.
 * @param password - The user's password.
 * @param context - Context for displaying Toasts and navigating to MainActivity.
 * @param onComplete - Callback invoked after the account creation attempt completes,
 * regardless of success.
 *
 * On success, navigates to MainActivity with the newly created user's ID.
 * On failure, displays a Toast with the error message.
 */
private fun createAccount(email: String, password: String, context: Context, onComplete: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToMain(context, auth.currentUser?.uid)
            } else {
                Toast.makeText(
                    context,
                    "Account Creation Failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            onComplete()
        }
}

/**
 * Signs in the user anonymously using Firebase Authentication.
 *
 * @param context - Context for displaying Toasts and navigating to MainActivity.
 * @param onComplete - Callback invoked after the anonymous sign-in attempt completes, regardless of success.
 *
 * On success, navigates to MainActivity with the anonymous user's ID.
 * On failure, displays a Toast with the error message.
 */
private fun signInAsGuest(context: Context, onComplete: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToMain(context, auth.currentUser?.uid)
            } else {
                Toast.makeText(
                    context,
                    "Guest Sign In Failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            onComplete()
        }
}

/**
 * Navigates to MainActivity after successful authentication.
 *
 * @param context - Context used to start the activity.
 * @param userId - The authenticated user's ID, passed as an intent extra.
 *
 * Clears the back stack and starts a new task to prevent the user from
 * returning to the sign-in screen.
 */
private fun navigateToMain(context: Context, userId: String?) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.putExtra("userID", userId)
    context.startActivity(intent)
}
