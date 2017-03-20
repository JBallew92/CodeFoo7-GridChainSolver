# CodeFoo7-GridChainSolver
Find all cells whose sum equals the grids area. Chains are unique.

This program generates an NxN grid whose size is determined by user input. Starting from the first element of the grid, each 8 possible surrounding grid cells are checked to see if the current cell + surrounding cell > area. If not, the search is continued in a recursive fashion.

Once a chain is found whose sum is equal to the area, the cells used to form the chain are compared to all cell combinations previously used to form chains. If another chain combo used the same exact cells then the combo is not saved. Else, the combo is stored and the cell combination is stored for later comparisons.

The checkNeighbor method is called atleast 8 more times even after a chain is found whose sum is equal to the area, because there is the possibility that the 8 surrounding grid cells' values could be 0. If 0 values exists, then there is the possibility that unique chains still exists.

If the sum exceeds the area then the search does not continue to branch off to surrounding cells.

BigO Complexity is ????
