package es.us.lsi.fogallego.torii.domain;

import java.util.Objects;

public class Token {

    private String word;
    private String stem;
    private String posTag;
    private String depTag;
    private int index;
    private int beginPosition;
    private int endPosition;

    public Token() {
        super();
    }

    public Token(String word, String stem, String posTag, String depTag, int index, int beginPosition, int endPosition) {
        this.word = word;
        this.stem = stem;
        this.posTag = posTag;
        this.depTag = depTag;
        this.index = index;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
    }

    public String getWord() {
        return word;
    }

    public String getStem() {
        return stem;
    }

    public String getPosTag() {
        return posTag;
    }

    public String getDepTag() {
        return depTag;
    }

    public int getIndex() {
        return index;
    }

    public int getBeginPosition() {
        return beginPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public void setDepTag(String depTag) {
        this.depTag = depTag;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setBeginPosition(int beginPosition) {
        this.beginPosition = beginPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(stem, token.stem);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stem);
    }

    @Override
    public String toString() {
        return "Token{" +
                "word='" + word + '\'' +
                ", stem='" + stem + '\'' +
                ", posTag='" + posTag + '\'' +
                ", depTag='" + depTag + '\'' +
                ", index=" + index +
                ", beginPosition=" + beginPosition +
                ", endPosition=" + endPosition +
                '}';
    }
}
