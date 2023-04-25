package io.github.bhhan.server.web.api.skill;

import io.github.bhhan.server.service.skill.SkillService;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import io.github.bhhan.server.service.skill.exception.SkillException;
import io.github.bhhan.server.web.api.common.error.ErrorCode;
import io.github.bhhan.server.web.api.common.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/skills")
public class SkillApi {
    private final SkillService skillService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillDto.SkillRes addSkill(@Valid SkillDto.SkillFormReq skillFormReq){
        return skillService.addSkill(skillFormReq);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SkillDto.SkillRes> getSkills(){
        return skillService.getSkills();
    }

    @GetMapping("/{skillId}")
    @ResponseStatus(HttpStatus.OK)
    public SkillDto.SkillRes getSkill(@PathVariable Long skillId){
        return skillService.getSkill(skillId);
    }

    @DeleteMapping("/{skillId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSkill(@PathVariable Long skillId){
        skillService.removeSkill(skillId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteSkills(){
        skillService.removeSkills();
    }

    @PutMapping("/{skillId}")
    @ResponseStatus(HttpStatus.OK)
    public SkillDto.SkillRes updateSkill(@PathVariable Long skillId, @Valid SkillDto.SkillFormReq skillUpdateFormReq){
        return skillService.updateSkill(skillId, skillUpdateFormReq);
    }

    @ExceptionHandler(SkillException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSkillException(SkillException e){
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR)
                .setMessage(e.getMessage());
    }
}
