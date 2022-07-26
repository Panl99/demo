package com.lp.demo.action.designpatterns_in_action.compositepattern;


import java.util.Enumeration;
import java.util.Vector;

/**
 * @author lp
 * @date 2022/7/26 21:57
 * @desc 1.定义一个树形结构
 **/
public class TreeNode {

    private String name;
    private TreeNode parent;
    private Vector<TreeNode> children = new Vector<>();

    public TreeNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * 添加子节点
     * @param node
     */
    public void add(TreeNode node) {
        children.add(node);
    }
    /**
     * 移除子节点
     * @param node
     */
    public void remove(TreeNode node) {
        children.remove(node);
    }
    /**
     * 获取子节点
     */
    public Enumeration<TreeNode> getChildren() {
        return children.elements();
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "name='" + name + '\'' +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}
