package io.github.bhhan.server.service.skill;

import io.github.bhhan.server.ServerConfig;
import io.github.bhhan.server.config.cache.CacheConfiguration;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import io.github.bhhan.server.service.skill.exception.SkillException;
import io.github.bhhan.storage.api.StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {ServerConfig.class, SkillServiceTest.Config.class, CacheConfiguration.class})
class SkillServiceTest {

    private static final String MOCK_FILE_NAME = "mockFile.txt";

    @Configuration
    static class Config {
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
    private SkillService skillService;

    @Test
    @DisplayName("skillService cacheTest1")
    void test1(){
        skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Java", "Java"));
        skillService.getSkills();
        skillService.getSkills();

        skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Typescript", "Typescript"));
        skillService.getSkills();
    }

    @Test
    @DisplayName("skillService cacheTest2")
    void test2(){
        final Long id1 = skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Kotlin", "Kotlin")).getId();
        final Long id2 = skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Javascript", "Javascript")).getId();

        skillService.getSkill(id1);
        skillService.getSkill(id1);
        skillService.getSkill(id2);
        skillService.getSkill(id2);
    }

    @Test
    @DisplayName("skillService cacheTest3")
    void test3(){
        final Long id1 = skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "C#", "C#")).getId();
        final Long id2 = skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Java", "Java")).getId();

        skillService.getSkills();
        skillService.getSkill(id1);
        skillService.getSkills();
        skillService.removeSkill(id1);
        skillService.getSkills();
        skillService.getSkill(id2);

        assertThrows(SkillException.class, () -> {
            skillService.getSkill(id1);
        });
    }

    @Test
    @DisplayName("skillService cacheTest4")
    void test4(){
        final Long id1 = skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Java", "Java")).getId();
        skillService.addSkill(makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Python", "Python"));

        skillService.getSkill(id1);
        skillService.getSkill(id1);
        skillService.getSkills();
        skillService.getSkills();
        skillService.removeSkills();
        skillService.getSkills();

        assertThrows(SkillException.class, () -> {
            skillService.getSkill(id1);
        });
    }

    @Test
    @DisplayName("skillService cacheTest5")
    void test5(){
        final SkillDto.SkillFormReq skillFormReq = makeSkillFormReq(MOCK_FILE_NAME, "WORK", "BACKEND", "Python", "Python");
        final Long id1 = skillService.addSkill(skillFormReq).getId();

        skillService.getSkill(id1);
        skillService.getSkill(id1);
        skillService.getSkills();
        skillService.getSkills();
        skillService.updateSkill(id1, skillFormReq);
        skillService.getSkills();
        skillService.getSkill(id1);
    }

    private SkillDto.SkillFormReq makeSkillFormReq(String fileName, String use, String type, String description, String name){
        return SkillDto.SkillFormReq.builder()
                .file(new MockMultipartFile(fileName, fileName, "", new byte[]{}))
                .use(use)
                .type(type)
                .description(description)
                .name(name)
                .build();
    }
}
