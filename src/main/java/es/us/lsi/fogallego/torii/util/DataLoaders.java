package es.us.lsi.fogallego.torii.util;

import au.com.bytecode.opencsv.CSVReader;
import es.us.lsi.fogallego.torii.domain.Sentence;
import es.us.lsi.fogallego.torii.nlp.CoreNLP;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DataLoaders {
    private static final String SENTIMENT_WORDS_FILE = "sentimentwords.txt";
    private static CoreNLP corenlp = new CoreNLP();

    public static List<Sentence> loadSentences(String language, String domain, String sentencesFile,
                                               Integer numSamples) throws IOException {

        List<Sentence> lstSentence;
        CSVReader csvReader;
        String filename;
        String[] line;

        filename = sentencesFile;
        csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"), ',', '\"', 1);
        lstSentence = new ArrayList<>();

        while ((line = csvReader.readNext()) != null && lstSentence.size() < numSamples) {
            if (line.length == 5 && line[2].equals(language) && line[3].equals(domain)) {
                lstSentence.add(createSentence(line[1]));
            }

        }
        csvReader.close();
        System.out.println("Loaded " + lstSentence.size() + " sentences!");

        return lstSentence;
    }

    private static Sentence createSentence(String sentenceText) {

        Sentence sentence;

        sentence = new Sentence();
        sentence.setText(sentenceText);

        sentence.setLstToken(corenlp.tokenise(sentenceText, false));

        return sentence;
    }

    public static Map<String, Double> loadSentimentWeights(String language, String resourcesFolder) throws IOException {

        String filePath;
        Map<String, Double> result;

        filePath = resourcesFolder + "/dicts/" + language + "/" + SENTIMENT_WORDS_FILE;

        result = Files.readAllLines(Paths.get(filePath)).stream()
                .map(line -> line.split("\t"))
                .collect(Collectors.toMap(e -> e[0], e -> Double.valueOf(e[1])));

        return result;
    }

    public static Set<String> loadFileWithString(String language, String resourcesFolder, String fileName) throws IOException {

        String filePath;
        Set<String> result;

        filePath = resourcesFolder + "/dicts/" + language + "/" + fileName;

        result = new HashSet<>(Files.readAllLines(Paths.get(filePath)));

        return result;
    }
}
