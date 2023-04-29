package com.tuna;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.bridge.MavenRepositorySystem;
import org.apache.maven.settings.Mirror;
import org.codehaus.plexus.component.annotations.Component;
import org.eclipse.aether.RepositorySystemSession;

import java.util.List;

@Component(role = MavenRepositorySystem.class)
public class MirrorFallbackMavenRepositorySystem extends MavenRepositorySystem {
    @Override
    public void injectMirror(List<ArtifactRepository> repos, List<Mirror> mirrors) {
        super.injectMirror(repos, mirrors);
        restoreRepos(repos);
    }

    @Override
    public void injectMirror(RepositorySystemSession session, List<ArtifactRepository> repos) {
        super.injectMirror(session, repos);
        restoreRepos(repos);
    }

    private void restoreRepos(List<ArtifactRepository> repos) {
        for (int i = 0; i < repos.size(); i++) {
            ArtifactRepository mirror = repos.get(i);
            for (ArtifactRepository orig : mirror.getMirroredRepositories()) {
                repos.add(i + 1, orig); // try original(s) right after mirror
            }
        }
    }
}
