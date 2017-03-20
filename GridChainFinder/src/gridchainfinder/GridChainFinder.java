/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gridchainfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author James Ballew
 */
public class GridChainFinder {

    static double area;
    static int input;
    static int count;
    static int[][] lookupGrid;
    static Set<String> cellCombos = new HashSet<String>(25);
    static ArrayList<String> validChains = new ArrayList<>();
    static Comparator<Integer> byCell
            = (Integer o1, Integer o2) -> o1 - o2;

    public static void main(String[] args) {
        //total complexity O(n^4)
        //get user input for an n x n matrix
        //then determine the area of the grid
        input = -1;
        System.out.println("Enter the width of the grid: ");
        Scanner scanner = new Scanner(System.in);
        while (input < 0 && scanner.hasNextInt()) {
            input = scanner.nextInt();
            if (input < 0)
                System.out.println("Please enter a positive whole number: ");
        }
        area = Math.pow(input, 2);

        int sum = 0;
        String chain = "";
        //user arraylist to keep track of which cells are used by valid chains
        ArrayList<Integer> cells = new ArrayList<>();
        //store generated nxn grid
        int[][] userGrid = new int[input][input];
        //create another nxn grid to hold cell numbers for lookup later
        lookupGrid = new int[input][input];
        //another nxn grid to hold the used status of each cell
        boolean[][] used = new boolean[input][input];
        count = 0;

        //populate lookupGrid with cell number, userGrid with random numbers, 
        //and set used status to false for all grid locations
        //display userGrid
        for (int i = 0; i < input; i++) {
            for (int j = 0; j < input; j++) {
                lookupGrid[i][j] = count;
                userGrid[i][j] = ThreadLocalRandom.current().nextInt(0, 10);
                used[i][j] = false;
                System.out.print(userGrid[i][j] + " ");
                count++;
            }
            System.out.print("\n");
        }

        //begin at grid[0][0] and begin searching for valid chains using checkNeighbor()
        //each "chain" begins at userGrid[i][j]
        for (int i = 0; i < input; i++) {
            for (int j = 0; j < input; j++) {
                chain = String.valueOf(userGrid[i][j]);
                cells.add(lookupGrid[i][j]);
                checkNeighbor(i, j, userGrid, used, sum, chain, cells);
                used[i][j] = false;
                cells.remove(cells.size() - 1);
            }//end for j
        }//end for i
        System.out.println("Valid Chains: " + validChains.size());
        validChains.forEach(System.out::println);
    }//end main

    public static void checkNeighbor(int row, int column, int[][] grid,
            boolean[][] used, int sum, String chain, ArrayList<Integer> cells) {
        //change cell status to used and increment sum by value of current cell
        used[row][column] = true;
        sum += grid[row][column];

        //check if sum = area, then split the comma seperated integers so into an array 
        if (sum == area) {
            String[] temp = chain.split(",");

            //check that chain "temp" length is at least grid.length -1
            if (temp.length >= grid.length - 1) {

                //clone cells then sort clone then append to string so that we can make a set of cell combos used
                ArrayList<Integer> clone = new ArrayList<>(cells.size());
                for (int i = 0; i < cells.size(); i++) {
                    clone.add(cells.get(i));
                }//end for i
                Collections.sort(clone, byCell);
                String tempCells = "";
                for (int i = 0; i < clone.size(); i++) {
                    tempCells += clone.get(i);
                }//end for i

                //check that the grids used to make "temp" chain have not already been used by a previous chain
                //if not then add that chain to valid chains and add the grids used to cellCombos
                if (!cellCombos.contains(tempCells)) {
                    cellCombos.add(tempCells);
                    String appendedChain = "";
                    for (int i = 0; i < temp.length; i++) {
                        if (i == temp.length - 1) {
                            appendedChain = appendedChain + temp[i] + " = " + sum;
                        } else {
                            appendedChain = appendedChain + temp[i] + " + ";
                        }//end if/else
                    }//end for i
                    //add the appendedChain to the list of validChains to be displayed later
                    validChains.add(appendedChain);
                }//end if !comboCell 
            }//end if temp > length
        }//end if sum = area 

        //if sum<=area then continue searching for valid chains until sum==area
        //the <= is necassary incase you have cells with the value 0 that could still be used to make unique combos
        if (sum <= area) {
            for (int i = row - 1; i <= row + 1 && i < input; i++) {
                for (int j = column - 1; j <= column + 1 && j < input; j++) {
                    if (i >= 0 && j >= 0 && !used[i][j]) {
                        cells.add(lookupGrid[i][j]);
                        checkNeighbor(i, j, grid, used, sum, chain + "," + String.valueOf(grid[i][j]),
                                cells);
                        cells.remove(cells.size() - 1);
                        //reset cell status so all the cells aren't "used" in the next cycle
                        used[i][j] = false;
                    }//end inner if
                }//end for j
            }//end for i
        }//end if sum < area
    }//end checkNeighbor
}//end class
