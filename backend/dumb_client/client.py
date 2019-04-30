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

    my_turn = False

    r = requests.get(host + "/api/1.0/games/" + str(game_id))

    while True:
        r = requests.get(host + "/api/1.0/users/" + str(my_user_id) + "/current-game")
        if r.status_code != 200:
            raise Exception("Failed to wait for game to start")

        response = r.json()
        if response['game_id'] is not None:
            game_id = response['game_id']
            my_turn = response['current_turn_player_id'] == my_user_id
            if my_user_id == response['first_player_id']:
                my_symbol = response['first_player_symbol']
                opp_id = response['second_player_id']
                opp_symbol = response['second_player_symbol']
            else:
                my_symbol = response['second_player_symbol']
                opp_id = response['first_player_id']
                opp_symbol = response['first_player_symbol']
            break
        time.sleep(1)
        print("Searching for opponent")
    print("Game with id " + str(game_id) + " is started")

    current_turn_user_id = -1

    if r.status_code != 200:
        raise Exception("Failed to get the game with id " + str(game_id) + " " + r.text)

    last_move_id = -1
    moves: list = []
    # game loop
    while True:
        r = requests.get(host + "/api/1.0/games/" + str(game_id) + "/moves?after=" + str(last_move_id))
        if r.status_code != 200:
            raise Exception("Failed to get history " + r.text)

        for new_move in r.json():
            if new_move['player_id'] == my_user_id:
                move_symbol = my_symbol
            else:
                move_symbol = opp_symbol
            moves.append((new_move['x'], new_move['y'], move_symbol))
            last_move_id = new_move['move_id']
            my_turn = new_move['player_id'] != my_user_id

        if not my_turn:
            print("Waiting for an opponent move")
            time.sleep(1)
            continue

        draw_board(moves)
        x = int(input("Enter x - "))
        y = int(input("Enter y - "))

        r = requests.post(host + "/api/1.0/games/" + str(game_id) + "/moves",
                          json={'player_id': my_user_id, 'x': x, 'y': y})
        if r.status_code != 200:
            print("Failed!" + r.text)

        validation_response = r.json()
        if validation_response['winner_id'] is not None:
            winner_id = validation_response['winner_id']
            if winner_id == my_user_id:
                print("Winner winner chicken dinner")
            else:
                print("You lose!")
            break


def draw_board(moves):
    min_x = -3
    min_y = -3
    max_x = 3
    max_y = 3

    for x, y, _ in moves:
        if min_x > x:
            min_x = x
        if max_x < x:
            max_x = x

        if min_y > y:
            min_y = y
        if max_y < y:
            max_y = y

    for y in range(min_y, max_y + 1):
        for x in range(min_x, max_x + 1):
            symb = ' '
            for xx, yy, symbol in moves:
                if x == xx and y == yy:
                    symb = symbol
                    break
            print(symb + ' ', end='')
        print()


# Main body
if __name__ == '__main__':
    main()
