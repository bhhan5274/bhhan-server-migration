package io.github.bhhan.server.integration.base;

import io.github.bhhan.server.ServerConfig;
import io.github.bhhan.server.config.cache.CacheConfiguration;
import io.github.bhhan.server.domain.project.ProjectRepository;
import io.github.bhhan.server.domain.skill.SkillRepository;
import io.github.bhhan.server.service.project.ProjectService;
import io.github.bhhan.server.service.skill.SkillService;
import io.github.bhhan.storage.api.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseServiceTest {
    @Configuration
    @Import({ServerConfig.class, CacheConfiguration.class})
    public static class Config {
        @Bean
        public StorageService mockStorageService(){
            return new StorageService() {
                @Override
                public String store(MultipartFile file) {
                    return file.getOriginalFilename();
                }

                @Override
                public List<String> storeAll(List<MultipartFile> files) {
                    return files.stream()
                            .map(MultipartFile::getOriginalFilename)
                            .collect(Collectors.toList());
                }

                @Override
                public void delete(String fileName) {
                }

                @Override
                public void delete(List<String> fileNames) {
                }
            };
        }
    }

    @Autowired
    protected SkillService skillService;

    @Autowired
    protected SkillRepository skillRepository;

    @Autowired
    protected ProjectService projectService;

    @Autowired
    protected ProjectRepository projectRepository;
}
