# Backend

Build and run:
```
./gradlew clean build
java -jar build/libs/backend.jar
```

## Service API.

### 1. User management
 
1. Sign up
2. Login

### 2. Game

Game statuses:
- Searching for opponent
- Canceled
- In progress
- Finished

1. POST /api/1.0/games - starts new game session
```
{
    "player_id" : 1
}
```
2. GET /api/1.0/games/{session_id} - get session info

3. DELETE /api/1.0/games/{session_id} - cancels specified session

4. POST /api/1.0/games/{session_id} - adds user movement
```
{   
    "player_id" : 1,
    "x_coord" : 0, 
    "y_coord" : 0 
}
```

5. GET /api/1.0/games/{session_id}/movements?after=1 - get movement history after movement id *after*

### 3. HighScore

1. GET /api/1.0/highscore - information about top users 