
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nType Command or help for a list of commands:");
            String input = scanner.nextLine();

            if (input.contains("run")) {
                run(input.substring(4)); // assuming it's gonna be run NAME, "run " -> 4
            }

            if (input.equals("exit")) {
                isRunning = false;
            }

            if (input.equals("list")) {
                System.out.println("Names in viewing history: ");
                for (String name : getNamesInViewingHistory()) {
                    System.out.println(name);
                }
            }

            if (input.equals("help")) {
                System.out.println("Available commands are: run (name), exit, list");
            }
        }
    }

    private static void run(String name) {
        System.out.println("Getting recommendations for: " + name);

        // get history for name
        String[] history = getHistory(name);
        if (history.length == 0) {
            System.out.println("No history obtained, will not search");
            return;
        }
        System.out.print("Video history: ");
        for (String video : history) {
            System.out.print(video + ", ");
        }
        System.out.println();

        // get tags for videos in history
        String[] tags = getTags(history);
        if (tags.length == 0) {
            System.out.println("No tags obtained, will not search");
            return;
        }
        System.out.print("Tags: ");
        for (String tag : tags) {
            System.out.print(tag + ", ");
        }
        System.out.println();


        // get recommendations for tags from youtube api
        String[] recommendations = YoutubeQuery.getRecommendationsForTags(tags);

        // print recommendations
        System.out.println("Recommendations: " + recommendations.length);
        int count = 1;
        for (String recommendation : recommendations) {
            System.out.println(count + " " + recommendation);
            count++;
        }
    }

    /**
     * Return a String[] containing the videos in the history of whoever's name is provided
     */
    private static String[] getHistory(String name) {

        try {
            Path path = Path.of(System.getProperty("user.dir") + "/src/main/resources/input/viewingHistory.txt");
            if (Files.exists(path)) {
                String fileContent = Files.readString(path);
                String[] viewingHistory = fileContent.split("\n");
                // going through history in steps of 2 to get names
                for (int i = 0; i < viewingHistory.length; i += 2) {
                    if (viewingHistory[i].contains(name)) { // TODO: improve so that not entering exact name does not pick wrong name
                        return parseHistory(viewingHistory[i + 1]);
                    }
                }
                System.out.println("Could not find name in viewingHistory: " + name);
                System.out.println("Type the command \"list\" to get the names in the viewingHistory file");
            } else {
                System.out.println("Could not find path: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private static String[] parseHistory(String history) {
        history = history.replace("history:", "");
        String[] parsedHistory = history.split(",");
        for (int i = 0; i < parsedHistory.length; i++) {
            parsedHistory[i] = parsedHistory[i].trim();
        }
        return parsedHistory;
    }

    private static String[] getTags(String[] history) {

        try {
            Path path = Path.of(System.getProperty("user.dir") + "/src/main/resources/input/videoTags.txt");
            if (Files.exists(path)) {
                String fileContent = Files.readString(path);
                String[] videoTags = fileContent.split("\n");
                ArrayList<String> tags = new ArrayList<>();
                // going through videos in steps of 2 to get tags
                for (String video : history) {
                    for (int i = 0; i < videoTags.length; i += 2) {
                        if (videoTags[i].contains(video)) {
                            tags.addAll(parseTags(videoTags[i + 1]));
                        }
                    }
                }
                String[] tagsInArray = new String[tags.size()];
                for (int i = 0; i < tags.size(); i++) {
                    tagsInArray[i] = tags.get(i);
                }
                return tagsInArray;
            } else {
                System.err.println("Could not find path: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private static ArrayList<String> parseTags(String tags) {
        tags = tags.replace("tags:", "");
        String[] parsedTags = tags.split(",");
        ArrayList<String> returnList = new ArrayList<>();
        for (int i = 0; i < parsedTags.length; i++) {
            returnList.add(parsedTags[i].trim());
        }
        return returnList;
    }

    private static String[] getNamesInViewingHistory() {
        try {
            Path path = Path.of(System.getProperty("user.dir") + "/src/main/resources/input/viewingHistory.txt");
            if (Files.exists(path)) {
                String fileContent = Files.readString(path);
                String[] viewingHistory = fileContent.split("\n");
                String[] names = new String[viewingHistory.length / 2];
                for (int i = 0; i < names.length; i++) {
                    String nameLine = viewingHistory[i*2];
                    names[i] = nameLine.substring(6); // "name: " -> 6
                }
                return names;
            } else {
                System.err.println("Could not find path: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

}
