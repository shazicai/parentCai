package cn.com.springCai.dataStructure.binaryTreeStructure;

/**
 * @Author caiJH
 * @Date 2025/12/15 4:47 PM
 * @Version 1.0
 * @Description 红黑树
 */
public class ReadBlackStructure {
    public static void main(String[] args) {
        System.out.println("hello world");
    }

    /**
     * 插入节点默认先是红色
     * 插入 左 根 右
     * LL：右单旋，父换爷+染色    爷旋转，父与爷节点颜色取反
     * RR：左单旋，父换爷+染色    叔、父，爷节点颜色取反，将爷节点当作新插入的节点
     * LR：先左 后右旋，儿换爷+染色     儿与爷节点颜色取反
     * RL：先右 后左旋，儿换爷+染色     儿与爷节点颜色取反
     */
    public void insert() {

    }
}
