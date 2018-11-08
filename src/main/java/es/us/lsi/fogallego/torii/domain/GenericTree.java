package es.us.lsi.fogallego.torii.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class GenericTree<T, C> implements Serializable {

    private T nodeInfo;
    private GenericTree parent;
    private List<GenericTree<C, ?>> children;

    public GenericTree() {
        super();
        children = new ArrayList<>();
    }

    public List<GenericTree<?, ?>> getEveryDescendants() {
        List<GenericTree<?, ?>> lstDescendants = new ArrayList<>();
        lstDescendants.addAll(this.children);
        this.children.forEach(child -> lstDescendants.addAll(child.getEveryDescendants()));

        return lstDescendants;
    }

    public int getDepth() {
        int depth;

        if (children.size() == 0) {
            depth = 0;
        } else {
            OptionalInt max = children.stream().mapToInt(child -> 1 + child.getDepth()).max();
            depth = max.isPresent() ? max.getAsInt() : 0;
        }

        return depth;
    }

    public T getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(T nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public GenericTree getParent() {
        return parent;
    }

    public void setParent(GenericTree parent) {
        this.parent = parent;
    }

    public List<GenericTree<C, ?>> getChildren() {
        return children;
    }

    public void setChildren(List<GenericTree<C, ?>> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericTree)) return false;

        GenericTree<?, ?> that = (GenericTree<?, ?>) o;

        if (nodeInfo != null ? !nodeInfo.equals(that.nodeInfo) : that.nodeInfo != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        return children != null ? children.equals(that.children) : that.children == null;
    }

    @Override
    public int hashCode() {
        int result = nodeInfo != null ? nodeInfo.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
