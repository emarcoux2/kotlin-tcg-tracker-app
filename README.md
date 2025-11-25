Created using Kotlin + Jetpack Compose, I created TCG Tracker, an app that's using OCR (Optical Character Recognition), storing cards locally, fetching card information from an API, and saving a logged-in user's personal collection to a database (most likely Room, but will be implemented in the future).

Features:
- Scan physical cards with the device camera.
- Extract text from scanned cards using OCR.
- Store scanned cards in a local database.
- Fetch card details from an API.
- Navigate between card lists, card details, and sets using Jetpack Compose navigation.
- Login authentication, where a logged-in user has access to the cards they scanned, as well as add cards to a Favourites list.

Note: Some features (OCR, API integration, login functionality, database functionality) are currently under development.

Screens:
- ScanCards - scan cards and display extracted text.
- AllPokemonCards - view all cards uploaded from the API.
- CardDetails - view detailed information about an individual card.
- CardSets - browse card sets
- CardSetDetails - view detailed information about a card set.
- Account - a logged-in user can view and edit their account details.
- MyPokemonCards - a logged-in user's personal collection of scanned cards.
- FavouriteCards - a logged-in user's favourite cards.

Architecture:
- MVVM (Model-View-ViewModel) with Jetpack Compose.
- ViewModels manage UI state and coordinate between the UI and data layers.
- NavController for Compose navigation between screens.

Getting Started
Prerequisites:
- Android Studio Flamingo or newer.
- Kotlin 1.9+
- Internet connection for API access.

Setup:
- Clone the repository:
- git clone https://github.com/emarcoux2/kotlin-tcg-tracker-app
- cd kotlin-tcg-tracker-app
- Open in Android Studio.
- Build the project (Build > Make Project).
- Run on an emulator or device with a camera.

Usage:
- Open the app.
- Navigate to the Scan Cards screen.
- Use the camera to capture a card.
- Extracted text is displayed and saved in the local database.
- Browse all cards and view details.
- Logged-in users can view manually added cards.
- Add a card to the favourites list for easy viewing.

Planned Features:
- Full OCR implementation for card scanning.
- Integration with Pokémon TCG API for card and set details.
- Bottom navigation for logged-in users.
- Database functionality.
- Log in functionality.
- Favourites and search functionality for scanned cards.
