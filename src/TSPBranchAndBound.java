
import java.util.*;
import java.util.Random;
public class TSPBranchAndBound {
    static final int INF = Integer.MAX_VALUE;

    public static int solveTSP(int[][] costMatrix) {
        int nodeCount = 0;

        int n = costMatrix.length;
        PriorityQueue<TSPNode> pq = new PriorityQueue<>();

        List<Integer> initialPath = new ArrayList<>();
        TSPNode root = new TSPNode(costMatrix, initialPath, 0, 0, 0);
        root.cost = reduceMatrix(root.reducedMatrix); // initial bound
        pq.add(root);

        int minCost = INF;
        List<Integer> bestPath = null;

        while (!pq.isEmpty()) {
            TSPNode min = pq.poll();

            // Prune nodes with cost >= current minCost
            if (min.cost >= minCost) {
                continue;
            }

            nodeCount++;

            if (min.level == n - 1) {
                int lastToFirst = costMatrix[min.vertex][0];
                if (lastToFirst != INF) {
                    int totalCost = min.cost + lastToFirst;
                    if (totalCost < minCost) {
                        minCost = totalCost;
                        min.path.add(0);
                        bestPath = min.path;
                    }
                }
                continue;
            }

            // Generate children only if min.cost < minCost (already checked)
            for (int i = 0; i < n; i++) {
                if (min.reducedMatrix[min.vertex][i] != INF) {
                    int[][] childMatrix = deepCopy(min.reducedMatrix);

                    for (int j = 0; j < n; j++) {
                        childMatrix[min.vertex][j] = INF;
                        childMatrix[j][i] = INF;
                    }
                    childMatrix[i][0] = INF;

                    int newCost = min.cost + min.reducedMatrix[min.vertex][i] + reduceMatrix(childMatrix);

                    if (newCost < minCost) {  // prune here too
                        TSPNode child = new TSPNode(childMatrix, min.path, min.level + 1, i, newCost);
                        pq.add(child);
                    }
                }
            }
        }


        System.out.println("Minimum cost: " + minCost);
        System.out.println("Path: " + bestPath);
        System.out.println("Number of node expansions: " + nodeCount);
        return nodeCount;

    }

    private static int reduceMatrix(int[][] matrix) {
        int n = matrix.length;
        int cost = 0;

        // Row reduction
        for (int i = 0; i < n; i++) {
            int rowMin = INF;
            for (int j = 0; j < n; j++)
                if (matrix[i][j] < rowMin)
                    rowMin = matrix[i][j];

            if (rowMin != INF && rowMin != 0) {
                cost += rowMin;
                for (int j = 0; j < n; j++)
                    if (matrix[i][j] != INF)
                        matrix[i][j] -= rowMin;
            }
        }

        // Column reduction
        for (int j = 0; j < n; j++) {
            int colMin = INF;
            for (int i = 0; i < n; i++)
                if (matrix[i][j] < colMin)
                    colMin = matrix[i][j];

            if (colMin != INF && colMin != 0) {
                cost += colMin;
                for (int i = 0; i < n; i++)
                    if (matrix[i][j] != INF)
                        matrix[i][j] -= colMin;
            }
        }

        return cost;
    }

    private static int[][] deepCopy(int[][] matrix) {
        int[][] copy = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            copy[i] = matrix[i].clone();
        return copy;
    }

    public static void main(String[] args) {
        int sum=0;
        int count =10000;
        int size =10;
        int boundRandom=50;

        Random rand = new Random();
        int[][] costMatrix=new int[size][size];

        for (int i=0; i<count;i++){
            for(int j=0;j<size;j++){
                for(int k=0; k<size;k++){
                    if(k==j){
                        costMatrix[j][k]=INF;
                    }
                    else{
                        costMatrix[j][k]=rand.nextInt(boundRandom);
                    }
                }
            }

            sum+=solveTSP(costMatrix);
        }

        System.out.print("AVG number of tries:");
        System.out.println(sum/count);
    }
}
