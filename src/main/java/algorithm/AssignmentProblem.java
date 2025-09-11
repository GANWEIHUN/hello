package algorithm;

import java.util.*;

/**
 * 分支限界算法
 * <p>
 * 纪录学生时代<a href="https://zhidao.baidu.com/question/512967780.html?entry=qb_uhome_tag">期末作业-任务分配问题</a>
 *
 * @author tomato
 * &#064;date  2025/9/10 11:48
 */
public class AssignmentProblem {
    private final int n;
    private final int[][] costMatrix;
    private int minCost = Integer.MAX_VALUE;
    private int[] bestAssignment;

    // 节点类表示搜索树中的节点
    private static class Node implements Comparable<Node> {
        int worker;
        int cost;
        int lowerBound;
        int[] assigned;
        Node parent;

        Node(int worker, int cost, int[] assigned, Node parent) {
            this.worker = worker;
            this.cost = cost;
            this.assigned = Arrays.copyOf(assigned, assigned.length);
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.lowerBound, other.lowerBound);
        }
    }

    public AssignmentProblem(int[][] costMatrix) {
        this.n = costMatrix.length;
        this.costMatrix = costMatrix;
        this.bestAssignment = new int[n];
        Arrays.fill(bestAssignment, -1);
    }

    // 计算下界（最小可能成本）
    private int calculateLowerBound(Node node) {
        //已分配成本
        int bound = node.cost;
        int[] assigned = node.assigned;
        boolean[] assignedWorkers = new boolean[n];

        // 标记已分配的工人
        for (int i = 0; i < n; i++) {
            if (assigned[i] != -1) {
                assignedWorkers[assigned[i]] = true;
            }
        }

        // 剩余任务的最小可能成本
        for (int i = 0; i < n; i++) {
            if (assigned[i] == -1) {
                int min = Integer.MAX_VALUE;
                int lastAssignedWorker = -1;
                for (int j = 0; j < n; j++) {
                    if (!assignedWorkers[j] && costMatrix[j][i] < min) {
                        min = costMatrix[j][i];
                        // （临时）标记该工人为已分配
                        assignedWorkers[j] = true;
                        if (lastAssignedWorker != -1) {
                            // 恢复上一个工人的未分配状态
                            assignedWorkers[lastAssignedWorker] = false;
                        }
                        lastAssignedWorker = j;
                    }
                }
                bound += min;
                // 恢复该工人的未分配状态
                if (lastAssignedWorker != -1) {
                    assignedWorkers[lastAssignedWorker] = false;
                }
            }
        }

        return bound;
    }

    // 解决任务分配问题
    public void solve() {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // 创建根节点
        int[] initialAssigned = new int[n];
        Arrays.fill(initialAssigned, -1);
        Node root = new Node(-1, 0, initialAssigned, null);
        root.lowerBound = calculateLowerBound(root);
        pq.add(root);

        while (!pq.isEmpty()) {
            Node minNode = pq.poll();
            int nextWorker = minNode.worker + 1;

            // 如果所有工人都已分配
            if (nextWorker == n) {
                if (minNode.cost < minCost) {
                    minCost = minNode.cost;
                    bestAssignment = minNode.assigned;
                }
                continue;
            }

            // 为下一个工人尝试所有未分配的任务
            for (int task = 0; task < n; task++) {
                if (minNode.assigned[task] == -1) {
                    int[] newAssigned = Arrays.copyOf(minNode.assigned, n);
                    newAssigned[task] = nextWorker;
                    int newCost = minNode.cost + costMatrix[nextWorker][task];

                    Node child = new Node(nextWorker, newCost, newAssigned, minNode);
                    child.lowerBound = calculateLowerBound(child);

                    // 只有当下界小于当前最小成本时才扩展节点
                    if (child.lowerBound < minCost) {
                        pq.add(child);
                    }
                }
            }
        }
    }

    // 打印解决方案
    public void printSolution() {
        System.out.println("最优分配方案:");
        for (int i = 0; i < n; i++) {
            System.out.println("工人 " + bestAssignment[i] + " -> 任务 " + i +
                    " (成本: " + costMatrix[bestAssignment[i]][i] + ")");
        }
        System.out.println("总最小成本: " + minCost);
    }

    public static void main(String[] args) {
        // 示例成本矩阵
        int[][] costMatrix = {
                {9, 2, 7, 8},
                {6, 4, 3, 7},
                {5, 8, 1, 8},
                {7, 6, 9, 4}
        };

        AssignmentProblem problem = new AssignmentProblem(costMatrix);
        problem.solve();
        problem.printSolution();
    }
}

