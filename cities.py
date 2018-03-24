"""
Author: Fernando Silva <fdealm02>

Implementation of the Travelling Salesman problem: given a list of cities and
the distances between each pair of cities, calculates the shortest possible
route that visits each city exactly once and returns to the origin city.
"""
import copy
import math
import operator
import random

SWAPS_LIMIT = 10000


def distance(lat1degrees, long1degrees, lat2degrees, long2degrees):
    """
    Calculates the distance (in miles) between 2 geographical locations.

    :param float lat1degrees: Location 1 latitude (in degrees)
    :param float long1degrees: Location 1 longitude (in degrees)
    :param float lat2degrees: Location 2 latitude (in degrees)
    :param float long2degrees: Location 2 longitude (in degrees)
    :return: Distance (in miles)
    :rtype: float
    """
    earth_radius = 3956  # miles
    lat1 = math.radians(lat1degrees)
    long1 = math.radians(long1degrees)
    lat2 = math.radians(lat2degrees)
    long2 = math.radians(long2degrees)
    lat_difference = lat2 - lat1
    long_difference = long2 - long1
    sin_half_lat = math.sin(lat_difference / 2)
    sin_half_long = math.sin(long_difference / 2)
    a = sin_half_lat**2 + math.cos(lat1) * math.cos(lat2) * sin_half_long**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1.0 - a))
    return earth_radius * c


def read_cities(file_name):
    """
    Parse the data from the given `file_name` and return as a list.

    :param str file_name: File containing the cities data in tabular format
    :return: List of four-tuples `[(state, city, latitude, longitude), ...]`
    :rtype: list<tuple(str, str, float, float)>
    """
    cities = []
    with open(file_name) as source:
        for line in source:
            state, city, latitude, longitude = line.strip().split("\t")
            cities.append((state, city, float(latitude), float(longitude)))

    return cities


def print_cities(road_map):
    """
    Prints a list of cities, along with their locations.

    :param list road_map: List of four-tuples containing cities data
    """
    # Sort the roadmap, alphabetically by city name
    road_map = sorted(road_map, key=operator.itemgetter(1))

    # Print each city in the following format:
    # Annapolis ...... Lat.  38.97  Long.  -76.50
    for item in road_map:
        _, city, lat, lon = item
        print("{:.<16} Lat.{:7.2f}  Long.{:8.2f}".format(city + " ", lat, lon))


def compute_total_distance(road_map):
    """
    Calculates the sum of the distances of all the connections in
    the `road_map`. Note that `road_map` is a cycle and the last connection
    is with the starting point.

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

        total_distance += distance(lat1degrees, long1degrees, lat2degrees,
                                   long2degrees)

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
    Calculates the best cycle through all the cities in `road_map`.
    This is a best effort algorithm within 10000 attempts.

    :param list road_map: List of four-tuples containing cities data
    :return: List of four-tuples containing cities data
    :rtype: list<tuple>
    """

    if len(road_map) < 2:
        return road_map

    # Optimization to provide a better heuristic on the path order
    best_road_map = copy.copy(road_map)
    best_road_map.sort(key=lambda conn: distance(0, 0, conn[2], conn[3]))
    best_total_distance = compute_total_distance(road_map)

    # Use a while loop, to have absolutely sure that we don't go over the
    # limit of swaps we are entitled to. Yes, I could break from a "for" loop
    # but the lecturer doesn't like it...
    swaps = a = b = 0
    while a < len(road_map) and swaps < SWAPS_LIMIT:

        b = a + 1
        while b < len(road_map) and swaps < SWAPS_LIMIT:
            swaps += 1
            new_road_map, new_total_distance = \
                swap_cities(best_road_map, a, b)

            # This is an implementation of the 2-opt algorithm as described in
            # https://en.wikipedia.org/wiki/2-opt
            #
            # It allows us to find the optimal route of 16500 miles. It is for
            # reference only, because it goes against the original assignment
            # specifications.
            # new_road_map = (best_road_map[:a] + best_road_map[a:b][::-1] +
            #                 best_road_map[b:])
            # new_total_distance = compute_total_distance(new_road_map)

            if new_total_distance < best_total_distance:
                best_road_map = new_road_map
                best_total_distance = new_total_distance

            b = b + 1

        a = a + 1

    return best_road_map


def print_map(road_map):
    """
    Prints the cities, their connections and the cost for each connection. It
    also prints the total cost.

    :param list road_map: List of four-tuples containing cities data
    """
    for i in range(len(road_map)):
        connection = road_map[i:i + 2]
        if i == len(road_map) - 1:
            connection.append(road_map[0])

        _, city1, _, _ = connection[0]
        _, city2, _, _ = connection[1]

        print("{}: {:8.2f}".format(
            city1.ljust(32 - len(city2), ".") + " " + city2,
            compute_total_distance(connection)))

    print("\nTOTAL DISTANCE: {:.2f} miles".format(
        compute_total_distance(road_map)))


def export_to_google_maps(road_map):
    """Plot the roadmap in Google Maps."""

    import gmplot

    start = road_map[0]
    gmap = gmplot.GoogleMapPlotter(start[2], start[3], 5)
    gmap.plot(
        lats=[conn[2] for conn in road_map] + [start[2]],
        lngs=[conn[3] for conn in road_map] + [start[3]],
        color="red",
        edge_width=3,
    )

    gmap.draw("cities_road_map.html")


def main():
    """
    Reads in (and prints out) the city data; then calculates and prints the
    "best" cycle.
    """
    road_map = read_cities("city-data.txt")

    print("\n{:=^50}\n".format(" CITY DATA "))
    print_cities(road_map)

    new_road_map = find_best_cycle(road_map)

    print("\n{:=^50}\n".format(" BEST ROUTE "))
    print_map(new_road_map)

    # Used to export to Google Maps and have a nice visualisation of the roadmap
    export_to_google_maps(new_road_map)


if __name__ == "__main__":
    main()
