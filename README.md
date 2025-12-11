*TCG Tracker*

TCG Tracker is a Pokémon Trading Card Game companion app built with Kotlin, Jetpack Compose, 
and Room. It allows users to browse all Pokémon cards, manage personal collections, and 
interact with card sets and series. Authentication is handled with Firebase Auth, and data 
is sourced from the TCGdex API.

Features:
- Browse all Pokémon cards with detailed information.
- View Pokémon card sets and series.
- Manage a personal card collection.
- Mark cards as favorites.
- Search for cards by name.
- Firebase Authentication for login, sign-up, and anonymous guest sessions.
- Room database for offline caching.
- Modular MVVM architecture.

Note: Some features, like OCR Technology and database functionality are currently under 
development.

*Architecture Overview*

The app is organized into the following packages:

1. Api
- Handles communication with the remote TCGdex API.
Contains:
- PokemonTCGdexService – Fetches cards, sets, and series from the API.
- Data models for API responses.

2. MVVM
- ViewModel layer managing UI state and business logic. 
Contains:
- PokemonCardsViewModel – Manages all cards screen.
- PokemonCardSetsViewModel – Manages card sets.
- PokemonCardSeriesViewModel – Manages card series.
- AddPokemonCardToCollectionViewModel – Handles adding cards to user collection.
- MyPokemonCardsViewModel – Handles the user’s personal collection.

3. Repository
Central data source orchestrating API, Room, and Firebase.
Contains:
PokemonCardRepository – Single source of truth for cards and collections.

4. Navigation
Handles app navigation using Jetpack Compose NavHostController.
NavGraph or NavHost definitions for all composable screens.

5. OCR Scanner (Not implemented)
Package reserved for future feature to scan Pokémon cards using OCR.

6. AppDatabase (Room)
Local database using Room for offline caching.
DAOs: ApiPokemonCardDao, UserPokemonCardDao
Entities: ApiPokemonCardEntity, UserPokemonCardEntity, PokemonCardSetEntity, PokemonCardSerieEntity.

7. Firebase Authentication
- Manages user authentication.
- Email/password sign-in
- Account creation
- Guest login

8. Destinations
Optional: Could include constants or helpers for navigation routes.

9. Screens
- Composable screens for the app UI:
- AllPokemonCardsScreen
- PokemonCardDetailsScreen
- MyPokemonCardsScreen
- AddPokemonCardsToCollectionScreen
- Account and Favorites screens

*Architecture:*
- MVVM (Model-View-ViewModel) with Jetpack Compose.
- ViewModels manage UI state and coordinate between the UI and data layers.
- NavController for Compose navigation between screens.

*Getting Started*
Prerequisites
- Android Studio Flamingo or later
- Kotlin 1.9+
- Firebase project setup
- TCGdex API (if API keys are required)

*Installation*
- Clone the repository:
- git clone https://github.com/yourusername/tcg-tracker.git
- Open in Android Studio.
- Add your Firebase google-services.json to app/.
- Sync Gradle and run the app on a device or emulator.

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

app/
├─ api/                # Remote API service & models
├─ mvvm/               # ViewModels
├─ repository/         # Repository layer
├─ navigation/         # NavHost & routes
├─ ocrscanner/         # Future OCR feature
├─ database/           # Room Database & DAOs
├─ firebase/           # Authentication utilities
├─ destinations/       # Navigation constants or helpers
├─ screens/            # Composable screens

*Contributing*
- Fork the repository
- Create your feature branch (git checkout -b feature/foo)
- Commit your changes (git commit -am 'Add foo')
- Push to the branch (git push origin feature/foo)
- Open a pull request
- License

MIT License © 2025 Elizabeth Marcoux