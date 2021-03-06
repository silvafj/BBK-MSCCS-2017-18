package saddlePoints;

import java.util.Random;

/**
 * Creates a number of random arrays, and checks each array to see if it contains a saddle point.
 * Prints the arrays and the results.
 *
 * @author Fernando Silva
 */
public class SaddlePoints {

    private Random random = new Random();

    /**
     * Creates arrays various sizes (including some 2x2 arrays and some larger), fills them with random values, and
     * prints each array and information about it. Keeps generating arrays until it has printed at least one with and
     * one without a saddle point.
     */
    void run() {
        int generatedCount = 0;
        boolean withSaddlePoint = false, withoutSaddlePoint = false;
        int rows = 2, cols = 2; // First array should be 2x2

        while (!(withSaddlePoint && withoutSaddlePoint)) {
            generatedCount++;

            int min = random.nextInt(49);
            int max = random.nextInt(49) + min;

            int[][] array = createRandomArray(rows, cols, min, max);
            if (hasSaddlePoint(array)) {
                withSaddlePoint = true;
            } else {
                withoutSaddlePoint = true;
            }

            printArray(array);
            printArrayInfo(array);
            System.out.println();

            rows = random.nextInt(10) + 2;
            cols = random.nextInt(10) + 2;
        }

        System.out.println("Number of arrays generated: " + generatedCount);
    }

    /**
     * Prints the array.
     *
     * @param array the array to be printed.
     */
    void printArray(int[][] array) {
        for (int[] row : array) {
            for (int value : row) {
                System.out.printf("%3d ", value);
            }
            System.out.println();
        }
    }

    /**
     * Prints whether the given array has a saddle point, and if so, where it is (row and column) and what its value
     * is. (If there are multiple saddle points, just prints the first.)
     *
     * @param array the array to be checked.
     */
    void printArrayInfo(int[][] array) {
        if (!hasSaddlePoint(array)) {
            System.out.println("No saddle point found.");
        } else {
            int col = saddlePointColumn(array);
            int row = saddlePointRow(array);
            int point = array[row][col];
            System.out.println("Saddle point found with value " + point + " at location (" + row + ", " + col + ").");
        }
    }

    /**
     * Creates and returns an array of the given size and fills it with random values in the specified range.
     *
     * @param numberOfRows the number of rows desired.
     * @param numberOfColumns the number of columns desired.
     * @param minValue the smallest number allowable in the array.
     * @param maxValue the largest number allowable in the array.
     * @return and array with random numbers.
     */
    int[][] createRandomArray(int numberOfRows, int numberOfColumns, int minValue, int maxValue) {
        int[][] array = new int[numberOfRows][numberOfColumns];
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                array[row][col] = random.nextInt(maxValue - minValue + 1) + minValue;
            }
        }

        return array;
    }

    /**
     * Finds the largest value in an array of integers.
     *
     * @param array the array to be searched.
     * @return the largest value in the array.
     */
    int largest(int[] array) {
        int largest = Integer.MIN_VALUE;
        for (int element : array) {
            if (element > largest) {
                largest = element;
            }
        }
        return largest;
    }

    /**
     * Finds the smallest value in an array of integers.
     *
     * @param array the array to be searched.
     * @return the smallest value in the array.
     */
    int smallest(int[] array) {
        int smallest = Integer.MAX_VALUE;
        for (int element : array) {
            if (element < smallest) {
                smallest = element;
            }
        }
        return smallest;
    }

    /**
     * Returns an array containing the largest values in each column of the given array.
     *
     * @param array the array to be searched.
     * @return an array of the largest values in each column.
     */
    int[] largestValues(int[][] array) {
        int[] largest = new int[array.length > 0 ? array[0].length : 0];

        for (int col = 0; col < largest.length; col++) {
            int[] column = new int[array.length];
            for (int row = 0; row < array.length; row++) {
                column[row] = array[row][col];
            }
            largest[col] = largest(column);
        }

        return largest;
    }

    /**
     * Returns an array containing the smallest values in each row of the given array.
     *
     * @param array the array to be searched.
     * @return an array of the smallest values in each row.
     */
    int[] smallestValues(int[][] array) {
        int[] smallest = new int[array.length];

        for (int i = 0; i < smallest.length; i++) {
            smallest[i] = smallest(array[i]);
        }

        return smallest;
    }

    /**
     * Returns true if the given array has a saddle point, and false if it does not.
     *
     * @param array the array to be checked.
     * @return True if the array has a saddle point, else false.
     */
    boolean hasSaddlePoint(int[][] array) {
        return largest(smallestValues(array)) == smallest(largestValues(array));
    }

    /**
     * Given an array that is known to have a saddle point, returns the number of a row containing a saddle point.
     * If more than one row contains a saddle point, the first such row will be returned.
     *
     * @param array an array containing one or more saddle points.
     * @return the lowest-numbered row containing a saddle point.
     */
    int saddlePointRow(int[][] array) {
        if (hasSaddlePoint(array)) {
            int[] smallest = smallestValues(array);
            for (int i = 0; i < smallest.length; ++i) {
                if (smallest[i] == largest(smallest)) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Given an array that is known to have a saddle point, returns the number of a column containing a saddle point.
     * If more than one column contains a saddle point, the first such column will be returned.
     *
     * @param array an array containing one or more saddle points.
     * @return the lowest-numbered column containing a saddle point.
     */
    int saddlePointColumn(int[][] array) {
        if (hasSaddlePoint(array)) {
            int[] largest = largestValues(array);
            for (int i = 0; i < largest.length; ++i) {
                if (largest[i] == smallest(largest)) {
                    return i;
                }
            }
        }

        return -1;
    }
}