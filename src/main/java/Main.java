import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * This class recommends videos to watch depending on a user's video viewing history
 * It refers to two txt files containing users' viewing histories and the tags for the videos viewed
 * It can also be asked to provide a list of users from the viewing history file so that one of them
 * can be picked in order to give recommendations
 */
public class Main {

    // ---- Paths of reference files

    private static final Path VIDEO_TAGS_PATH = Path.of(System.getProperty("user.dir") + "/src/main/resources/input/videoTags.txt");
    private static final Path VIEWING_HISTORY_PATH = Path.of(System.getProperty("user.dir") + "/src/main/resources/input/viewingHistory.txt");

    // ---- Maps holding data

    /**
     * Holds the viewing histories of different users
     * Each user is a String
     * The viewing history is an ArrayList of Strings
     */
    private static HashMap<String, ArrayList<String>> viewingHistories = new HashMap<>();
    /**
     * Holds the tags for different videos
     * Each video is a String
     * The tags list is an ArrayList of Strings
     */
    private static HashMap<String, ArrayList<String>> videoTags = new HashMap<>();

    // -------- Main & Run --------

    public static void main(String[] args) {

        instantiateVideoTags();
        instantiateViewingHistories();

        run("Lionel Messi");

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nType Command or help for a list of commands available:");
            String input = scanner.nextLine();

            if (input.contains("run")) {
                run(input.substring(4)); // assuming it's gonna be run NAME, "run " -> 4
            }

            if (input.equals("exit")) {
                isRunning = false;
            }

            if (input.equals("list")) {
                printNamesInViewingHistory();
            }

            if (input.equals("help")) {
                System.out.println("Available commands are:\n" +
                        "run (name) - Make video recommendations for the given name\n" +
                        "list - print a list of names in the viewing history\n" +
                        "exit - exit the program");
            }
        }
    }

    /**
     * Prints video recommendations given a name
     *
     * Gets viewing history for that name
     * Gets tags for viewing history
     * Gets recommendations from YoutubeQuery
     * Prints Recommendations
     *
     * @param name name for which recommendations are to be given
     */

    private static void run(String name) {

        ArrayList<String> history = getHistory(name);
        ArrayList<String> tags = getTags(history);

        String[] temp = new String[tags.size()];
        System.out.println("tags:");
        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i]);
            temp[i] = tags.get(i);
        }

        String[] recommendations = YoutubeQuery.getRecommendationsForTags(temp);
        printRecommendations(recommendations);
    }

    // -------- Map getters --------

    private static ArrayList<String> getHistory(String name) {
        return viewingHistories.get(name);
    }

    private static ArrayList<String> getTags(ArrayList<String> history) {
        ArrayList<String> tags = new ArrayList<>();
        for (String video : history) {
            tags.addAll(videoTags.get(video));

        }

        return tags;
    }

    // -------- Print requests --------

    private static void printNamesInViewingHistory() {
        System.out.println("Names in viewing history: ");
        for (String name : viewingHistories.keySet()) {
            System.out.println(name);
        }
    }

    /**
     * Prints recommendations provided with a header followed by each recommendation
     * @param recommendations recommendations to be printed
     */
    private static void printRecommendations(String[] recommendations) {
        // print recommendations
        System.out.println("Recommendations: " + recommendations.length);
        int count = 1;
        for (String recommendation : recommendations) {
            System.out.println(count + " " + recommendation);
            count++;
        }
    }

    // -------- Parsing history and tags into maps --------

    // ---- Instantiations calls

    private static void instantiateViewingHistories() {
        String[] lines = getFileLines(VIEWING_HISTORY_PATH);
        parseLinesIntoMap(lines, viewingHistories, "name", "history");
    }

    private static void instantiateVideoTags() {
        String[] lines = getFileLines(VIDEO_TAGS_PATH);
        parseLinesIntoMap(lines, videoTags, "video", "tags");
    }

    // ---- Fetching files

    /**
     * Returns an array of strings representing the lines of a txt file
     * @param path path of the txt file
     * @return the lines of the file in an array of strings
     */
    private static String[] getFileLines(Path path) {
        if (Files.exists(path)) {
            try {
                return Files.readString(path).split("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Could not find path: " + path);
        }
        return new String[0];
    }

    // ---- Parsing of data

    /**
     * Parses the lines from the viewing history and video tags files into their respective hashmaps
     * @param lines lines of data obtained from history or tags files
     * @param map viewing history or video tags hashmap
     * @param firstGroupName name / video
     * @param secondGroupName history / tags
     */
    private static void parseLinesIntoMap(String[] lines, HashMap<String, ArrayList<String>> map, String firstGroupName, String secondGroupName) {
        for (int i = 0; i < lines.length; i += 2) {
            String firstLine = lines[i];
            String secondLine = lines[i + 1];
            map.put(parseGroupContent(firstLine, firstGroupName), splitIntoElements(parseGroupContent(secondLine, secondGroupName)));
        }
    }

    /**
     * Parses a line which starts with a group name
     * Simply removes the group name from the line
     * @param line line to remove group name from
     * @param groupName group name to be removed from line
     * @return the line without the group name
     */
    private static String parseGroupContent(String line, String groupName) {
        return line.substring(groupName.length() + 1).trim();
    }

    /**
     * Splits a line containing elements separated with a "," into an ArrayList of Strings
     * @param line line containing the elements
     * @return the separated elements in an ArrayList
     */
    private static ArrayList<String> splitIntoElements(String line) {
        String[] elements = line.split(",");

        for (int i = 0; i < elements.length; i++) {
            elements[i] = elements[i].trim();
        }

        return new ArrayList<String>(Arrays.asList(elements));
    }
}
