package io.github.bhhan.server.integration.base;

import io.github.bhhan.server.service.common.dto.TimeRange;
import io.github.bhhan.server.service.project.dto.ProjectDto;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class Fixture {
    public static SkillDto.SkillFormReq makeSkillFormReq(String name, String description, String type, String use, String fileName) {
        return SkillDto.SkillFormReq
                .builder()
                .name(name)
                .description(description)
                .type(type)
                .use(use)
                .file(makeMockMultipartFile(fileName))
                .build();
    }

    public static ProjectDto.ProjectFormReq makeProjectFormReq(String summary, String description, Long member,
                                                               String title, TimeRange timeRange,
                                                               List<MultipartFile> files,
                                                               List<Long> skills){
        return ProjectDto.ProjectFormReq
                .builder()
                .summary(summary)
                .description(description)
                .member(member)
                .title(title)
                .timeRange(timeRange)
                .files(files)
                .skills(skills)
                .build();
    }

    public static MockMultipartFile makeMockMultipartFile(String fileName) {
        return new MockMultipartFile(fileName, fileName, MediaType.IMAGE_GIF_VALUE, new byte[]{});
    }
}
