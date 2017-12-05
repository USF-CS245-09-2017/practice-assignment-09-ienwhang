import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.*;

public class GraphAdjMatrix implements Graph {

	int[][] edges;
	int vertices;
	
	// Initialise edges matrix
	public GraphAdjMatrix(int vertices) {
		edges = new int[vertices][vertices];
		this.vertices = vertices;
	}
	
	// Add edge with weight
	public void addEdge(int v1, int v2, int weight) {
		edges[v1][v2] = weight;
	}

	// Return weight of edge and 0 if edge does not exist
	public int getEdge(int v1, int v2) {
		if (edges[v1][v2] != 0) 
			return edges[v1][v2];
		return 0;
	}
	
	// Get smallest in-degree (ideal if 0)
	public int smallestInDeg() {
		int target = 0;
		int in = 0;
		for (int v=0; v<vertices; v++) {
			for (int i=0; i<vertices; i++) {
				if (edges[i][v] != 0)
					in++;  
			}
			if (in == target) 
				return v;
		}
		target++;
		return -1;  // Fail
	}

	public int createSpanningTree() {
		boolean[] known = new boolean[vertices];  // Initialise arrays
		Arrays.fill(known, false);
		int[] cost = new int[vertices];
		Arrays.fill(cost, 0);
		int[] path = new int[vertices];
		Arrays.fill(path, -1);
		
		int start = smallestInDeg();
		int total = 0;
		path[start] = -2; // Mark path starting point as -2
		
		createSpanningTree(start, known, cost, path);
			
		for (int i=0; i<cost.length; i++)
			total += cost[i];
		
		pruneTree(path, cost);  // Gets rid of unneeded edges
		
		return total;  // Return total cost
	}
	
	public void createSpanningTree(int current, boolean[] known, int[] cost, int[] path) {  // Check for cycle
		int[] temp = new int[vertices];
		ArrayList<Integer> parent = new ArrayList<Integer>();
		ArrayList<Integer> parentCost = new ArrayList<Integer>();
		boolean allTrue = false;
		known[current] = true;  // Update known array
		
		for (int i=0; i<vertices; i++) {  // Construct temp cost array
			if (edges[current][i] != 0) 
				temp[i] = edges[current][i];
		}
		
		Arrays.sort(temp);  // Sort
		int max = temp[temp.length-1];  // Find max cost
		for (int i=0; i<vertices; i++) {  // Get lowest non-zero cost
			if ((temp[i] != 0) && (temp[i] < max))
				max = temp[i];
		}
		
		int v = 0;
		for (int i=0; i<vertices; i++) {  // Get index of vertex to update
			if (edges[current][i] == max) 
				v = i;
		}	
		
		if (allTrue(known) == true) {  // If all are true
//			System.out.println("All vertices known");
		}
		else if (known[v] == false) {  // If next vertex has false known value
			cost[v] = max;  // Update cost array
			path[v] = current;  // Update path array		
			if (!allTrue)
				createSpanningTree(v, known, cost, path);  
		}
		else {  // if known[v] = true, go to unknown vertex
			boolean looking = true;
			int index = 0;
			while (looking) {  // Find first unknown vertex
				if (known[index] == true)
					index++;
				else 
					looking = false;
			}
			for (int i=0; i<vertices; i++) {
				if (edges[i][index] != 0)
					parent.add(i);  // Find parent vertices of unknown vertex
			}
			for (int i:parent)  // Get costs of parent vertices
				parentCost.add(edges[i][index]);
			int c = Collections.min(parentCost);  // Find least cost value
			int p = 0;
			for (int i=0; i<parent.size(); i++) {  // Get parent of least cost
				if (parentCost.get(i) == c)
					p = parent.get(i);
			}
			known[index] = true;  // Update all arrays
			cost[index] = c;
			path[index] = p;
			if (!allTrue)
				createSpanningTree(index, known, cost, path);
		}
	}
	
	// Returns true if all vertices known
	private boolean allTrue(boolean[] array) {  
		for (boolean value : array) {
	        if (!value)
	            return false;
	    }
	    return true;
	}
	
	private void pruneTree(int[] path, int[] cost) {
		int[][] temp = new int[vertices][vertices];  // Temp 2D array
		for (int i=0; i<path.length; i++) {  // Populate array
			if (path[i] != -2) {
				temp[path[i]][i] = cost[i];
			}
		}
		edges = temp;
	}
	
}
