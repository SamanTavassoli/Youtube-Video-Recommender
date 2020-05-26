import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Type Command:");
            String input = scanner.nextLine();

            if (input.contains("run")) {
                run(input.substring(input.indexOf("run"))); // assuming it's gonna be run NAME
            }

            if (input.equals("exit")) {
                isRunning = false;
            }
        }
    }

    private static void run(String name) {
        // get history for name
        String[] history = getHistory(name);

        // get tags for videos in history
        String[] tags = getTags(history);

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

    private static String[] getHistory(String name) {
        
        return null;
    }

    private static String[] getTags(String[] history) {
        return null;
    }
}
