/* Student information for assignment:
 *  Name: Mohammad N Kashif
 *  UT EID: mnk665
 *  email address: mohammadnkashif@utexas.edu
 *  Professor name: Axel Turnquist
 *  Class: Matrices and Matrix Calculations
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Matrix {
    // Instance Variable
    private static double[][] matrix;

    // Constants
    private static final int DIGITS_AFTER_DECIMAL = 6;

    /**
     * Main method, runs matrix calculator in a loop
     * as long as user wants to calculate more.
     * Also includes some tests for the
     * Gaussian Elimination being performed.
     */
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        intro(kb);
        char userChoice;
        userChoice = calculationPrompt(kb);
        while (userChoice == 'Y') {
            getUserMatrix(kb);
            if (gaussianElimination()) {
                System.out.println("\nRow-Reduced Matrix: ");
                System.out.println(matrixToString());
            }
            else {
                System.out.println("Matrix could not be " +
                        "reduced to echelon form.");
                System.out.println(matrixToString());

            }
            userChoice = calculationPrompt(kb);
        }
        System.out.println("\nThank you for using the matrix calculator!");
    }

    /**
     * Prints intro message and if desired, runs tests on the program.
     * @param kb Input/ Keyboard scanner object.
     */
    private static void intro(Scanner kb) {
        System.out.println("Welcome to the Gaussian Elimination" +
                " Matrix Calculator.\n");
        System.out.println("M 340L Midterm Project");
        System.out.println("Author: Mohammad Kashif");
        System.out.println("Professor: Axel Turnquist\n");
        System.out.print("If you would like to run " +
                "some tests on this matrix calculator, " +
                "\nplease enter 'y' or 'Y'. Otherwise, enter 'n' or 'N': ");
        String input = kb.next();
        System.out.println();
        char userChoice = getUserChoice(kb, input);
        if (userChoice == 'Y') {
            runTests();
        }
    }

    /**
     * Prompts user to ask if they would like to calculate a row-reduced matrix.
     * @param kb Input/ keyboard scanner object.
     * @return
     */
    private static char calculationPrompt(Scanner kb) {
        System.out.print("If you would like to calculate " +
                "a Gaussian Elimination Matrix" +
                "\nplease enter 'y' or 'Y'. Otherwise, enter 'n' or 'N': ");
        String input = kb.next();
        System.out.println();
        char userChoice = getUserChoice(kb, input);
        return userChoice;
    }

    /**
     * Confirms valid user input and returns their choice.
     * @param kb Input/ keyboard scanner object
     * @param input User's initial choice.
     * @return User's valid choice.
     */
    private static char getUserChoice(Scanner kb, String input) {
        char userChoice = input.toUpperCase().charAt(0);
        while (!validInput(input, userChoice)) {
            System.out.print("Please enter a valid choice: ");
            input = kb.next();
            System.out.println();
            userChoice = input.toUpperCase().charAt(0);
        }
        return userChoice;
    }

    /**
     * Actual boolean check for input validity.
     * @param input User choice.
     * @return true/false, is input valid?
     */
    private static boolean validInput(String input, char userChoice) {
        return input.length() == 1 &&
                (userChoice == 'Y' || userChoice == 'N');
    }

    /**
     * Receives matrix from user.
     * @param kb Input/ keboard scanner object.
     */
    private static void getUserMatrix(Scanner kb) {
        System.out.print("Enter number of rows in your matrix: ");
        int rows = kb.nextInt(); // add test for invalid input
        System.out.print("Enter number of columns in your matrix: ");
        int columns = kb.nextInt();
        kb.nextLine();
        System.out.println();
        matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.out.print("Enter row " + (i + 1) +
                    " of data separated by spaces: ");
            String curr = kb.nextLine();
            Scanner line = new Scanner(curr);
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = line.nextDouble();
            }
            line.close();
        }
    }

    /**
     * Main matrix row reduction algorithm
     * @return Whether row reduction was successful
     */
    public static boolean gaussianElimination() {
        // Row and column index tracking variables.
        int numRow = 0;
        int numCol = 0;
        // While loop runs for a square matrix within current matrix.
        // Calculates min of rows and columns to find size of square.
        while (numCol < Math.min(matrix.length, matrix[0].length)) {
            // While loop runs until nonzero column is reached
            // and partial pivot is successful.
            while (!partialPivot(numRow, numCol)) {
                if (numCol < matrix[0].length) {
                    // Skip to next column
                    numCol++;
                }
                else {
                    // If at last column, row reduction is complete.
                    return true;
                }
            }
            for (int i = numRow + 1; i < matrix.length; i++) {
                // Calculates scale factor for row subtraction to achieve a
                // zero in position i, numCol, then replaces with zero.
                double scaleFactor =
                        matrix[numRow][numCol] / matrix[i][numCol];
                matrix[i][numCol] = 0;
                // Scale factor used to modify remaining values in row.
                for (int j = numCol + 1; j < matrix[0].length; j++) {
                    matrix[i][j] -= (matrix[numRow][j] / scaleFactor);
                }
            }
            numRow++;
            numCol++;
        }
        return true;
    }

    /**
     * Method to perform partial pivot for each while loop
     * iteration in gaussianElimination
     * @param row Starting row index
     * @param col Current column for row reduction
     * @return boolean for whether the column is nonzero or not.
     */
    public static boolean partialPivot(int row, int col) {
        ArrayList<Double> rowVals = new ArrayList<>();
        boolean nonZero = false;
        for (int i = row; i < matrix.length; i++) {
            rowVals.add(matrix[i][col]);
            if (matrix[i][col] != 0) {
                nonZero = true;
            }
        }
        int maxRow = indexOfMax(rowVals) + row;
        swapRows(row, maxRow);
        return nonZero;
    }

    /**
     * Returns the index of the max value in a list.
     * Receives a list of column values that need comparison
     * for performing partial pivot.
     * @param list
     * @return
     */
    private static int indexOfMax(ArrayList<Double> list) {
        double max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (Math.abs(list.get(i)) > Math.abs(max)) {
                max = list.get(i);
            }
        }
        return list.indexOf(max);
    }

    /**
     * Swaps 2 rows in a matrix, helper method for partial pivot.
     * @param row1 First row in swap
     * @param row2 Second row in swap
     */
    private static void swapRows(int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    /**
     * @return a String with all elements of this Matrix.
     * Each row is on a separate line.
     * Spacing based on longest element in Matrix.
     */
    public static String matrixToString() {
        StringBuilder sb = new StringBuilder();
        int spacing = findGreatestLength() + 1;
        for (double[] nums : matrix) {
            sb.append("|");
            for (int j = 0; j < matrix[0].length; j++) {
                sb.append(distancedStr(nums[j], spacing));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    /**
     * Finds the length of the longest element in our matrix.
     * Aids in spacing for matrixToString.
      * @return Length of longest element.
     */
    private static int findGreatestLength() {
        int greatestLength = Integer.MIN_VALUE;
        for (double[] nums : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                String numString = String.format("%."
                        + DIGITS_AFTER_DECIMAL + "f", nums[j]);
                if (numString.length() > greatestLength) {
                    greatestLength = numString.length();
                }
            }
        }
        return greatestLength;
    }

    /**
     * Returns a string that includes the double
     * passed into the method as well as the
     * appropriate amount of spacing afterwards.
     * @param num Number needing to be appended
     * @param spacing Required amount of spacing.
     * @return String from combining double value and spacing.
     */
    private static String distancedStr(double num, int spacing) {
        String numString = String.format("%." +
                DIGITS_AFTER_DECIMAL + "f", num);
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < (spacing - numString.length()); i++) {
            sb2.append(" ");
        }
        sb2.append(numString);
        return sb2.toString();
    }

    /**
     * Runs tests on program as required by assignment instructions.
     */
    private static void runTests() {

        // Test 1 (Computational):
        System.out.println("\nTest 1: Computational Test\n");
        createRandomMatrix(100, 10);
        gaussianElimination();
        System.out.println("Row-Reduced Matrix: ");
        System.out.println(matrixToString());

        // Test 2 (Theoretical)
        System.out.println("\nTest 2: Time Complexity Calculation:\n");
        System.out.println("Floating Point Operations (FLOPs):\n");
        System.out.println("Overall Time Complexity: O(N)^3");
        System.out.println("Partial Pivot: O(N)");
        System.out.println("Row Reduction: O(N)^2");
        System.out.println("Time Complexity Equation: "); // add here

        // Test 3 (Theoretical)
        System.out.println("\nTest 3: Time Tests To " +
                "Prove Time Complexity:\n");
        runTimeTest(1, 5, 5);
        runTimeTest(2, 50, 50);
        runTimeTest(3, 500, 500);
    }

    /**
     * Runs timing tests on a random matrix of given size.
     * @param testNum Number of test we are on.
     * @param numRows Number of rows for random matrix.
     * @param numCols Number of columns for random matrix.
     */
    private static void runTimeTest(int testNum, int numRows, int numCols) {
        System.out.println("Matrix " + testNum + ": "
                + numRows + " x " + numCols);
        createRandomMatrix(numRows, numCols);
        Stopwatch s = new Stopwatch();
        s.start();
        gaussianElimination();
        s.stop();
        System.out.println("Timing: " +
                String.format("%.10f", s.time()) + " seconds\n");
    }

    /**
     * Creates a matrix with random numbers between 0 and 1.
     * @param numRows Number of rows in matrix.
     * @param numCols Number of columns in matrix.
     */
    private static void createRandomMatrix (int numRows, int numCols) {
        matrix = new double[numRows][numCols];
        Random r = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = r.nextDouble();
            }
        }
    }

    /**
     A class to measure time elapsed.
     Adopted from Mike Scott's CS 314 Course Material.
     */

    public static class Stopwatch
    {
        private long startTime;
        private long stopTime;

        public static final double NANOS_PER_SEC = 1000000000.0;

        /**
         start the stop watch.
         */
        public void start(){
            startTime = System.nanoTime();
        }

        /**
         stop the stop watch.
         */
        public void stop()
        {	stopTime = System.nanoTime();	}

        /**
         elapsed time in seconds.
         @return the time recorded on the stopwatch in seconds
         */
        public double time()
        {	return (stopTime - startTime) / NANOS_PER_SEC;	}

        public String toString(){
            return "elapsed time: " + time() + " seconds.";
        }

        /**
         elapsed time in nanoseconds.
         @return the time recorded on the stopwatch in nanoseconds
         */
        public long timeInNanoseconds()
        {	return (stopTime - startTime);	}
    }
}

