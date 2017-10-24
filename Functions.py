# Functions.py

def right_justify(s):
    """
    Right justify a string with a fixed length of 70 characters and print it.

    :param str s: The string to be justified
    :return: None

    :Example:
    >>> right_justify('monty')
                                                                     monty
    """
    print((70 - len(s)) * " " + s)

def do_twice(f, v):
    """
    Takes a function and values as arguments and calls it twice.

    :param func f: Function to be called twice
    :param object v: Value to be passed as argument to f
    :return: None

    :Example:
    >>> do_twice(print, "spam")
    spam
    spam
    """
    f(v)
    f(v)

def print_twice(s):
	print(s)
	print(s)

def do_four(f, v):
    """
    Takes a function and values as arguments and calls it four times.

    :param func f: Function to be called
    :param object v: Value to be passed as argument to f
    :return: None

    :Example:
    >>> do_four(print, "more spam")
    more spam
    more spam
    more spam
    more spam
    """
    for _ in range(4):
        f(v)

right_justify("monty")
do_twice(print_twice, "spam")
do_four(print, "more spam")
