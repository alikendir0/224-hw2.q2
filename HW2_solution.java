import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class HW2_solution {
  public static void main(String[] args) {
    FileRead a = new FileRead("HW2_Q2_text.txt");// file location given to fileread
    int X = Integer.parseInt(a.Read());// getting the number of vertices through fileread
    System.out.println("V= " + X);
    Digraph G = new Digraph(X);// initiating digraph
    int Y = Integer.parseInt(a.Read());// getting the number of edges through fileread
    System.out.println("E= " + Y);
    Valuefinder k = new Valuefinder();// iniating valuefinder
    for (int i = 0; i < Y; i++) {// adding the edges Y times
      int[] temp = k.value((a.Read()));
      G.addEdge(temp[0], temp[1]);
      System.out.println(temp[0] + " " + temp[1]);
    }
    int minValue = Collections.min(G.indexes);// since we are dealing with a neural network the minimum value will be
                                              // the first neuron
    DirectedDFS h = new DirectedDFS(G, minValue);// iniating algorithm
    System.out.println("Result is");
    for (int i = 2; i < h.maxSize; i++) {// printing out the paths in length order
      for (ArrayList<Integer> innerList : h.twoDArrayList) {
        if (innerList.size() < h.maxSize && innerList.size() == i) {// algorithm also returns the output neurons here we
                                                                    // are excluding them
          Collections.reverse(innerList);// algorithm returns the paths
          System.out.println(innerList);
        }
      }
    }
  }

  static class Digraph {
    int V;// number of vertices
    int E;// number of edges
    LinkedList<Integer>[] adj;// adjacency list of vertices
    LinkedList<Integer> indexes;// indexing list since neurons will have namings (0,10,11,12) instead of indexes
                                // on the input

    public Digraph(int N) {// constructor
      V = N;
      indexes = new LinkedList<Integer>();
      adj = (LinkedList<Integer>[]) new LinkedList[V];
      for (int v = 0; v < V; v++)
        adj[v] = new LinkedList<Integer>();// adj list for each vertex
    }

    public int V() {
      return V;// retrun number of vertices
    }

    public void addEdge(int v, int w) {
      int i = indexing(v);
      int j = indexing(w);
      adj[i].add(j);// adding directed edge betwen v and w with their names in mind
      E++;
    }

    public Iterable<Integer> adj(int v) {
      int x = indexing(v);
      LinkedList<Integer> output = new LinkedList<>();
      for (int i : adj[x]) {
        output.add(revindexing(i));// reversing the name of vertex and outputting its adjacent vertices
      }
      return output;
    }

    public int adjSize(int k) {// returns the number of adjacent vertices of a vertex, crucial for the
                               // algorithm
      Iterable<Integer> h = adj(k);
      int count = 0;
      for (Object item : h) {
        count++;
      }
      return count;
    }

    public int indexing(int u) {
      if (!indexes.contains(u))// if indexes does not contain the new number, add it
        indexes.add(u);
      return indexes.indexOf(u);// return the index of the new number
    }

    public int revindexing(int u) {// reverse indexing
      return indexes.get(u);// return the indexes number
    }
  }

  static class DirectedDFS {
    private boolean[] marked;// marked array
    private int[] edgeTo;// edgeto array to store parents
    private int s;// starting vertex
    private ArrayList<ArrayList<Integer>> twoDArrayList;// storing the paths
    private int maxSize = -1;// store max length of the neural network

    public DirectedDFS(Digraph g, int d) {
      s = d;// starting vertex
      marked = new boolean[g.V()];// creating marked array in the number of vertices
      edgeTo = new int[g.V()];// creating edgeto array in the number of vertices
      twoDArrayList = new ArrayList<ArrayList<Integer>>();// iniating the arraylist that will store the paths

      int temp = -1;// variable used to find maxsize

      while (temp != 1) {
        ArrayList<Integer> list = new ArrayList<Integer>();// will store a path
        dfs(list, g, s);
        list.add(s);// adding the starting vertex to the path
        twoDArrayList.add(list);// storing the path to the paths arraylist
        temp = list.size();// checking to find the maxsize
        if (temp > maxSize) {
          maxSize = temp;
        }
      }
    }

    private void dfs(ArrayList<Integer> list, Digraph g, int v) {
      for (int w : g.adj(v)) {// looping through each adjacent vertex of v
        if (!marked(g, w)) {// if its marked stop
          edgeTo[g.indexing(w)] = v;// adjacent vertexes' parent is v
          dfs(list, g, w);// recursive dfs
          if (g.adjSize(w) == 0)// if w has no adjacent vertices
            if (s != edgeTo[g.indexing(w)]) {// if w's parent is not the starting vertex
              while (marked[g.indexing(w)]) {// mark the parent vertex, if its marked mark its parent instead
                if (w != s)
                  break;
                w = g.revindexing(edgeTo[g.indexing(w)]);
              }
              marked[g.indexing(edgeTo[g.indexing(w)])] = true;
            } else {
              marked[g.indexing(w)] = true;
            }
          list.add(w);
          return;
        }
      }
    }

    public boolean marked(Digraph g, int v) {
      return marked[g.indexing(v)];
    }

    public int edgeTo(int v) {
      return edgeTo[v];
    }
  }
}