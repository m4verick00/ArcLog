# ArcLogbook - Defensive Security Toolkit for Android

ArcLogbook is a privacy-first, local-first defensive security toolkit designed for Android devices. It provides comprehensive incident logging, threat intelligence aggregation, vulnerability tracking, metadata extraction, and AI-assisted analysis capabilities.

## ⚠️ Legal & Ethical Notice

**IMPORTANT**: ArcLogbook is designed for authorized security research, incident response, and defensive security operations only. Users are responsible for ensuring all activities comply with local laws, regulations, and authorized usage policies. Any active testing capabilities require explicit authorization and are gated behind Expert Mode with consent dialogs.

## 🔒 Privacy & Security

- **Local-first architecture**: All data stored locally with SQLCipher encryption
- **No telemetry**: Zero data collection or transmission without explicit user consent
- **Biometric protection**: Optional biometric authentication for app access
- **Panic mode**: Quick data wipe/lock functionality
- **Network isolation**: Module-level network permissions with optional VPN/Tor routing

## 🏗️ Architecture

### Multi-Module Structure
- `app/` - Main application module
- `core/common/` - Shared UI components and utilities
- `core/database/` - Room database with SQLCipher encryption
- `core/network/` - Retrofit networking with security configurations
- `feature/*` - Individual feature modules (logbook, intel, metadata, etc.)

### Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: Clean Architecture + MVVM
- **Database**: Room + SQLCipher
- **DI**: Hilt
- **Background**: WorkManager
- **Networking**: Retrofit + OkHttp

## 🚀 Features

### 📝 Incident Response & Logbook
- Structured incident logging with timelines
- Playbook engine with progress tracking
- IOC (Indicators of Compromise) management
- Evidence attachment and export capabilities

### 🛡️ Threat Intelligence Aggregation
- Free threat intel feeds (CISA KEV, CERT advisories)
- Automatic deduplication and severity scoring
- Watchlist management for assets and indicators
- Real-time notifications for high-priority threats

### 🔍 Vulnerability Management
- CVE tracking and patch management
- Kanban-style workflow (Assess → Plan → Patch → Verify)
- Integration with threat intelligence
- Automated reminder system

### 📊 Metadata Extraction (Forensic-Grade)
- **EXIF/XMP/IPTC extraction** from images, documents, videos
- **GPS location processing**:
  - Clickable Google Maps integration
  - Offline reverse geocoding (bundled cities database)
  - Optional online geocoding via Nominatim
- **Hash generation**: SHA-256, MD5, perceptual hashes (aHash, pHash, PDQ)
- **Evidence bundling**: One-tap export with metadata, hashes, and reports
- **PII risk assessment**: Automatic privacy risk scoring
- **Timeline and map views**: Visual organization by location and time
- **OCR and barcode extraction**: ML Kit integration for IOC extraction

### 🤖 AI Assistant (On-Device)
- Local LLM integration (GGUF format, 3-4B models)
- Log analysis and IOC extraction
- Report generation and automation
- AI Actions API with user confirmation
- RAG (Retrieval-Augmented Generation) over local data

### 🔗 External Service Connectors
- Pluggable architecture for external services (Shodan, Censys, etc.)
- Secure credential management with Android Keystore
- OAuth and API key support with automatic token refresh

### 📱 Widgets & Notifications
- Android Glance widgets for status monitoring
- High-priority threat notifications
- Quick action shortcuts

### 📤 Export & SIEM Integration
- Multiple export formats (JSONL, CSV, PDF)
- Local webhook integration for SIEM systems
- CEF and syslog-compatible output

## 🎨 User Experience

### Three-Tier Visibility System
Every screen and component supports three view modes:
1. **Simple** - Plain-language summaries for non-technical users
2. **Advanced** - Technical details with visualizations
3. **Expert** - Full raw data, headers, payloads, and logs

### Premium Motion Design
- 60/120 fps animations with `AnimatedContent` and `AnimatedVisibility`
- Material 3 design system with dynamic colors
- Smooth state transitions and micro-interactions
- Haptic feedback integration

## 🔧 Build Instructions

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK 26+ (Android 8.0+)
- Kotlin 1.9.10+

### Building the Project
```bash
# Clone the repository (when available)
git clone [repository-url]
cd ArcLogbook

# Build debug APK
./gradlew assembleDebug

# Build both flavors
./gradlew assembleFossDebug assembleFullDebug

# Run tests
./gradlew test
./gradlew connectedAndroidTest

# Generate baseline profiles (performance optimization)
./gradlew generateBaselineProfile
```

### APK Outputs
Built APKs are placed in the `apk/` directory:
- `apk/ArcLogbook-debug.apk` - Debug build
- `apk/ArcLogbook-release-unsigned.apk` - Release build (unsigned)

### Flavors
- **FOSS**: Open-source only dependencies, no proprietary services
- **Full**: Includes optional cloud connectors and proprietary integrations

## 🧪 Testing

### Test Coverage
- Unit tests for repositories, parsers, and ViewModels
- Instrumented tests for database operations
- UI tests for critical user flows
- Macrobenchmark tests for performance validation

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# UI tests
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.arclogbook.app.ui.UiTestSuite
```

## 🔒 Security Considerations

### Data Protection
- All sensitive data encrypted at rest using SQLCipher
- Credentials stored in Android Keystore
- Optional screenshot blocking for sensitive screens
- Clipboard protection with automatic clearing

### Network Security
- TLS 1.3+ for all external communications
- Certificate pinning for critical endpoints
- Network Security Config enforcing HTTPS
- Per-module network permission controls

### Authentication
- Biometric authentication support
- Session timeout controls
- Multi-factor authentication ready

## 📱 Device Requirements

### Minimum Requirements
- Android 8.0 (API 26)
- 2GB RAM
- 100MB storage space
- Internet connection (optional, for threat intel updates)

### Recommended
- Android 12+ (API 31+) for best performance
- 4GB RAM for AI assistant features
- Biometric authentication hardware
- Google Maps app installed (for location features)

## 🤝 Contributing

### Code Style
- Follow Android Kotlin style guide
- Use KTLint for code formatting
- Run Detekt for static analysis
- Maintain test coverage above 80%

### Modules
Each feature should be implemented as a separate module with:
- Clean architecture layers (domain, data, ui)
- Dependency injection with Hilt
- Comprehensive test coverage
- Documentation for public APIs

## 📄 License

[License information to be determined]

## ⚖️ Disclaimer

ArcLogbook is provided for educational and authorized security research purposes only. Users assume full responsibility for compliance with applicable laws and regulations. The developers are not responsible for misuse of this tool.

---

**Target Device**: Optimized for Pixel 7 and similar flagship Android devices
**Performance**: <2.5s cold start, <1% jank, smooth 60/120fps animations
**Privacy**: Local-first, no telemetry, user-controlled data sharing