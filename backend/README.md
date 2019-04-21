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

3. GET /api/1.0/users/{player_id}/current-game - get in progress game for user

### 2. Game

Game statuses:
- In progress
- Finished

1. POST /api/1.0/games - starts new game session
```
{
    "player_id" : 1
}
```

2. GET /api/1.0/games/{session_id} - get session info 

3. POST /api/1.0/opponents-search - start opponent search
```
{
    "player_id" : 1
}
```
4. DELETE /api/1.0/opponents-search - cancel opponent search
```
{
    "player_id" : 1
}
```

5. POST /api/1.0/games/{session_id}/moves - adds user movement
```
{   
    "player_id" : 1,
    "x" : 0, 
    "y" : 0 
}
```

6. GET /api/1.0/games/{session_id}/moves?after=1 - get movement history after movement id *after*

### 3. HighScore

1. GET /api/1.0/highscore - information about top users 
