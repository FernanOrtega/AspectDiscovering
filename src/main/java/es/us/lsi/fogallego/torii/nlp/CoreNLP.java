package es.us.lsi.fogallego.torii.nlp;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;
import es.us.lsi.fogallego.torii.domain.DependencyNode;
import es.us.lsi.fogallego.torii.domain.GenericTree;
import es.us.lsi.fogallego.torii.domain.Token;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adapter of Stanford CoreNLP
 */
public class CoreNLP {

    private String modelPath;
    private String taggerPath;
    private String parserPath;
    private DependencyParser parser;
    private MaxentTagger tagger;

    public CoreNLP() {
        modelPath = "edu/stanford/nlp/models/parser/nndep/UD_Spanish.gz";
        taggerPath = "edu/stanford/nlp/models/pos-tagger/spanish/spanish.tagger";
        parserPath = "edu/stanford/nlp/models/lexparser/spanishPCFG.ser.gz";

        tagger = new MaxentTagger(taggerPath);

        parser = DependencyParser.loadFromModelFile(modelPath);
    }

    public List<Token> tokenise(String pText, boolean fixPeriod) {
        List<Token> lstTokens;
        String text = pText;

        if (!pText.endsWith(".")) {
            text += " .";
        }

        GenericTree dependencyTree = getDependencyTree(text).get(0);
        List<GenericTree<DependencyNode, DependencyNode>> lstDescendant = dependencyTree.getEveryDescendants();

        lstDescendant = lstDescendant.stream()
                .sorted(Comparator.comparingInt(node -> node.getNodeInfo().getToken().getIndex()))
                .collect(Collectors.toList());

        lstTokens = lstDescendant.stream().map(node -> node.getNodeInfo().getToken()).collect(Collectors.toList());

        if (!pText.endsWith(".") && !fixPeriod) {
            lstTokens.remove(lstTokens.size() - 1);
        }

        return lstTokens;
    }

    public List<GenericTree<DependencyNode, DependencyNode>> getDependencyTree(String text) {
        List<GenericTree<DependencyNode, DependencyNode>> lstDependencyTree = new ArrayList<>();

        String extraOptions = "ptb3Escaping=false,strictTreebank3=true";
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
        TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.coreLabelFactory(extraOptions);
        tokenizer.setTokenizerFactory(tokenizerFactory);

        for (List<HasWord> sentence : tokenizer) {
            List<TaggedWord> lstTaggedWord = tagger.apply(sentence);
            GrammaticalStructure gs = parser.predict(lstTaggedWord);
            lstDependencyTree.add(gramStruct2DepTree(gs, lstTaggedWord));
        }

        return lstDependencyTree;
    }

    /**
     * Auxiliary method for DepTree
     *
     * @param grammaticalStructure
     * @param lstTaggedWord
     * @return
     */
    private GenericTree<DependencyNode, DependencyNode> gramStruct2DepTree(GrammaticalStructure grammaticalStructure,
                                                                           List<TaggedWord> lstTaggedWord) {

        Map<Integer, List<TypedDependency>> mapDependencies = getMapDependencies(grammaticalStructure);
        TypedDependency typedDependencies = mapDependencies.get(0).get(0);
        IndexedWord indexedWord = typedDependencies.dep();
        GenericTree<DependencyNode, DependencyNode> root = new GenericTree<>();

        String word = indexedWord.word();
        TaggedWord taggedWord = lstTaggedWord.get(indexedWord.index() - 1);
        root.setNodeInfo(new DependencyNode("root", new Token(word,
                UtilPosTagger.stem(word),
                indexedWord.tag(),
                "root",
                indexedWord.index(),
                taggedWord.beginPosition(), taggedWord.endPosition())));

        fillChildrenDep(root, mapDependencies, lstTaggedWord);
        return root;
    }

    /**
     * Auxiliary method for DepTree
     *
     * @param node
     * @param mapDependencies
     * @param lstTaggedWord
     */
    private void fillChildrenDep(GenericTree<DependencyNode, DependencyNode> node, Map<Integer,
            List<TypedDependency>> mapDependencies, List<TaggedWord> lstTaggedWord) {
        List<TypedDependency> typedDependencies = mapDependencies.get(node.getNodeInfo().getToken().getIndex());
        if (typedDependencies != null) {
            for (TypedDependency typedDependency : typedDependencies) {
                IndexedWord dep = typedDependency.dep();
                GrammaticalRelation reln = typedDependency.reln();
                String dependencyTag = reln.getShortName();
                GenericTree<DependencyNode, DependencyNode> child = new GenericTree<>();
                child.setParent(node);
                int index = dep.index();
                TaggedWord taggedWord = lstTaggedWord.get(index - 1);
                child.setNodeInfo(new DependencyNode(dependencyTag, new Token(dep.word(),
                        UtilPosTagger.stem(dep.word()),
                        dep.tag(),
                        dependencyTag,
                        index, taggedWord.beginPosition(),
                        taggedWord.endPosition())));
                node.getChildren().add(child);
                fillChildrenDep(child, mapDependencies, lstTaggedWord);
            }
        }
    }

    private Map<Integer, List<TypedDependency>> getMapDependencies(GrammaticalStructure grammaticalStructure) {
        Map<Integer, List<TypedDependency>> mapDependencies = new HashMap<>();

        for (TypedDependency typedDependency : grammaticalStructure.typedDependencies()) {
            Integer source = typedDependency.gov().index();

            List<TypedDependency> lstTypedDependencies = mapDependencies.computeIfAbsent(source, k -> new ArrayList<>());

            lstTypedDependencies.add(typedDependency);
        }

        return mapDependencies;
    }
}
