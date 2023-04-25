package io.github.bhhan.server;

import io.github.bhhan.server.domain.project.ProjectRepository;
import io.github.bhhan.server.domain.skill.SkillRepository;
import io.github.bhhan.server.service.project.ProjectService;
import io.github.bhhan.server.service.skill.SkillService;
import io.github.bhhan.storage.api.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaAuditing
@EnableJpaRepositories
@RequiredArgsConstructor
public class ServerConfig {
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final StorageService storageService;

    @Bean
    public SkillService skillService(){
        return new SkillService(skillRepository, storageService);
    }

    @Bean
    public ProjectService projectService(){
        return new ProjectService(projectRepository, skillRepository, storageService);
    }
}
