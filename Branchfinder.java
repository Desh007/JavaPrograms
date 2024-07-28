import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BranchCreationFinder {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: BranchCreationFinder <repoPath> <branchName>");
            System.exit(1);
        }

        String repoPath = args[0];
        String branchName = args[1];

        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            try (Git git = new Git(builder.setGitDir(new File(repoPath, ".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build())) {

                Ref branchRef = git.getRepository().findRef(branchName);
                if (branchRef == null) {
                    System.out.println("Branch " + branchName + " not found");
                    return;
                }

                RevWalk walk = new RevWalk(git.getRepository());
                RevCommit branchCommit = walk.parseCommit(branchRef.getObjectId());

                Map<ObjectId, String> branchCommits = new HashMap<>();
                for (Ref ref : git.branchList().call()) {
                    String currentBranch = ref.getName();
                    Iterable<RevCommit> commits = git.log().add(ref.getObjectId()).call();
                    for (RevCommit commit : commits) {
                        branchCommits.put(commit.getId(), currentBranch);
                    }
                }

                RevCommit parentCommit = branchCommit.getParent(0);
                while (parentCommit != null) {
                    String parentBranch = branchCommits.get(parentCommit.getId());
                    if (parentBranch != null) {
                        System.out.println("Branch " + branchName + " was created from " + parentBranch);
                        return;
                    }
                    parentCommit = parentCommit.getParentCount() > 0 ? parentCommit.getParent(0) : null;
                }

                System.out.println("Unable to determine the parent branch of " + branchName);

            }
        } catch (IOException | org.eclipse.jgit.api.errors.GitAPIException e) {
            e.printStackTrace();
        }
    }
}
