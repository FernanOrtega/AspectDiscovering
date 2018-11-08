package es.us.lsi.fogallego.torii;

import es.us.lsi.fogallego.torii.domain.Sentence;
import es.us.lsi.fogallego.torii.domain.Token;
import es.us.lsi.fogallego.torii.util.DataLoaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EntryPoint {

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.err.println("Wrong number of parameters");
            System.err.println("Usage: EntryPoint <language> <domain> <sentences file> <freq min>");
        } else if(!Files.exists(Paths.get(args[2]))) {
            System.err.println("File " + args[2] + " doesn't exist.");
        } else if(!isDouble(args[3]) && Double.parseDouble(args[4]) <= 0) {
            System.err.println("Num samples " + args[4] + " wrong.");
        } else {
            computeAspects(args[0], args[1], args[2], Double.parseDouble(args[3]));
        }
    }

    private static void computeAspects(String language, String domain, String sentencesFile, double freqMin) throws IOException {

        List<Sentence> sentences;
        Map<Token, Integer> mapAspectCandidate;
        List<Token> aspects;

        // Load sentences and compute the token's tuple
        sentences = DataLoaders.loadSentences(language, domain, sentencesFile, 1000);
        mapAspectCandidate = new HashMap<>();

        assert sentences != null;
        for (Sentence sentence : sentences) {
            List<Token> lstAspectCandidate;
            Set<Token> aspectsOfSentence;

            lstAspectCandidate = getCandidateAspect(sentence);
            aspectsOfSentence = new HashSet<>();

            for (Token aspectCandidate : lstAspectCandidate) {
                Integer freq = mapAspectCandidate.get(aspectCandidate);

                if (freq == null) {
                    freq = 0;
                } else if(!aspectsOfSentence.contains(aspectCandidate)) {
                    freq += 1;
                    aspectsOfSentence.add(aspectCandidate);
                }

                mapAspectCandidate.put(aspectCandidate, freq);
            }

        }

        aspects = filterAspects(mapAspectCandidate, sentences.size(), freqMin);

        aspects.forEach(System.out::println);
    }

    private static List<Token> filterAspects(Map<Token, Integer> mapAspectCandidate, int numSentences, double freqMin) {

        int minNumSentences;

        minNumSentences = (int) (numSentences * freqMin);

        return mapAspectCandidate.entrySet().stream().filter(e -> e.getValue() >= minNumSentences)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static List<Token> getCandidateAspect(Sentence sentence) {

        return sentence.getLstToken().stream()
                .filter(token -> token.getPosTag().startsWith("nc"))
                .filter(token -> token.getDepTag().equals("nmod")
                        || token.getDepTag().equals("dobj")
                        || token.getDepTag().equals("nsubj")
                )
                .filter(token -> Pattern.matches("\\w+",token.getWord()))
                .collect(Collectors.toList());
    }

    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

}
