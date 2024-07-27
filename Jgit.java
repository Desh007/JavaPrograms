import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;

public class BitbucketRepoCommits {

    public static void main(String[] args) {
        String remoteUrl = "https://your-bitbucket-username:your-app-password@bitbucket.org/your-repo-owner/your-repo-slug.git";
        File localPath = new File("/path/to/local/repo");

        try {
            // Clone the repository
            System.out.println("Cloning repository from " + remoteUrl + " to " + localPath);
            Git.cloneRepository()
                .setURI(remoteUrl)
                .setDirectory(localPath)
                .call();

            // Open the cloned repository
            Repository repository = new RepositoryBuilder()
                .setGitDir(new File(localPath, ".git"))
                .build();

            // Fetch and print commit details
            fetchCommitDetails(repository);

            // Close the repository
            repository.close();
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void fetchCommitDetails(Repository repository) {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> commits = git.log().call();
            for (RevCommit commit : commits) {
                System.out.println("Commit: " + commit.getName());
                System.out.println("Author: " + commit.getAuthorIdent().getName());
                System.out.println("Message: " + commit.getFullMessage());
                System.out.println("Date: " + commit.getAuthorIdent().getWhen());
                System.out.println("------------------------------------------------");
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
