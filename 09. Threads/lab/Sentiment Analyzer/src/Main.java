//import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
//import bg.sofia.uni.fmi.mjt.sentimentanalyzer.ParallelSentimentAnalyzer;
//import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
//import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;
//import bg.sofia.uni.fmi.mjt.sentimentanalyzer.lexicon.SentimentLexiconLoader;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main {
//    public static void main(String[] args) {
//
//        Path lexiconPath = Path.of("AFINN-111.txt");
//        Map<String, SentimentScore> sentimentLexicon = SentimentLexiconLoader.loadLexicon(lexiconPath);
//        String[] inputs = {
//            "I love programming, it's so fun. But sometimes I hate when the code doesn't work.",
//            "Today was a good day. I felt happy and accomplished, though I had some challenges.",
//            "I feel so sad today. Everything seems bad and nothing is going right.",
//            "I love working on new projects. However, I hate the pressure of deadlines.",
//            "Life is good. I am happy with my work and personal life.",
//            "The weather is nice today, which makes me feel good. I love sunny days.",
//            "I feel bad about the mistakes I made yesterday. It's tough to fix things.",
//            "Hate is such a strong word. It's better to focus on good things.",
//            "Good things come to those who wait. I am confident about the future.",
//            "Sad to see my friends leave, but I know they will be successful in their new journey."
//        };
//        AnalyzerInput[] analyzerInputs = new AnalyzerInput[inputs.length];
//        for (int i = 0; i < inputs.length; i++) {
//            analyzerInputs[i] = new AnalyzerInput("ID-" + i, new StringReader(inputs[i]));
//        }
//        Set<String> stopwords;
//        try (var br = Files.newBufferedReader(Path.of("stopwords.txt"))) {
//            stopwords = br.lines().collect(Collectors.toSet());
//        } catch (IOException ex) {
//            throw new SentimentAnalysisException("Error reading");
//        }
//        ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(5, stopwords, sentimentLexicon);
//        Map<String, SentimentScore> results = analyzer.analyze(analyzerInputs);
//        // Example usage
//        System.out.println("Word 'happy': " + sentimentLexicon.get("happy"));
//        System.out.println("Word 'sad': " + sentimentLexicon.get("sad"));
//
//    }
//}