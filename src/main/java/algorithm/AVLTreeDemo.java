package algorithm;

/**
 *
 * @author tomato
 * &#064;date  2025/9/11 17:51
 */


public class AVLTreeDemo {

    Node root;

    // 获取节点高度
    int height(Node N) {
        if (N == null) {
            return 0;
        }
        return N.height;
    }

    // 获取平衡因子
    int getBalance(Node N) {
        if (N == null) {
            return 0;
        }
        return height(N.left) - height(N.right);
    }

    // 右旋转
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // 左旋转
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // 插入节点
    Node insert(Node node, int key) {
        if (node == null) {
            return new Node(key);
        }

        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        } else {
            return node; // 不允许重复键
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // 左左情况
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        // 右右情况
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        // 左右情况
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // 右左情况
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // 中序遍历（升序）
    void inOrder(Node node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.key + " ");
            inOrder(node.right);
        }
    }

    // 打印树结构（辅助方法）
    void printTree(Node root, String indent, boolean last) {
        if (root != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("└─");
                indent += "  ";
            } else {
                System.out.print("├─");
                indent += "│ ";
            }
            System.out.println(root.key);

            printTree(root.left, indent, false);
            printTree(root.right, indent, true);
        }
    }

    public static void main(String[] args) {
        AVLTreeDemo tree = new AVLTreeDemo();

        // 插入根节点0和子节点1-6
        tree.root = tree.insert(tree.root, 0);
        for (int i = 1; i <= 6; i++) {
            tree.root = tree.insert(tree.root, i);
        }

        System.out.println("中序遍历结果（升序）:");
        tree.inOrder(tree.root);

        System.out.println("\n\n树结构:");
        tree.printTree(tree.root, "", true);
    }

    static class Node {
        int key, height;
        Node left, right;

        Node(int d) {
            key = d;
            height = 1;
        }
    }
}

