package io.github.bhhan.server.integration.skill;

import io.github.bhhan.server.domain.skill.Skill;
import io.github.bhhan.server.integration.base.BaseServiceTest;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.github.bhhan.server.integration.base.Fixture.makeSkillFormReq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BaseServiceTest.Config.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SkillServiceTest extends BaseServiceTest {

    private static final String NAME = "bhhan";
    private static final String DESCRIPTION = "description";
    private static final String TYPE = Skill.Type.BACKEND.name();
    private static final String USE = Skill.Use.WORK.name();
    private static final String FILE_NAME = "mockfile";

    private static final String UPDATE_NAME = "update bhhan";
    private static final String UPDATE_DESCRIPTION = "update description";
    private static final String UPDATE_TYPE = Skill.Type.APPLICATION.name();
    private static final String UPDATE_USE = Skill.Use.STUDY.name();
    private static final String UPDATE_FILE_NAME = "update mockfile";

    private static Long SKILL_ID;

    @Test
    @DisplayName("addSkill")
    @Order(1)
    void addSkill() {
        SkillDto.SkillFormReq skillFormReq = makeSkillFormReq(NAME, DESCRIPTION, TYPE, USE, FILE_NAME);

        SkillDto.SkillRes skillRes = skillService.addSkill(skillFormReq);
        SKILL_ID = skillRes.getId();

        assertSkill(skillRes, NAME, DESCRIPTION, FILE_NAME, TYPE, USE);
    }

    @Test
    @DisplayName("getSkills")
    @Order(2)
    void getSkills(){
        List<SkillDto.SkillRes> skills = skillService.getSkills();

        assertEquals(1, skills.size());
        SkillDto.SkillRes skillRes = skills.get(0);
        assertSkill(skillRes, NAME, DESCRIPTION, FILE_NAME, TYPE, USE);
    }

    @Test
    @DisplayName("getSkill")
    @Order(3)
    void getSkill(){
        SkillDto.SkillRes skillRes = skillService.getSkill(SKILL_ID);

        assertSkill(skillRes, NAME, DESCRIPTION, FILE_NAME, TYPE, USE);
    }

    @Test
    @DisplayName("updateSkill")
    @Order(4)
    void updateSkill(){
        SkillDto.SkillFormReq updateSkillFormReq = makeSkillFormReq(UPDATE_NAME, UPDATE_DESCRIPTION, UPDATE_TYPE, UPDATE_USE, UPDATE_FILE_NAME);

        SkillDto.SkillRes skillRes = skillService.updateSkill(SKILL_ID, updateSkillFormReq);

        assertSkill(skillRes, UPDATE_NAME, UPDATE_DESCRIPTION, UPDATE_FILE_NAME, UPDATE_TYPE, UPDATE_USE);
    }

    @Test
    @DisplayName("deleteSkill")
    @Order(5)
    void deleteSkill(){
        int size = skillService.getSkills()
                .size();

        assertEquals(1, size);

        skillService.removeSkill(SKILL_ID);

        assertEquals(0, skillService.getSkills()
                .size());
    }

    @Test
    @DisplayName("clear")
    @Order(6)
    void clear(){
        skillService.removeSkills();
    }

    private void assertSkill(SkillDto.SkillRes skillRes, String... properties) {
        assertEquals(properties[0], skillRes.getName());
        assertEquals(properties[1], skillRes.getDescription());
        assertEquals(properties[2], skillRes.getPath());
        assertEquals(properties[3], skillRes.getType());
        assertEquals(properties[4], skillRes.getUse());
        assertNotNull(skillRes.getId());
    }
}
