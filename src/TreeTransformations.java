import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

class TreeNode {
    int id;
    TreeNode parent;
    List<TreeNode> children;

    TreeNode(int id) {
        this.id = id;
        this.parent = null;
        this.children = new ArrayList<>();
    }
}

public class TreeTransformations {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TreeTransformations <path_to_file1> <path_to_file2>");
            return;
        }

        String file1 = args[0];
        String file2 = args[1];

        TreeNode root1 = parseFile(file1);
        TreeNode root2 = parseFile(file2);

        List<String> transformations = calculateTransformations(root1, root2);
        System.out.println(transformations);
    }

    private static TreeNode parseFile(String filename) {
        Map<Integer, TreeNode> nodeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Paths.get(filename).toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] edges = line.trim().split("\\s*\\]\\s*");
                for (String edge : edges) {
                    String[] nodes = edge.substring(1).split(", ");
                    int parentId = Integer.parseInt(nodes[0]);
                    int childId = Integer.parseInt(nodes[1]);

                    nodeMap.putIfAbsent(parentId, new TreeNode(parentId));
                    nodeMap.putIfAbsent(childId, new TreeNode(childId));

                    TreeNode parent = nodeMap.get(parentId);
                    TreeNode child = nodeMap.get(childId);
                    parent.children.add(child);
                    child.parent = parent;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodeMap.values().stream().filter(node -> node.parent == null).findFirst().orElse(null);
    }

    private static List<String> calculateTransformations(TreeNode root1, TreeNode root2) {
        List<String> removeOperations = new ArrayList<>();
        List<String> addOperations = new ArrayList<>();
        List<TreeNode> tree1Nodes = new ArrayList<>();
        List<TreeNode> tree2Nodes = new ArrayList<>();

        fillNodesList(root1, tree1Nodes);
        fillNodesList(root2, tree2Nodes);

        // Remove nodes from tree1 that are not present in tree2 or have different parents
        removeInDepthFirstOrder(root1, tree2Nodes, removeOperations);

        // Add nodes from tree2 that are not present in tree1 or have different parents
        for (TreeNode node2 : tree2Nodes) {
            if (node2.parent == null) { // Skip the root node
                continue;
            }
            TreeNode node1 = findNodeById(tree1Nodes, node2.id);
            if (node1 == null || node1.parent == null || node1.parent.id != node2.parent.id) {
                addOperations.add("ADD(" + node2.parent.id + ", " + node2.id + ")");
            }
        }

        List<String> result = new ArrayList<>();
        result.addAll(removeOperations);
        result.addAll(addOperations);
        return result;
    }

    private static void removeInDepthFirstOrder(TreeNode node, List<TreeNode> tree2Nodes, List<String> removeOperations) {
        for (TreeNode child : node.children) {
            removeInDepthFirstOrder(child, tree2Nodes, removeOperations);
        }
        if (node.parent != null) { // Skip the root node
            TreeNode node2 = findNodeById(tree2Nodes, node.id);
            if (node2 == null || node2.parent == null || node2.parent.id != node.parent.id) {
                removeOperations.add("REMOVE(" + node.id + ")");
            }
        }
    }

    private static void fillNodesList(TreeNode node, List<TreeNode> nodesList) {
        nodesList.add(node);
        for (TreeNode child : node.children) {
            fillNodesList(child, nodesList);
        }
    }

    private static TreeNode findNodeById(List<TreeNode> nodes, int id) {
        for (TreeNode node : nodes) {
            if (node.id == id) {
                return node;
            }
        }
        return null;
    }
}
