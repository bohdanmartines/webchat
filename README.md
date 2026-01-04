# Web Chat Application

A real-time chat application built as a demonstration of full-stack development skills, featuring WebSocket-based messaging, JWT authentication, and modern web technologies.

## Tech Stack

### Backend
- **Scala** with Play Framework
- **WebSockets** for real-time messaging
- **Slick** for database access
- **MySQL** for data persistence
- **JWT** for authentication (access tokens with 1-hour lifetime)

### Frontend
- **React** for UI components
- **WebSocket API** for real-time communication

### Infrastructure
- **Docker** for containerization
- **Docker Compose** for orchestration

## Features

- User registration and authentication
- JWT-based secure API access
- Real-time messaging via WebSockets
- Multiple chat rooms/conversations
- Persistent message history

## Project Structure

```
web-chat/
├── backend/          # Scala Play Framework application
├── frontend/         # React application
├── docker-compose.yml
└── README.md
```

## Running Locally
The recommended way is to run the application with Docker, which is the fastest and easiest way to do it.
Alternatively, you can run it from the command line or IDE of your choice.

### Option 1: Docker (Recommended for Quick Start)
Ensure you have Docker installed on your machine.

Start all services with a single command:
```bash
docker-compose up -d
```

Access the application:
- Frontend: http://localhost:5173
- Backend API: http://localhost:9000

Stop the application:
```bash
docker-compose down
```

### Option 2: Manual Setup

Ensure you have next installed on your machine:
- JDK 17+ and SBT
- Node.js and npm
- Docker

#### Start Backend Only
1. Start MySQL (via Docker):
```bash
docker-compose up -d mysql
```

2. Run the backend:
```bash
cd backend
sbt run
```

Backend API will be available at http://localhost:9000

3. Run the frontend:
In a separate terminal:
```bash
cd frontend
npm install
npm run dev
```

Frontend will be available at http://localhost:5173