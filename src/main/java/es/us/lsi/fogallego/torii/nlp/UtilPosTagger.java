package es.us.lsi.fogallego.torii.nlp;

import es.us.lsi.fogallego.torii.domain.Token;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.SpanishStemmer;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UtilPosTagger implements Serializable {

    public static String stem(String word) {
        SnowballProgram stemmer = new SpanishStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        stemmer.stem();
        stemmer.stem();

        return stemmer.getCurrent();
    }

    public static String reconstructString(List<Token> lstToken, boolean lowercased) {
        StringBuilder result = new StringBuilder();
        if (!lstToken.isEmpty()) {
            int lastIndex = lstToken.get(0).getBeginPosition();
            for (Token token : lstToken) {
                int diff = token.getBeginPosition() - lastIndex;
                lastIndex = token.getEndPosition();
                if (diff > 0) {
                    result.append(String.join("", Collections.nCopies(diff, " ")));
                }

                if (lowercased) {
                    result.append(token.getWord().toLowerCase());
                } else {
                    result.append(token.getWord());
                }
            }
        }

        return result.toString();
    }
}
