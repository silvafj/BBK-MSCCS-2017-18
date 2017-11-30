"""
Author: Fernando Silva <fdealm02>

Implementation of the Travelling Salesman problem: given a list of cities and
the distances between each pair of cities, calculates the shortest possible
route that visits each city exactly once and returns to the origin city.
"""
import copy

from math import *

def read_cities(file_name):
    """
    Parse the data from the given `file_name` and return as a list.

    :param str file_name: File containing the cities data
    :return: List of four-tuples `[(state, city, latitude, longitude), ...]`
    :rtype: list<tuple>
    """
    cities = []
    with open(file_name) as f:
        for line in f:
            state, city, latitude, longitude = line.strip().split("\t")
            cities.append((state, city, float(latitude), float(longitude)))

    return cities


def print_cities(road_map):
    """
    Prints a list of cities, along with their locations.

    :param list road_map: List of four-tuples containing cities data
    """
    for item in road_map:
        _, city, lat, lon = item
        print("{} {:.2f} {:.2f}".format(city, lat, lon))


def distance(lat1degrees, long1degrees, lat2degrees, long2degrees):
    earth_radius = 3956  # miles
    lat1 = radians(lat1degrees)
    long1 = radians(long1degrees)
    lat2 = radians(lat2degrees)
    long2 = radians(long2degrees)
    lat_difference = lat2 - lat1
    long_difference = long2 - long1
    sin_half_lat = sin(lat_difference / 2)
    sin_half_long = sin(long_difference / 2)
    a = sin_half_lat ** 2 + cos(lat1) * cos(lat2) * sin_half_long ** 2
    c = 2 * atan2(sqrt(a), sqrt(1.0 - a))
    return earth_radius * c


def compute_total_distance(road_map):
    """
    Calculates the sum of the distances of all the connections in
    the `road_map`. Note that `road_map` is a cycle and the last connection
    returns to the starting point.

    :param list road_map: List of four-tuples containing cities data
    :return: Sum of the distances of all connections in miles
    :rtype: float
    """
    total_distance = 0.0

    if len(road_map) < 2:
        return total_distance

    for i, item in enumerate(road_map):
        _, _, lat1degrees, long1degrees = item
        _, _, lat2degrees, long2degrees = road_map[(i + 1) % len(road_map)]

        total_distance += distance(lat1degrees, long1degrees,
                                   lat2degrees, long2degrees)

    return total_distance


def swap_adjacent_cities(road_map, index):
    """
    Swaps two adjacent cities based on the given `index`. City at the location
    `index` will be swapped with the city at the location `index + 1` (or at
    `0`, if `index` refers to the last element in the list).

    :param list road_map: List of four-tuples containing cities data
    :param int index: City to be swaped
    :return: Tuple with new `road_map` and `total_distance`
    :rtype: tuple
    """
    index2 = index if len(road_map) < 2 else (index + 1) % len(road_map)
    return swap_cities(road_map, index, index2)


def swap_cities(road_map, index1, index2):
    """
    Swaps two cities based on the given `index1` and `index2`.

    :param list road_map: List of four-tuples containing cities data
    :param int index1: City to be swaped to `index2`
    :param int index2: City to be swaped to `index1`
    :return: Tuple with new `road_map` and `total_distance`
    :rtype: tuple

    Allow for the possibility that `index1=index2`,
    and handle this case correctly.
    """
    new_road_map = copy.copy(road_map)

    if len(new_road_map) < 2:
        return (new_road_map, 0.0)

    if index1 != index2:
        new_road_map[index1], new_road_map[index2] = \
            new_road_map[index2], new_road_map[index1]

    return (new_road_map, compute_total_distance(new_road_map))


def find_best_cycle(road_map):
    """
    Using a combination of `swap_cities` and `swap_adjacent_cities`,
    try `10000` swaps, and each time keep the best cycle found so far.
    After `10000` swaps, return the best cycle found so far.
    """

    # When we travel from city 1 to city 2, it must be the shortest distance
    # So, I should find for each city what is the closest
    best_road_map = road_map
    best_total_distance = compute_total_distance(road_map)

    swaps = 0
    swap_from = 1
    while swaps <= 10000:
        for i in range(swap_from + 1, len(road_map)):
            swaps += 1
            new_road_map, new_total_distance = \
                swap_cities(best_road_map, swap_from, i)

            if new_total_distance < best_total_distance:
                best_road_map = new_road_map
                best_total_distance = new_total_distance

        swap_from += 1

        if swap_from > len(road_map):
            swap_from = 1

    return best_road_map


def print_map(road_map):
    """
    Prints, in an easily understandable format, the cities and
    their connections, along with the cost for each connection
    and the total cost.
    """
    for i in range(len(road_map)):
        connection = road_map[i:i+2]
        if i == len(road_map) - 1:
            connection.append(road_map[0])

        _, city1, _, _ = connection[0]
        _, city2, _, _ = connection[1]
        distance = compute_total_distance(connection)

        print("{} => {}: {:.2f}".format(city1, city2, distance))

    distance = compute_total_distance(road_map)
    print("\nTOTAL DISTANCE: {:.2f}".format(distance))


def main():
    """
    Reads in, and prints out, the city data, then creates the "best"
    cycle and prints it out.
    """
    road_map = read_cities("city-data.txt")

    print("\n{:=^80}\n".format(" CITY DATA "))
    print_cities(road_map)

    new_road_map = find_best_cycle(road_map)

    print("\n{:=^80}\n".format(" BEST ROUTE "))
    print_map(new_road_map)


if __name__ == "__main__":
    main()
