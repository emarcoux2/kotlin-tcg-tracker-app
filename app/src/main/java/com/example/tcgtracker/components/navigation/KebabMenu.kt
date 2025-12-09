package com.example.tcgtracker.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tcgtracker.destinations.Destination

/**
 * Some of the structure of this menu comes from:
 * https://developer.android.com/develop/ui/compose/components/menu
 *
 * Opens and displays a dropdown menu of additional options for the user.
 *
 * @param navController - NavController - The object responsible for navigation
 * between composable screens.
 * @param onShowMessage - String that displays a Snackbar message when certain
 * menu options are clicked.
 */
@Composable
fun KebabMenu(
    navController: NavController,
    onShowMessage: (String) -> Unit
) {
    var parentMenuExpanded by remember { mutableStateOf(false) }
    var childMenuExpanded by remember { mutableStateOf(false) }

    IconButton(onClick = { parentMenuExpanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
    }

    DropdownMenu(
        expanded = parentMenuExpanded,
        onDismissRequest = {
            parentMenuExpanded = false
            childMenuExpanded = false
        }
    ) {
        // if the user is signed in
        DropdownMenuItem(
            text = { Text("Account") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            onClick = {
                navController.navigate(Destination.Account.route)
            }
        )
        DropdownMenuItem(
            text = { Text("All Pokemon Cards") },
            leadingIcon = { Icon(Icons.Outlined.Menu, contentDescription = null) },
            onClick = {
                navController.navigate(Destination.AllPokemonCards.route)
            }
        )
        DropdownMenuItem(
            text = { Text("Other TCGs") },
            leadingIcon = { Icon(Icons.Outlined.ArrowDropDown, contentDescription = null) },
            onClick = {
                childMenuExpanded = true
            }
        )
        DropdownMenu(
            expanded = childMenuExpanded,
            onDismissRequest = { childMenuExpanded = false },
            offset = DpOffset(180.dp, 0.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Yu-Gi-Oh!",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                },
                onClick = {
                    onShowMessage("Coming Soon...")
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "Magic The Gathering",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                },
                onClick = {
                    onShowMessage("Coming Soon...")
                }
            )
        }

        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Sign In") },
            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            onClick = {  }
        )
        // if the user is signed in
        DropdownMenuItem(
            text = { Text("Log Out") },
            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            onClick = {  }
        )
    }
}