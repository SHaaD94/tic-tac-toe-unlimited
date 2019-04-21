# Imports
import sys
import time

import requests


# import os


def main():
    args = sys.argv[1:]
    if not args:
        print('service url is required example: python3 client.py http://127.0.0.1:8080')
        sys.exit(1)

    host: str = args[0]

    name = input("Enter your name - ")

    r = requests.post(host + "/api/1.0/users", json={'nick': name})

    if r.status_code != 200:
        raise Exception("Failed to login as " + name)

    my_user_id: int = int(r.json()['id'])
    print("Successfully logged in with id " + str(my_user_id))

    r = requests.post(host + "/api/1.0/opponents-search", json={'player_id': my_user_id})
    if r.status_code != 200:
        raise Exception("Failed to start opponent search for id")
    print("Successfully started search, waiting...")

    game_id = None
    opp_id = None
    my_symbol = None
    opp_symbol = None

    while True:
        r = requests.get(host + "/api/1.0/users/" + str(my_user_id) + "/current-game")
        if r.status_code != 200:
            raise Exception("Failed to wait for game to start")

        response = r.json()
        if response['game_id'] is not None:
            game_id = response['game_id']
            if my_user_id == response['first_player_id']:
                my_symbol = response['first_player_symbol']
                opp_id = response['second_player_id']
                opp_symbol = response['second_player_symbol']
            else:
                my_symbol = response['second_player_symbol']
                opp_id = response['first_player_id']
                opp_symbol = response['first_player_symbol']
            break
        time.sleep(10)
        print("Searching for opponent")
    print("Game with id " + str(game_id) + " is started")

    r = requests.get(host + "/api/1.0/games/" + str(game_id))
    if r.status_code != 200:
        raise Exception("Failed to get the game with id " + str(game_id))

    last_move = -1
    # game loop
    while True:
        r = requests.get(host + "/api/1.0/games/" + str(my_user_id) + "movements?after" + str(last_move))
        if r.status_code != 200:
            raise Exception("Failed to get history")
        moves = r.json()

        x = int(input("Enter x - "))
        y = int(input("Enter y - "))

        r = requests.post(host + "/api/1.0/games/" + str(my_user_id) + "movements" + str(last_move),
                          json={'player_id': my_user_id, 'x': x, 'y': y})
        if r.status_code != 200:
            raise Exception("Failed to post move")


# Main body
if __name__ == '__main__':
    main()
