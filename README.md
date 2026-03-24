# Rick and Morty Explorer

Rick and Morty Explorer is an Android application built with Kotlin and Jetpack Compose 
that allows users to browse characters from the Rick and Morty API, view detailed information, and manage favorite characters locally.

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Koin
- Ktor
- Room
- Coroutines / Flow
- Coil

## Features

### Character List
- Fetches characters from the Rick and Morty API
- Search by name
- Filter by status and gender
- Infinite scroll / pagination
- Character cards with image, name and status

### Character Details
- Detailed screen with:
  - name
  - image
  - status
  - species
  - gender
  - origin
  - location
  - episodes count
- Add / remove favorites

### Favorites
- Favorites persisted locally with Room
- Dedicated favorites screen
- Search within favorites
- Filter favorites by status and gender
- Sort favorites by name

## Architecture

The project follows a layered architecture:

- **data**: API, local database, mappers, repository implementation
- **domain**: models, repository contracts, use cases
- **presentation**: screens, components, UI state, ViewModels
- **di**: dependency injection modules
- **navigation**: navigation graph and destinations

This separation improves maintainability, readability and scalability.

## Technical Decisions

- **Koin** was used for dependency injection to keep the setup simple and readable.
- **Ktor** was used for networking, aligned with the requested stack.
- **Room** is used to persist favorite characters locally.
- **Manual pagination** was implemented to keep the prototype simple and reliable within the challenge timeframe.
- **Dark/Light mode** is supported automatically based on system theme.
- Filters were intentionally kept simple (**status** and **gender**) to meet the challenge requirements while keeping the UI straightforward and maintainable.
- Favorite filtering and sorting were implemented with simple local UI state, which keeps the feature easy to understand and evolve.
- Empty states and API error handling were treated explicitly, so searches with no results display a friendly message instead of exposing technical exceptions to the user.

## Testing

The project includes unit tests for key functionalities, including:
- `GetCharactersUseCase`
- `CharacterListViewModel`

## Bonus Features Implemented

- Dark / Light mode
- Local persistence for favorites with Room
- Image loading with Coil, benefiting from built-in image caching

## Error Handling

- Searches with no matching characters are treated as an empty state (`No characters found`)
- Favorite searches with no matches display a friendly empty state
- API rate limiting and unexpected responses are handled with controlled UI feedback

## Possible Future Improvements

- Add offline cache for the remote character list
- Expand filter options (species, status, gender and other supported API fields)
- Improve loading, error and empty states further
- Add UI / integration tests
- Improve accessibility semantics and content descriptions

## API

This project uses the public Rick and Morty API:

- `https://rickandmortyapi.com/`

## How to Run

1. Open the project in Android Studio
2. Sync Gradle files
3. Run the app on an emulator or Android device

## Notes

This project was developed as a technical challenge with focus on:
- code quality
- architecture
- maintainability
- prioritization of core functionality