## Purpose

As [complained here](https://issues.apache.org/jira/browse/MNG-714), by default when a repository is covered by a mirror, Maven does not check the original if the mirror is not accessible or lacks a certain artifact. This extension changes above behavior for Maven 3.x.

(For Maven 2.x, check out above link for a possible patch.)


## How it works

When building the list of `remoteRepositories` to be used for the project model, Maven checks enabled repos against known mirrors, and replaces all matched repos with "mirror" repo objects - each with a `mirrorOf` attribute pointing to the list of "real" repos that it is mirroring. This list is then used across the project for artifact/metadata resolution etc, in a fallback fashion (check first repo, check next repo if failed/not found, ...).

This module contains a custom implementation/override of `org.apache.maven.bridge.MavenRepositorySystem` (which, among other things, does the above repo-mirror correlation); this injects, after each mirror in `remoteRepositories`, the list of corresponding "real" repos that it covers.

If the default build would have resulted in `remoteRepositories`:

`mirror-1 (of repo-1, repo-2), repo-3, mirror-2 (of repo-4, repo-5)`

The extension will change it to be:

`mirror-1 (of repo-1, repo-2), repo-1, repo-2, repo-3, mirror-2 (of repo-4, repo-5), repo-4, repo-5`

This way, if a mirror fails during resolution, Maven will check its "real" repos (which happen to come immediately after it, on the list).


## Usage

1. Update `mvn.version`, `mvn.resolver.version` and `plexus.version` in `pom.xml`, based on JAR versions available in your `$MAVEN_HOME/lib`
2. Build the project.
3. Place resulting JAR into `$MAVEN_HOME/lib/ext/`


## Disclaimer

* Tested only with Maven 3.6.1, with local (file://) mirrors so far
* Any other behavioral changes due to mixing of mirrors with repos, have not been checked/documented.