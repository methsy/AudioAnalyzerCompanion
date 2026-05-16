# Audio Analyzer Android Companion

Android companion app for AI-powered audio reference analysis.

## Features
- View analysis history from AWS backend
- Detailed metrics (LUFS, RMS, stereo width, etc.)
- AI-generated insights and recommendations
- Offline-friendly date formatting

## Tech Stack
- Kotlin
- Jetpack Compose (UI)
- Retrofit (API client)
- Coroutines (async)
- MockWebServer (testing)

## API
Connects to: `https://d2yyuk018lsehx.cloudfront.net`

## Testing
- Unit tests for Retrofit client
- MockWebServer for API simulation
- Edge case coverage (404, 500, malformed JSON)

## Screenshots
[Add screenshots here]

## License
MIT