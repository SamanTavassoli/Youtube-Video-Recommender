import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


public class YoutubeQuery {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "YOUTUBE-VIDEO-RECOMMENDER";
    private static final String API_KEY = "***REMOVED***";


    public static String[] getRecommendationsForTags(String[] tags) {
        return null;
    }

    public static void getResults(String[] args) throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        YouTube youTubeService = new YouTube.Builder(httpTransport,JSON_FACTORY,null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        YouTube.Search.List request = youTubeService.search().list("snippet");
        request.setKey(API_KEY);

        SearchListResponse response = request.setMaxResults(25L)
                .setQ("food")
                .execute();

        System.out.println("Response: " + response);
    }
}
