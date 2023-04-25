package io.github.bhhan.server.integration.project;

import io.github.bhhan.server.domain.skill.Skill;
import io.github.bhhan.server.integration.base.BaseServiceTest;
import io.github.bhhan.server.service.common.dto.Pageable;
import io.github.bhhan.server.service.common.dto.TimeRange;
import io.github.bhhan.server.service.project.dto.ProjectDto;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.bhhan.server.integration.base.Fixture.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BaseServiceTest.Config.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectServiceTest extends BaseServiceTest {

    private static final List<Long> skills = new ArrayList<>();
    private static final String DESCRIPTION = "description";
    private static final String SUMMARY = "summary";
    private static final Long MEMBER = 1L;
    private static final String TITLE = "title";
    private static final String START_DATE = "2021-01-01";
    private static final String END_DATE = "2021-02-01";
    private static final String PROJECT_FILE_1 = "projectFile1";
    private static final String PROJECT_FILE_2 = "projectFile2";
    private static final String PROJECT_FILE_3 = "projectFile3";
    private static final String SKILL_1 = "SKILL1";
    private static final String SKILL_2 = "SKILL2";
    private static final String SKILL_3 = "SKILL3";

    private static final String UPDATE_SUMMARY = "update_summary";
    private static final String UPDATE_DESCRIPTION = "update_description";
    private static final Long UPDATE_MEMBER = 3L;
    private static final String UPDATE_TITLE = "update_title";
    private static final String UPDATE_START_DATE = "2021-07-01";
    private static final String UPDATE_END_DATE = "2021-10-01";
    private static final String UPDATE_PROJECT_FILE_1 = "update_projectFile1";
    private static final String UPDATE_PROJECT_FILE_2 = "update_projectFile2";

    private static Long PROJECT_ID;
    private static final int PROJECT_COUNT = 10;
    private static final int TOTAL_PAGE = 2;
    private static final int SEARCH_PAGE = 1;
    private static final int SEARCH_SIZE = 5;

    @Test
    @DisplayName("skill and dummy project add")
    @Order(1)
    void addSkills(){
        skills.add(skillService.addSkill(makeSkillFormReq(SKILL_1, "DESC1", Skill.Type.BACKEND.name(), Skill.Use.WORK.name(), "file1")).getId());
        skills.add(skillService.addSkill(makeSkillFormReq(SKILL_2, "DESC2", Skill.Type.DEVOPS.name(), Skill.Use.TOY.name(), "file2")).getId());
        skills.add(skillService.addSkill(makeSkillFormReq(SKILL_3, "DESC3", Skill.Type.APPLICATION.name(), Skill.Use.STUDY.name(), "file3")).getId());

        for(int i = 1; i <= PROJECT_COUNT - 1; i++){
            projectService.addProject(makeProjectFormReq(SUMMARY + i,DESCRIPTION + i, MEMBER, TITLE + i, TimeRange.builder().startDate(START_DATE).endDate(END_DATE).build(),
                    Arrays.asList(makeMockMultipartFile(PROJECT_FILE_1),
                            makeMockMultipartFile(PROJECT_FILE_2),
                            makeMockMultipartFile(PROJECT_FILE_3)),
                    Collections.singletonList(skills.get(i % 3))));
        }
    }

    @Test
    @DisplayName("addProject")
    @Order(2)
    void addProject(){
        TimeRange timeRange = TimeRange.builder().startDate(START_DATE).endDate(END_DATE).build();

        ProjectDto.ProjectFormReq projectFormReq = makeProjectFormReq(SUMMARY, DESCRIPTION, MEMBER, TITLE, timeRange,
                Arrays.asList(makeMockMultipartFile(PROJECT_FILE_1),
                        makeMockMultipartFile(PROJECT_FILE_2),
                        makeMockMultipartFile(PROJECT_FILE_3)),
                skills);

        ProjectDto.ProjectRes projectRes = projectService.addProject(projectFormReq);
        PROJECT_ID = projectRes.getId();

        assertProject(projectRes, PROJECT_ID, TITLE, MEMBER, DESCRIPTION, SUMMARY,
                Arrays.asList(PROJECT_FILE_1, PROJECT_FILE_2, PROJECT_FILE_3),
                skills, timeRange);
    }

    @Test
    @DisplayName("getProject")
    @Order(3)
    void getProject(){
        ProjectDto.ProjectRes projectRes = projectService.getProject(PROJECT_ID);

        assertProject(projectRes, PROJECT_ID, TITLE, MEMBER, DESCRIPTION, SUMMARY,
                Arrays.asList(PROJECT_FILE_1, PROJECT_FILE_2, PROJECT_FILE_3),
                skills, TimeRange.builder().startDate(START_DATE).endDate(END_DATE).build());
    }

    @Test
    @DisplayName("getProjects")
    @Order(4)
    void getProjects(){
        Pageable pageable = Pageable.builder()
                .page(SEARCH_PAGE)
                .size(SEARCH_SIZE)
                .build();

        ProjectDto.ProjectPagingRes projectPagingRes = projectService.getProjects(pageable.of());

        assertEquals(TOTAL_PAGE, projectPagingRes.getTotalPages());
        assertEquals(PROJECT_COUNT, projectPagingRes.getTotalElements());
        assertEquals(SEARCH_SIZE, projectPagingRes.getSize());
        assertEquals(SEARCH_PAGE - 1, projectPagingRes.getNumber());
        assertFalse(projectPagingRes.isEmpty());
        assertTrue(projectPagingRes.isFirst());
        assertFalse(projectPagingRes.isLast());
        assertEquals(SEARCH_SIZE, projectPagingRes.getContent().size());
    }

    @Test
    @DisplayName("getProjectView")
    @Order(5)
    void getProjectView(){
        ProjectDto.ProjectViewRes projectViewRes = projectService.getProjectView(PROJECT_ID);

        assertEquals(DESCRIPTION, projectViewRes.getDescription());
        assertEquals(TITLE, projectViewRes.getTitle());
        assertEquals(START_DATE, projectViewRes.getTimeRange().getStartDate());
        assertEquals(END_DATE, projectViewRes.getTimeRange().getEndDate());
        assertEquals(PROJECT_ID, projectViewRes.getId());
        assertEquals(MEMBER, projectViewRes.getMember());
        assertImages(Arrays.asList(PROJECT_FILE_1, PROJECT_FILE_2, PROJECT_FILE_3), projectViewRes.getImages());
        assertSkills(skills, projectViewRes.getSkills());
        assertEquals( PROJECT_ID - 1, projectViewRes.getNextId());
        assertEquals(0, projectViewRes.getPrevId());
    }

    @Test
    @DisplayName("updateProject")
    @Order(6)
    void updateProject(){
        skills.remove(0);
        TimeRange timeRange = TimeRange.builder().startDate(UPDATE_START_DATE).endDate(UPDATE_END_DATE).build();

        ProjectDto.ProjectRes projectRes = projectService.updateProject(PROJECT_ID, makeProjectFormReq(UPDATE_SUMMARY, UPDATE_DESCRIPTION, UPDATE_MEMBER,
                UPDATE_TITLE, timeRange, Arrays.asList(makeMockMultipartFile(UPDATE_PROJECT_FILE_1),
                        makeMockMultipartFile(UPDATE_PROJECT_FILE_2)), skills));

        assertProject(projectRes, PROJECT_ID, UPDATE_TITLE, UPDATE_MEMBER, UPDATE_DESCRIPTION, UPDATE_SUMMARY,
                Arrays.asList(UPDATE_PROJECT_FILE_1, UPDATE_PROJECT_FILE_2),
                skills, timeRange);
    }

    @Test
    @DisplayName("deleteProject")
    @Order(7)
    void deleteProject(){
        projectService.removeProject(PROJECT_ID);

        assertEquals(PROJECT_COUNT - 1, projectRepository.findAll().size());
    }

    @Test
    @DisplayName("deleteProjects")
    @Order(8)
    void deleteProjects(){
        projectService.removeProjects();

        assertEquals(0L, projectRepository.findAll().size());
    }

    @Test
    @DisplayName("clear")
    @Order(9)
    void clear(){
        projectRepository.deleteAll();
        skillRepository.deleteAll();
    }

    @SuppressWarnings("unchecked")
    private void assertProject(ProjectDto.ProjectRes projectRes, Object... properties) {
        assertEquals(properties[0], projectRes.getId());
        assertEquals(properties[1], projectRes.getTitle());
        assertEquals(properties[2], projectRes.getMember());
        assertEquals(properties[3], projectRes.getDescription());
        assertEquals(properties[4], projectRes.getSummary());

        assertImages((List<String>) properties[5], projectRes.getImages());
        assertSkills((List<Long>) properties[6], projectRes.getSkills());
        assertEquals(properties[7], projectRes.getTimeRange());
    }

    private void assertImages(List<String> images, List<ProjectDto.ImageRes> imageRes) {
        for (ProjectDto.ImageRes image : imageRes) {
            assertTrue(images.contains(image.getPath()));
        }
    }

    private void assertSkills(List<Long> skills, List<SkillDto.SkillRes> skillsRes) {
        for (SkillDto.SkillRes skillRes : skillsRes) {
            assertTrue(skills.stream()
                    .anyMatch(skill -> skill.equals(skillRes.getId())));
        }
    }
}
