package es.us.lsi.fogallego.torii.domain;

public class DependencyNode {
    private String dependencyTag;
    private Token token;

    public DependencyNode(String dependencyTag, Token token) {
        this.dependencyTag = dependencyTag;
        this.token = token;
    }

    public String getDependencyTag() {
        return dependencyTag;
    }

    public void setDependencyTag(String dependencyTag) {
        this.dependencyTag = dependencyTag;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "DependencyNode{" +
                "dependencyTag='" + dependencyTag + '\'' +
                ", token=" + token +
                '}';
    }
}
