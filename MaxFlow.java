
// Java program for implementation of Ford Fulkerson algorithm
import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

class MaxFlow
{

  // class variables
  static int numVertices = 0;    //Number of vertices in graph

  // will be set false if the total number of games team A can possibly win
  // is less than the current number of wins for another team
  static boolean winStillPossible = true;




  public static void main (String[] args) throws java.lang.Exception
  {
    //Scanner sc = new Scanner(System.in);
    //int number = sc.nextInt();

    MaxFlow m = new MaxFlow();

    processInput();

    // Let us create a graph shown in the above example
    int graph[][] =new int[][] { {0, 16, 13, 0, 0, 0},
                                {0, 0, 10, 12, 0, 0},
                                {0, 4, 0, 0, 14, 0},
                                {0, 0, 9, 0, 0, 20},
                                {0, 0, 0, 7, 0, 4},
                                {0, 0, 0, 0, 0, 0}
                              };

    System.out.println("The maximum possible flow is " + m.fordFulkerson(graph, 0, 5));

  }

  // reads input and creates a matrix that will represent the graph
  static int[][] processInput()
  {
    Scanner sc = new Scanner(System.in);  // create Scanner
    int numberOfTeams = sc.nextInt();  // take first line of input
    int counter = 1;
    int nFactorial = 1;
    for(int i = 2; i <= numberOfTeams - 1; i++)
    {
      nFactorial = nFactorial * i;
    }
    int nMinusTwoFactorial = 1;
    for(int i = 2; i <= numberOfTeams - 3; i++)
    {
      nMinusTwoFactorial = nMinusTwoFactorial * i;
    }

    int teamCombinations = (nFactorial) / (2 * nMinusTwoFactorial);

    // final calculation for number of vertices in the graph
    numVertices = 2 + (numberOfTeams - 1) + teamCombinations;

    // store the second line of input, the number of wins for each team
    int teamsWins[] = new int[numberOfTeams];
    int tmp = 0;
    for(int i = 0; i < numberOfTeams; i++)
    {
      tmp = sc.nextInt();
      teamsWins[i] = tmp;
      System.out.println(teamsWins[i]);
    }


    // make teamsWins[0] into the total number of games that team A can win in total
    for(int i = 0; i < numberOfTeams; i++)
    {
      tmp = sc.nextInt();
      teamsWins[0] = teamsWins[0] + tmp;
    }

    System.out.println(teamsWins[0]);

    // changes all other values in teamsWins into the maximum games those teams
    // can will while still giving team A a chance
    for(int i = 1; i < numberOfTeams; i++)
    {
      if(teamsWins[i] > teamsWins[0])
      {
        winStillPossible = false; // in case team A can not win regardless
      }
      else
      {
        teamsWins[i] = teamsWins[0] - teamsWins[i];
        System.out.println(teamsWins[i]);
      }
    }

    System.out.println();

    // matrix representation of the graph
    int graphRepresentation[][] = new int[numVertices][numVertices]; //STUB
    int tempCounter = 1;

    // directed edges from the source to the second column of vertices
    for(int i = 1; i < numberOfTeams; i++)
    {
      for(int j = numberOfTeams - i; j <= numberOfTeams; j++)
      {
        sc.nextInt();
      }
      for(int k = i + 1; k < numberOfTeams; k++)
      {
        graphRepresentation[0][tempCounter] = sc.nextInt();
        tempCounter++;
        //System.out.print(sc.nextInt() + " ");
      }
    }

    // debug print stuff
    System.out.println();
    for(int i = 0; i < numVertices; i++)
    {
      System.out.print(graphRepresentation[0][i] + " ");
    }


    // directed edges from the second column of vertices to the third column
    for(int i = 1; i <= teamCombinations; i++)
    {
    }



    //System.out.println(sc.nextInt());
    //System.out.println(sc.nextInt());
    //System.out.println(sc.nextInt());



    return graphRepresentation; //STuB
  }





  /* public static int getInt()
  {
    Scanner sc = new Scanner(System.in);  // create Scanner
    int numberOfTeams = sc.nextInt();  // take first line of input
    int counter = 1;
    int nFactorial = 1;
    for(int i = 2; i <= numberOfTeams - 1; i++)
    {
      nFactorial = nFactorial * i;
    }
    int nMinusTwoFactorial = 1;
    for(int i = 2; i <= numberOfTeams - 3; i++)
    {
      nMinusTwoFactorial = nMinusTwoFactorial * i;
    }

    // final calculation for number of vertices in the graph
    int numberOfNodes = 2 + (numberOfTeams - 1) + ((nFactorial) / (2 * nMinusTwoFactorial));

    System.out.println(numberOfNodes);
    return numberOfNodes;
  }*/







  // BELOW IS STUFF THAT COMES FROM
  // https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
  // *****************************************************************************














  /* Returns true if there is a path from source 's' to sink
    't' in residual graph. Also fills parent[] to store the
    path */
  boolean bfs(int rGraph[][], int s, int t, int parent[])
  {
    // Create a visited array and mark all vertices as not
    // visited
    boolean visited[] = new boolean[numVertices];
    for(int i=0; i < numVertices; ++i)
      visited[i]=false;

    // Create a queue, enqueue source vertex and mark
    // source vertex as visited
    LinkedList<Integer> queue = new LinkedList<Integer>();
    queue.add(s);
    visited[s] = true;
    parent[s]=-1;

    // Standard BFS Loop
    while (queue.size()!=0)
    {
      int u = queue.poll();

      for (int v=0; v < numVertices; v++)
      {
        if (visited[v]==false && rGraph[u][v] > 0)
        {
          queue.add(v);
          parent[v] = u;
          visited[v] = true;
        }
      }
    }

    // If we reached sink in BFS starting from source, then
    // return true, else false
    return (visited[t] == true);
  }

  // Returns tne maximum flow from s to t in the given graph
  int fordFulkerson(int graph[][], int s, int t)
  {
    int u, v;

    // Create a residual graph and fill the residual graph
    // with given capacities in the original graph as
    // residual capacities in residual graph

    // Residual graph where rGraph[i][j] indicates
    // residual capacity of edge from i to j (if there
    // is an edge. If rGraph[i][j] is 0, then there is
    // not)
    int rGraph[][] = new int[numVertices][numVertices];

    for (u = 0; u < numVertices; u++)
      for (v = 0; v < numVertices; v++)
        rGraph[u][v] = graph[u][v];

    // This array is filled by BFS and to store path
    int parent[] = new int[numVertices];

    int max_flow = 0;  // There is no flow initially

    // Augment the flow while tere is path from source
    // to sink
    while (bfs(rGraph, s, t, parent))
    {
      // Find minimum residual capacity of the edhes
      // along the path filled by BFS. Or we can say
      // find the maximum flow through the path found.
      int path_flow = Integer.MAX_VALUE;
      for (v=t; v!=s; v=parent[v])
      {
        u = parent[v];
        path_flow = Math.min(path_flow, rGraph[u][v]);
      }

      // update residual capacities of the edges and
      // reverse edges along the path
      for (v=t; v != s; v=parent[v])
      {
        u = parent[v];
        rGraph[u][v] -= path_flow;
        rGraph[v][u] += path_flow;
      }

      // Add path flow to overall flow
      max_flow += path_flow;
    }

    // Return the overall flow
    return max_flow;
  }

}
