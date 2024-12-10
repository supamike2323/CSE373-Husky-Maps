import minpq.MinPQ;
import minpq.PriorityNode;
import minpq.UnsortedArrayMinPQ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import minpq.PriorityNode;



/**
 * Display the most commonly-reported WCAG recommendations.
 */
public class ReportAnalyzer {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("data/wcag.tsv");
        Map<String, String> wcagDefinitions = new LinkedHashMap<>();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            String index = "wcag" + line[0].replace(".", "");
            String title = line[1];
            wcagDefinitions.put(index, title);
        }

        Pattern re = Pattern.compile("wcag\\d{3,4}");
        List<String> wcagTags = Files.walk(Paths.get("data/reports"))
                .map(path -> {
                    try {
                        return Files.readString(path);
                    } catch (IOException e) {
                        return "";
                    }
                })
                .flatMap(contents -> re.matcher(contents).results())
                .map(MatchResult::group)
                .toList();

        Map<String, Integer> tagCounts = new HashMap<>();
        for (String tag : wcagTags) {
            tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
        }

        MinPQ<PriorityNode<String>> pq = new UnsortedArrayMinPQ<>();
        for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
            String tag = entry.getKey();
            int count = entry.getValue();

            PriorityNode<String> node = new PriorityNode<>(tag, count);

            if (pq.size() < 3) {
                pq.add(node, count);
            } else if (count > pq.peekMin().getPriority()) {
                pq.removeMin();
                pq.add(node, count);
            }
        }

        List<String> topTags = new ArrayList<>();
        while (!pq.isEmpty()) {
            topTags.add(pq.removeMin().getElement());
        }
        Collections.reverse(topTags);

        System.out.println("Top 3 most commonly-reported WCAG recommendations:");
        for (String tag : topTags) {
            String description = wcagDefinitions.getOrDefault(tag, "Unknown WCAG Tag");
            System.out.printf("%s: %s%n", tag, description);
        }
    }
}