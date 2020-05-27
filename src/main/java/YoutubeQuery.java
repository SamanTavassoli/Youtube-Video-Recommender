import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
<<<<<<< HEAD
import java.util.List;
import java.util.Scanner;
=======
import java.util.ArrayList;
import java.util.List;
>>>>>>> fa737c8ba26415d260de210da642776387cee7d5


public class YoutubeQuery {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "YOUTUBE-VIDEO-RECOMMENDER";
<<<<<<< HEAD
    private static String API_KEY;
=======
    private static final String API_KEY = "AIzaSyCKuIK_Ke31BcFKW3yGUM2SZYqGG1ha_oU";
>>>>>>> fa737c8ba26415d260de210da642776387cee7d5


    public static void main(String[] args) throws GeneralSecurityException, IOException {

        String[] results = getResults(null);
        for (String result : results) {
            System.out.println(result);
        }
    }
    public static String[] getRecommendationsForTags(String[] tags) {
        return new String[0];
    }

    private static String[] getResults(String[] args) throws GeneralSecurityException, IOException {

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // have to create a new service from which I can request things from youtube
        YouTube youTubeService = new YouTube.Builder(httpTransport,JSON_FACTORY,null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // setting the request type and setting api key for credentials
        YouTube.Search.List request = youTubeService.search().list("snippet");
<<<<<<< HEAD
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter API KEY: ");
        API_KEY = scanner.nextLine();
=======
>>>>>>> fa737c8ba26415d260de210da642776387cee7d5
        request.setKey(API_KEY);

        // fetching response for "food"
        SearchListResponse response = request.setMaxResults(25L)
                .setQ("food")
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
