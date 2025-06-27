# DocOps MCP Server

A Spring Boot application that provides visualization services for Architecture Decision Records (ADRs) and Feature Cards using the Model Context Protocol (MCP).

## Overview

This project is a server implementation that connects to the DocOps visualization service at [roach.gy](https://roach.gy) to generate SVG-based visualizations. It exposes AI tools through Spring AI's Model Context Protocol that can be used to create:

- Architecture Decision Records (ADRs)
- Feature Card visualizations

## Features

### Architecture Decision Records (ADRs)

Create standardized Architecture Decision Records with:
- Title and date
- Status (PROPOSED, ACCEPTED, SUPERSEDED, DEPRECATED, REJECTED)
- Context, decision, and consequences sections
- Optional participants and references

### Feature Cards

Generate visual feature cards with:
- Title and description
- Emoji and color scheme
- Detailed bullet points
- Various layout options (GRID, COLUMN, ROW)
- Theme support (LIGHT, DARK, AUTO)

## Getting Started

### Prerequisites

- JDK 21 or higher
- Maven

### Installation

1. Clone the repository
2. Build the project with Maven:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```

## Usage

The services are exposed as Spring AI tools that can be used through the Model Context Protocol. Example usage:

### Creating an ADR

```kotlin
// Example code for creating an ADR
val adrService = AdrService()
val adr = AdrService.AdrData(
    title = "Use Kotlin for Backend Development",
    date = LocalDate.now(),
    status = AdrService.AdrStatus.ACCEPTED,
    context = listOf("We need to choose a programming language for our backend services"),
    decision = listOf("We will use Kotlin for all new backend development"),
    consequences = listOf("Improved null safety", "Better interoperability with Java")
)
val svgResponse = adrService.makeAdr(adr)
```

### Creating Feature Cards

```kotlin
// Example code for creating Feature Cards
val featureCardService = FeatureCardService()
val featureCards = listOf(
    FeatureCardService.FeatureCardData(
        title = "Kotlin Support",
        description = "First-class Kotlin language support",
        emoji = "🚀",
        colorScheme = "BLUE",
        details = listOf("Null safety", "Coroutines", "Extension functions")
    )
)
val svgResponse = featureCardService.featureCard(featureCards, theme = "DARK", layout = "GRID")
```

## References

* [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html)
* [Spring Boot Documentation](https://docs.spring.io/spring-boot/3.5.3/maven-plugin)

## License

This project is licensed under the terms of the license included in the repository.
