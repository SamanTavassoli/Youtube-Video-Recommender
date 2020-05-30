import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Scanner;


public class YoutubeQuery {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "YOUTUBE-VIDEO-RECOMMENDER";
    private static String API_KEY;

    public static String[] getRecommendationsForTags(String[] tags) {
        try {
            return parseResults(getResults(createQuerySearchTerm(tags)));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            System.err.println("No results were obtained from search");
        }
        return new String[0];
    }

    /**
     * ORs all the tags together so the search can contain either tags
     */
    private static String createQuerySearchTerm(String[] tags) {
        return String.join("|", tags);
    }

    private static String[] parseResults(String[] results) {
        for (int i = 0; i < results.length; i++) {
            try {
                String jsonString = results[i];
                JSONObject obj = new JSONObject(jsonString);
                String videoTitlte = obj.getJSONObject("snippet").getString("title");
                String videoId = "https://www.youtube.com/watch?v=" + obj.getJSONObject("id").getString("videoId");
                String result = "[\"" + videoTitlte + "\", " + videoId + "]";
                results[i] = result;
            } catch (Exception e) {
                results[i] = "Could not find videoId or title for: " + results[i];
            }
        }
        return results;
    }

    private static String[] getResults(String querySearchTerm) throws GeneralSecurityException, IOException {

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // have to create a new service from which I can request things from youtube
        YouTube youTubeService = new YouTube.Builder(httpTransport,JSON_FACTORY,null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // setting the request type and setting api key for credentials
        YouTube.Search.List request = youTubeService.search().list("snippet");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter API KEY: ");
        API_KEY = scanner.nextLine().trim();
        request.setKey(API_KEY);

        // fetching response for fed in argument
        SearchListResponse response = request.setMaxResults(5L)
                .setQ(querySearchTerm)
                .execute();

        // Putting all the responses into an array
        List<SearchResult> items = response.getItems();
        String[] arrayResponse = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            arrayResponse[i] = String.valueOf(items.get(i));
        }

        return arrayResponse;
    }
}
