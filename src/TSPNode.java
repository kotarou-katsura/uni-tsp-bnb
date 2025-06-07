import java.util.*;

class TSPNode implements Comparable<TSPNode> {
    int[][] reducedMatrix;
    int cost;
    int level;
    int vertex;
    List<Integer> path;

    public TSPNode(int[][] parentMatrix, List<Integer> path, int level, int vertex, int cost) {
        this.reducedMatrix = deepCopy(parentMatrix);
        this.level = level;
        this.vertex = vertex;
        this.path = new ArrayList<>(path);
        this.path.add(vertex);
        this.cost = cost;
    }

    @Override
    public int compareTo(TSPNode other) {
        return this.cost - other.cost;
    }

    private int[][] deepCopy(int[][] matrix) {
        int[][] copy = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            copy[i] = matrix[i].clone();
        return copy;
    }
}