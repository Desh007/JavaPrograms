import okhttp3.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class BitbucketPRChecker {

    private static final String BITBUCKET_API_BASE_URL = "https://api.bitbucket.org/2.0";
    private static final String REPO_OWNER = "your-repo-owner";
    private static final String REPO_SLUG = "your-repo-slug";
    private static final String USERNAME = "your-username";
    private static final String APP_PASSWORD = "your-app-password";
    private static final File LOCAL_REPO_PATH = new File("/path/to/local/repo");

    public static void main(String[] args) {
        try {
            JSONArray openPRs = fetchOpenPullRequests(REPO_OWNER, REPO_SLUG);
            for (int i = 0; i < openPRs.length(); i++) {
                JSONObject pr = openPRs.getJSONObject(i);
                String sourceBranch = pr.getJSONObject("source").getJSONObject("branch").getString("name");
                String destinationBranch = pr.getJSONObject("destination").getJSONObject("branch").getString("name");
                System.out.println("PR ID: " + pr.getInt("id") + ", Source Branch: " + sourceBranch + ", Destination Branch: " + destinationBranch);
                checkMergeConflicts(sourceBranch, destinationBranch);
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

    private static void checkMergeConflicts(String sourceBranch, String destinationBranch) {
        try {
            // Clone or open the repository
            if (!LOCAL_REPO_PATH.exists()) {
                Git.cloneRepository()
                        .setURI("https://your-bitbucket-username:your-app-password@bitbucket.org/your-repo-owner/your-repo-slug.git")
                        .setDirectory(LOCAL_REPO_PATH)
                        .call();
            }
            Repository repository = new RepositoryBuilder()
                    .setGitDir(new File(LOCAL_REPO_PATH, ".git"))
                    .build();

            try (Git git = new Git(repository)) {
                // Fetch all branches
                git.fetch()
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(USERNAME, APP_PASSWORD))
                        .call();

                // Checkout the destination branch
                git.checkout().setName(destinationBranch).call();

                // Attempt to merge the source branch
                git.merge()
                        .include(repository.findRef("refs/remotes/origin/" + sourceBranch))
                        .call();

                System.out.println("No conflicts found for merging " + sourceBranch + " into " + destinationBranch);
            }
        } catch (GitAPIException | IOException e) {
            if (e.getMessage().contains("conflict")) {
                System.out.println("Conflicts detected while merging " + sourceBranch + " into " + destinationBranch);
            } else {
                e.printStackTrace();
            }
        }
    }
}
