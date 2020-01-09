package utils.algorithm.bts;

/**
 * @author zhangjianxin
 * @version 2019-12-30.
 * @url github.com/uk0
 * @project vladimir
 * @since JDK1.8.
 */
public class Node {

    // root

    private Node treeRoot;

    // 左节点(儿子)

    private Node lefNode;

    // 右节点(儿子)

    private Node rightNode;

    // 数据

    private int value;

    public Node(int value) {
        this.value = value;
    }

    public Node() {
    }

    public Node getLefNode() {
        return lefNode;
    }

    public void setLefNode(Node lefNode) {
        this.lefNode = lefNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getTreeRoot() {
        return treeRoot;
    }

    public void setTreeRoot(Node treeRoot) {
        this.treeRoot = treeRoot;
    }


    public static void createTree(Node node, int value) {


        // 如果不是root节点那么 set root 节点

        if (node.getTreeRoot() == null) {
            Node node1 = new Node(value);
            node.setTreeRoot(node1);
        }
        // 无论任何一颗子树，左边都比根要小，右边比根要大
        //当前树根
        Node tempRoot = node.getTreeRoot();

        while (tempRoot != null) {
            //当前值大于根值，往右边走
            if (value > tempRoot.getValue()) {
                //右边没有树根，那就直接插入
                if (tempRoot.getRightNode() == null) {
                    Node node1 = new Node(value);
                    tempRoot.setRightNode(node1);
                    return;
                } else {
                    //如果右边有树根，到右边的树根去
                    tempRoot = tempRoot.getRightNode();

                }

            } else {
                //左没有树根，那就直接插入
                if (tempRoot.getLefNode() == null) {
                    tempRoot.setLefNode(new Node(value));

                    return;
                } else {
                    //如果左有树根，到左边的树根去
                    tempRoot = tempRoot.getLefNode();
                }
            }

        }
    }


    public static void main(String[] args) {

        //根节点-->10
        Node treeNode1 = new Node(10);

        //左孩子-->9
        Node treeNode2 = new Node(9);

        //右孩子-->20
        Node treeNode3 = new Node(20);

        //20的左孩子-->15
        Node treeNode4 = new Node(15);

        //20的右孩子-->35
        Node treeNode5 = new Node(35);
        //根节点的左右孩子
        treeNode1.setLefNode(treeNode2);
        treeNode1.setRightNode(treeNode3);

        //20节点的左右孩子
        treeNode3.setLefNode(treeNode4);
        treeNode3.setRightNode(treeNode5);


        preTraverseBTree(treeNode1);
        inTraverseBTree(treeNode1);



        int[] arrays = {2, 3, 1, 4, 5};

        //动态创建树

        Node root = new Node();
        for (int value : arrays) {
            createTree(root, value);
        }
        preTraverseBTree(root.getTreeRoot());
        inTraverseBTree(root.getTreeRoot());
        System.out.println(getHeight(root.getTreeRoot()));;
    }

    /**
     * 先序遍历
     *
     * @param rootNode 根节点
     */
    public static void preTraverseBTree(Node rootNode) {

        if (rootNode != null) {

            //访问根节点
            System.out.println(rootNode.getValue());

            //访问左节点
            preTraverseBTree(rootNode.getLefNode());

            //访问右节点
            preTraverseBTree(rootNode.getRightNode());
        }
    }

    /**
     * 中序遍历
     *
     * @param rootNode 根节点
     */
    public static void inTraverseBTree(Node rootNode) {

        if (rootNode != null) {

            //访问左节点
            inTraverseBTree(rootNode.getLefNode());

            //访问根节点
            System.out.println(rootNode.getValue());

            //访问右节点
            inTraverseBTree(rootNode.getRightNode());
        }
    }
    public static int getHeight(Node treeNode) {

        if (treeNode == null) {
            return 0;
        } else {

            //左边的子树深度
            int left = getHeight(treeNode.getLefNode());

            //右边的子树深度
            int right = getHeight(treeNode.getRightNode());


            int max = left;

            if (right > max) {
                max = right;
            }
            return max + 1;
        }
    }
}

