# VideoSearch Project

The VideoSearch project is a Java application designed to search and retrieve information about TV shows and series. It includes both a backend and frontend component for efficient searching and filtering of shows.

## Features

- **Search by Title Word**: Search for TV shows using specific keywords in their titles.

- **Search by Year First Produced**: Search for TV shows by the year they were first produced.

- **Filter by Streaming Provider**: Filter shows based on streaming providers like Netflix, Hulu, Prime Video, and Disney+.

- **Caching and History**: The application caches search results and maintains a history of keywords for efficient retrieval.

## Project Structure

The project is organized into several packages, each with specific functionality:

- `com.demo.videosearch.model`: Contains classes related to defining TV show properties.
  - `Show.java`: Represents a TV show.
  - `IShow.java`: Interface for defining TV show properties.

- `com.demo.videosearch.util`: Houses utility classes and functions used across the project.
  - `CommonUtils.java`: Common utility functions.
  - `RedisQuery.java`: Utility for Redis database queries.

- `com.demo.videosearch.db`: Handles database-related functionality.
  - `JedisDBPool.java`: Manages connections to the Redis database.
  - `HashtableMap.java`: Implementation of a hashtable for mapping keys to values.
  - `HashTableSortedSets.java`: Implementation of a sorted hashtable for mapping keys to sorted sets of values.
  - `IHashTableSortedSets.java`: Interface for sorted hashtable functionality.

- `com.demo.videosearch.service`: Contains service classes for loading and searching shows.
  - `ShowLoader.java`: Loads TV show data.
  - `IShowLoader.java`: Interface for loading TV show data.
  - `ShowSearcherBackend.java`: Implements the backend logic for searching and filtering shows.
  - `IShowSearcherBackend.java`: Interface for the backend of the show search functionality.

- `com.demo.videosearch.ui`: Handles the user interface components.
  - `ShowSearcherFrontend.java`: Represents the frontend interface for interacting with the application.
  - `IShowSearcherFrontend.java`: Interface for the frontend of the show search functionality.

- `com.demo.videosearch.app`: Contains the main application entry point.
  - `ShowSearcherApp.java`: The main class to start the application.

## Usage

To use the VideoSearch application, you can follow these steps:

1. **Compile and Run**: Compile and run the application to start the search interface.

2. **Search by Title Word**: Use the option to search for TV shows by entering specific keywords.

3. **Search by Year First Produced**: Search for shows by the year they were first produced.

4. **Filter by Streaming Provider**: Filter shows by streaming providers.

5. **View History and Cache**: You can access the search history and cached results for improved search efficiency.

## Requirements

- Java Development Kit (JDK)
- Redis (for caching)

## Contributing

Contributions to the VideoSearch project are welcome. Feel free to open issues, submit pull requests, or provide feedback.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
