import com.atlassian.bitbucket.commit.Commit;
import com.atlassian.bitbucket.commit.CommitService;
import com.atlassian.bitbucket.commit.CommitsBetweenRequest;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.user.ApplicationUser;
import com.atlassian.sal.api.component.ComponentLocator;

import java.util.List;

public class BitbucketClient {

    public static void main(String[] args) {
        // Fetch the repository service
        RepositoryService repositoryService = ComponentLocator.getComponent(RepositoryService.class);

        // Fetch the commit service
        CommitService commitService = ComponentLocator.getComponent(CommitService.class);

        // Define the repository you are working with
        String projectKey = "PROJECT_KEY";
        String repoSlug = "repo-slug";

        // Fetch the repository
        Repository repository = repositoryService.getBySlug(projectKey, repoSlug);

        // Define the commits between request
        CommitsBetweenRequest commitsBetweenRequest = new CommitsBetweenRequest.Builder(repository)
                .include("refs/heads/master") // Include commits on the master branch
                .build();

        // Fetch commits
        List<Commit> commits = commitService.getCommitsBetween(commitsBetweenRequest).getValues();

        // Print commit details
        for (Commit commit : commits) {
            System.out.println("Commit ID: " + commit.getId());
            System.out.println("Author: " + commit.getAuthor().getName());
            System.out.println("Message: " + commit.getMessage());
            System.out.println("Date: " + commit.getAuthorTimestamp());
            System.out.println("------------------------------------------------");
        }
    }
}
