# Solution Project for the Maximum Diversity Problem (MDP)
This project implements route-based metaheuristic algorithms, specifically local search and tabu search, to solve the Maximum Diversity Problem (MDP). The objective of the MDP is to select a subset of elements from a larger set in such a way that the diversity among the selected elements is maximized.

## Implemented Algorithms

### Local Search
Local search is a heuristic technique that explores the solution space by moving from one solution to a neighboring one. The general steps of the local search algorithm applied to the MDP are:

- Initialization: Start with an initial solution generated randomly or by some heuristic.
- Neighborhood Exploration: Evaluate the solutions in the neighborhood of the current solution, where the neighborhood is defined by small modifications of the current solution (e.g., swapping elements in and out of the selected subset).
- Selection: Choose the best solution in the neighborhood that maximizes diversity.
- Update: Replace the current solution with the selected solution.
- Stopping Criterion: Repeat the steps of exploration, selection, and updating until a stopping criterion is met, such as a maximum number of iterations or an insignificant improvement in the solution.


### Tabu Search
Tabu search is an extension of local search that uses a memory called the "tabu list" to avoid revisiting previously explored solutions and thus escape local optima. The general steps of the tabu search algorithm applied to the MDP are:

- Initialization: Start with an initial solution and an empty tabu list.
- Neighborhood Exploration: Evaluate the solutions in the neighborhood of the current solution.
- Selection with Tabu List: Choose the best solution in the neighborhood that is not in the tabu list, or allow tabu moves if they significantly improve the current solution (aspiration criterion).
- Update and Tabu List Management: Update the current solution and add the performed move to the tabu list. Manage the tabu list to keep its size under control.
- Stopping Criterion: Repeat the steps of exploration, selection, and updating until a stopping criterion is met.

