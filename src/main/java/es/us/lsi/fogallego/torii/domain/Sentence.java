package es.us.lsi.fogallego.torii.domain;

import java.util.List;

public class Sentence {

    private String text;
    private List<Token> lstToken;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Token> getLstToken() {
        return lstToken;
    }

    public void setLstToken(List<Token> lstToken) {
        this.lstToken = lstToken;
    }
}
