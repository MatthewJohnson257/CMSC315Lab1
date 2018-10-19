// Basel Arafat and Matthew Johnson
// CMSC 315 Algorithms, Lab 1 - Max Flow Applications
// Source for the Implementation of the Ford-Fulkerson Max Flow:
// https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/

// Java program for implementation of Ford Fulkerson algorithm
import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

class MaxFlow
{
  // number of vertices in graph
  static int numVertices = 0;

  // will be set false if the total number of games team A can possibly win
  // is less than the current number of wins for another team at this point
  // in the season
  static boolean winStillPossible = true;

  // if true, the program will print debug statements
  static boolean debug = false;


  public static void main (String[] args) throws java.lang.Exception
  {


    MaxFlow m = new MaxFlow();

    // retrieve a graph representation of the max flow problem, given the input
    // from standard input.  The graph is implemented using a two dimensional
    // array, where each row represents a starting vertex of an edge, each column
    // represents an ending vertex of an edge, and each value in the array
    // represents the weight of that edge
    int graph[][] = processInput();

    // the max flow that is necessary, given the graph representation, such that
    // team A still has a chance to win/tie overall
    int maxFlowRequired = 0;
    for(int i = 0; i < numVertices; i++)
    {
      maxFlowRequired = maxFlowRequired + graph[0][i];
    }

    // debug print statements
    if(debug)
    {
      System.out.println("maxFlowRequired: " + maxFlowRequired);
      System.out.println("numVertices: " + numVertices);
      System.out.println("The maximum possible flow is " + m.fordFulkerson(graph, 0, numVertices - 1));
      System.out.println("winStillPossible: " + winStillPossible);
    }

    if(winStillPossible)
    {
      // run max flow on the graph
      if(m.fordFulkerson(graph, 0, numVertices - 1) >= maxFlowRequired)
      {
        // team A has a chance to win/tie overall
        System.out.println("yes");
      }
      else
      {
        // team A does not have a chance to win/tie overall
        System.out.println("no");
      }
    } else
    {
      // some team other than team A has more wins currently that team A can
      // accumulate the rest of the season
      System.out.println("no");
    }


  }

  // reads input and creates a matrix that will represent the graph
  static int[][] processInput()
  {
    Scanner sc = new Scanner(System.in);  // create Scanner
    int numberOfTeams = sc.nextInt();  // take first line of input


    int counter = 1;
    int nFactorial = 1;

    // calculate factorials which are later used in calculation the combinations
    // of games played by the teams other than A. (Ex. BC, BD, BE, CD, CE, DE)
    for(int i = 2; i <= numberOfTeams - 1; i++)
    {
      nFactorial = nFactorial * i;
    }
    int nMinusTwoFactorial = 1;
    for(int i = 2; i <= numberOfTeams - 3; i++)
    {
      nMinusTwoFactorial = nMinusTwoFactorial * i;
    }

    // using the calculation of combinations, find the number of team game
    // combinations of the games that don't involve team A
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
    }


    // make teamsWins[0] into the total number of games that team A can win in total
    // assuming it wins all its remaining games
    for(int i = 0; i < numberOfTeams; i++)
    {
      tmp = sc.nextInt();
      teamsWins[0] = teamsWins[0] + tmp;
    }

    // changes all other values in teamsWins into the maximum games those teams
    // can will while still giving team A a chance to tie/win the season
    for(int i = 1; i < numberOfTeams; i++)
    {
      if(teamsWins[i] > teamsWins[0])
      {
        winStillPossible = false; // in case team A can not win regardless
        // this occurs if the total possible wins team A can get is less than
        // the number of wins of any team at this point in the season
      }
      else
      {
        teamsWins[i] = teamsWins[0] - teamsWins[i];
      }
    }

    // matrix representation of the graph
    int graphRepresentation[][] = new int[numVertices][numVertices];

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
      }
    }


    // directed edges from the second column of vertices to the third column
    tempCounter = 1;
    for(int i = teamCombinations + 1; i < teamCombinations + numberOfTeams - 1; i++)
    {
      for(int j = i + 1; j < teamCombinations + numberOfTeams; j++)
      {
        graphRepresentation[tempCounter][i] = graphRepresentation[0][tempCounter];
        graphRepresentation[tempCounter][j] = graphRepresentation[0][tempCounter];
        tempCounter++;
      }
    }


    // directed edges from the third column of vertices to the sink vertex
    for(int i = 1; i < numberOfTeams; i++)
    {
      graphRepresentation[teamCombinations + i][numVertices - 1] = teamsWins[i];
    }


    // debug print statements
    if(debug)
    {
      System.out.println("Contents of graphRepresentation: ");
      for(int i = 0; i < numVertices; i++)
      {
        for(int j = 0; j < numVertices; j++)
        {
          System.out.print(graphRepresentation[i][j] + " ");
        }
        System.out.println();
      }
      System.out.println();
    }


    return graphRepresentation;
  }






  // ***************************************************************************
  // The below code was found at:
  // https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
  // ***************************************************************************







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
