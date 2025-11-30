# Changelog

## [v0.0.1] – Registration API
- Implemented storing users in the database
- Add password encoding

## [v0.0.2] – Login API
- Added user credentials verification on login
- Introduced JWT generation on login

## [v0.1.0] – Endpoint protection
- Implemented access token verification for protected endpoints
- Add a protected endpoint for retrieving user information

## [v0.1.1] – Small improvement to Login API
- Removed name field from the user entity

## [v0.2.0] – Registration and Login UI
- Added frontend subproject
- Implemented Registration and Login pages
- Integrated Registration and Login pages with the API

## [v0.2.1] – Introduce API for chat creation and retrieval
- Added endpoint to create chat
- Added endpoint to retrieve chats for current user

## [v0.2.2] – Display user chats
- Display user chats on the Home page
- Allow adding new chats
- Allow filtering chats by name

## [v0.3.0] – Add individual chat page
- Add chat page
- Display chat name
- Display empty message area
- Display disabled input and button for message sending

## [v0.4.0] – Add support for chat messaging on the backend
- Introduce a WebSocket endpoint to open a chat connection
- Track currently opened chats
- Track when a user is connected and disconnected to the chat
- Broadcast messages to all currently connected participants
- Add support for a user connected to the chat from multiple tabs
