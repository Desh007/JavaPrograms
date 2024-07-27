import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class BitbucketPRFetcher {

    private static final String BITBUCKET_API_BASE_URL = "https://api.bitbucket.org/2.0";
    private static final String REPO_OWNER = "your-repo-owner";
    private static final String REPO_SLUG = "your-repo-slug";
    private static final String USERNAME = "your-username";
    private static final String APP_PASSWORD = "your-app-password";

    public static void main(String[] args) {
        try {
            JSONArray openPRs = fetchOpenPullRequests(REPO_OWNER, REPO_SLUG);
            for (int i = 0; i < openPRs.length(); i++) {
                JSONObject pr = openPRs.getJSONObject(i);
                System.out.println("PR ID: " + pr.getInt("id"));
                System.out.println("Title: " + pr.getString("title"));
                System.out.println("Author: " + pr.getJSONObject("author").getString("display_name"));
                System.out.println("Source Branch: " + pr.getJSONObject("source").getJSONObject("branch").getString("name"));
                System.out.println("Destination Branch: " + pr.getJSONObject("destination").getJSONObject("branch").getString("name"));
                System.out.println("Description: " + pr.optString("description", "No description"));
                System.out.println("Created On: " + pr.getString("created_on"));
                System.out.println("------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray fetchOpenPullRequests(String owner, String repoSlug) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = BITBUCKET_API_BASE_URL + "/repositories/" + owner + "/" + repoSlug + "/pullrequests?state=OPEN";
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", Credentials.basic(USERNAME, APP_PASSWORD))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            return json.getJSONArray("values");
        }
    }
}
