package com.tuna;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.bridge.MavenRepositorySystem;
import org.apache.maven.settings.Mirror;
import org.codehaus.plexus.component.annotations.Component;

import java.util.List;

@Component(role = MavenRepositorySystem.class)
public class MirrorFallbackMavenRepositorySystem extends MavenRepositorySystem {
    @Override
    public void injectMirror(List<ArtifactRepository> repos, List<Mirror> mirrors) {
        super.injectMirror(repos, mirrors);
        for (int i = 0; i < repos.size(); i++) {
            ArtifactRepository mirror = repos.get(i);
            for (ArtifactRepository orig : mirror.getMirroredRepositories()) {
                repos.add(i + 1, orig); // try original(s) right after mirror
            }
        }
    }
}
