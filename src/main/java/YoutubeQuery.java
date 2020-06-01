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

/**
 * Interacts with the youtube api to give search results for given tags
 * Parses the response and outputs them in the form of recommendations
 */
public class YoutubeQuery {

    /**
     * Made from api itself, not sure exactly how it works
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Made from api itself, not sure exactly how it works
     */
    private static final String APPLICATION_NAME = "YOUTUBE-VIDEO-RECOMMENDER";

    /**
     * Gives recommendations for tags provided
     * Creates a query search term from the tags
     * Calls the youtube api with the search term
     * Parses the output of the api
     * @param tags the tags for which recommendations will be made
     * @return parsed output of api call
     */
    public static String[] getRecommendationsForTags(String[] tags) {
        try {
            return parseResponse(callAPI(createQuerySearchTerm(tags)));
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

    /**
     * Parses the response of the api call
     * The title and the video id of the video are searched for using the json dependency
     * If the json search fails for one of the responses then it is replaced with a default message
     * @param response response from api call
     * @return parsed response
     */
    private static String[] parseResponse(String[] response) {
        for (int i = 0; i < response.length; i++) {
            try {
                JSONObject obj = new JSONObject(response[i]);

                String videoTitle = obj.getJSONObject("snippet").getString("title");
                String videoId = "https://www.youtube.com/watch?v=" + obj.getJSONObject("id").getString("videoId");
                String parsedElement = "[\"" + videoTitle + "\", " + videoId + "]";

                response[i] = parsedElement;
            } catch (Exception e) {
                response[i] = "Could not find videoId or title for: " + response[i];
            }
        }
        return response;
    }

    /**
     * Creates a Search.List request from a new Youtube instance
     * Asks for the user's API KEY
     * Fetches response, converts it to a String[] and returns it
     * @param querySearchTerm The tags which will be searched for
     * @return Response from the api as a String[]
     * @throws GeneralSecurityException If httpTransport could not be created - not sure exactly
     * @throws IOException If httpTransport, request or response fail - not sure how exactly
     */
    private static String[] callAPI(String querySearchTerm) throws GeneralSecurityException, IOException {

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // have to create a new service from which I can request things from youtube
        YouTube youTubeService = new YouTube.Builder(httpTransport,JSON_FACTORY,null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // setting the request type and setting api key for credentials
        YouTube.Search.List request = youTubeService.search().list("snippet");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter API KEY: ");
        String API_KEY = scanner.nextLine().trim();
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
