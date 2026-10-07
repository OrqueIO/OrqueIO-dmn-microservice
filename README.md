# OrqueIO DMN Microservice

A lightweight Spring Boot microservice that uses the **OrqueIO DMN Engine** (standalone) to evaluate a decision table for travel recommendations. No database required.

Given a season, budget, number of travelers, and whether children are present, the service recommends a **destination** and a list of **activities**.

## Prerequisites

* Java 25
* Maven 3.8+

## Setup

1. Clone the repository
2. Build and run:
```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## API

### GET /travel

Recommends a destination and activities based on input parameters.

**Parameters:**

| Parameter | Type | Values |
|---|---|---|
| `season` | string | `Summer`, `Winter`, `Spring`, `Fall` |
| `budget` | string | `Low`, `Medium`, `High` |
| `nbTravelers` | integer | any positive number |
| `withChildren` | boolean | `true`, `false` |

**Example request:**
```bash
curl "http://localhost:8080/travel?season=Summer&budget=Medium&nbTravelers=2&withChildren=false"
```

**Example response:**
```json
{
  "destination": "Santorini",
  "activities": ["Boat Cruise", "Local Cuisine"],
  "evaluationTime": "5 ms"
}
```

## Decision Table

A single DMN table (`travelRecommendations.dmn`) with **COLLECT** hit policy that returns all matching rules.

### Destinations

| Season | Budget | Travelers | Destination |
|---|---|---|---|
| Summer | Low | any | Marrakech |
| Summer | Medium | <= 4 | Santorini |
| Summer | Medium | > 4 | Antalya |
| Summer | High | any | Maldives |
| Winter | Low | any | Istanbul |
| Winter | Medium/High | <= 4 | Swiss Alps |
| Winter | Medium/High | > 4 | Lapland |
| Spring | Low | any | Lisbon |
| Spring | Medium/High | any | Kyoto |
| Fall | Low | any | Tunis |
| Fall | Medium | any | Rome |
| Fall | High | any | New York |

### Activities

Each destination has a specific activity. Two additional rules always apply:
- **"Local Cuisine"** is always recommended
- **"Theme Park"** is added when `withChildren=true`

## Project Structure
```
src/main/java/.../dmn/
  TravelApplication.java        ← Spring Boot entry point
  DmnConfig.java                ← DMN engine + decision parsing (singleton)
  TravelController.java         ← REST controller (GET /travel)
  TravelRecommendation.java     ← Response record (destination, activities, evaluationTime)
src/main/resources/
  travelRecommendations.dmn     ← DMN decision table
pom.xml
```

## Tech Stack

* **Spring Boot 4.1.0**
* **OrqueIO DMN Engine 2.1.0** (standalone, no database)
* **Java 25**
