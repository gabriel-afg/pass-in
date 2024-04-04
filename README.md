
# Pass In API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
[![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)](./LICENSE)

This project is an API built using **Java, Spring Boot, and PostgreSQL as the database.**

The API was developed to manage events and attendees. It allows organizers to create events, view event details, and view the list of attendees. Attendees can register for an event and check-in at the event.

Unit tests were developed using JUnit and Mockito.

## Table of Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Database](#database)
- [Contributing](#contributing)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/gabriel-afg/event-management-api.git
```

2. Install dependencies with Maven
3. Initialize the container in docker
```bash
docker compose up -d
```

## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080

## API Endpoints
The API provides the following endpoints:

**GET EVENTS**
```markdown
GET /events - Retrieve a list of all events.
```

**POST EVENTS**
```markdown
POST /events - Create a new event.
```

**GET EVENT DETAILS**
```markdown
GET /events/{eventId} - Retrieve details of a specific event.
```

**POST ATTENDEE**
```markdown
POST /events/{eventId}/attendees - Register a new attendee to a specific event.
```

## Database
The project uses two databases, the default using PostgreSQL containerized in Docker. And [H2 Database](https://www.h2database.com/html/tutorial.html) for unit tests.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.
