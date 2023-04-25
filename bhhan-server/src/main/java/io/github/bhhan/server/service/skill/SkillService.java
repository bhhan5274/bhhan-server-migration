package io.github.bhhan.server.service.skill;

import io.github.bhhan.server.domain.skill.Skill;
import io.github.bhhan.server.domain.skill.SkillRepository;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import io.github.bhhan.server.service.skill.exception.SkillException;
import io.github.bhhan.storage.api.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class SkillService {
    private final SkillRepository skillRepository;
    private final StorageService storageService;

    @CacheEvict(value = "skills", allEntries = true)
    public SkillDto.SkillRes addSkill(SkillDto.SkillFormReq skillFormReq){
        try {
            String path = storageService.store(skillFormReq.getFile());

            Skill savedSkill = skillRepository.save(Skill.builder()
                    .name(skillFormReq.getName())
                    .use(Skill.Use.valueOf(skillFormReq.getUse().toUpperCase()))
                    .type(Skill.Type.valueOf(skillFormReq.getType().toUpperCase()))
                    .path(path)
                    .description(skillFormReq.getDescription())
                    .build());

            return toSkillRes(savedSkill);

        }catch(Exception e){
            throw new SkillException("스킬 추가에 실패했습니다.");
        }
    }

    @Cacheable(value = "skills")
    public List<SkillDto.SkillRes> getSkills(){
        try {
            List<Skill> skills = skillRepository.findAllByOrderByIdAsc();

            return skills.stream()
                    .map(this::toSkillRes)
                    .collect(Collectors.toList());

        }catch(Exception e){
            throw new SkillException("스킬 불러오기에 실패했습니다.");
        }
    }

    @Cacheable(value = "skill", key = "#skillId")
    public SkillDto.SkillRes getSkill(Long skillId){
        try {
            Skill findSkill = findSkill(skillId);

            return toSkillRes(findSkill);

        }catch(Exception e){
            throw new SkillException("스킬 불러오기에 실패했습니다.");
        }
    }

    @Caching(evict = {@CacheEvict(value = "skill", key = "#skillId"), @CacheEvict(value = "skills", allEntries = true)})
    public void removeSkill(Long skillId){
        try{
            skillRepository.deleteById(skillId);
        }catch(Exception e){
            throw new SkillException("스킬 삭제에 실패했습니다.");
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "skill", allEntries = true),
            @CacheEvict(value = "skills", allEntries = true)
    })
    public void removeSkills(){
        try{
            skillRepository.deleteAll();
        }catch(Exception e){
            throw new SkillException("스킬 삭제에 실패했습니다.");
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "project", allEntries = true),
                    @CacheEvict(value = "paging", allEntries = true),
                    @CacheEvict(value = "skills", allEntries = true)
            },
            put = {
                    @CachePut(value = "skill", key = "#skillId")
            }
    )
    public SkillDto.SkillRes updateSkill(Long skillId, SkillDto.SkillFormReq skillUpdateFormReq){
        try {
            String path = storageService.store(skillUpdateFormReq.getFile());
            Skill findSkill = findSkill(skillId);

            findSkill.updateDescription(skillUpdateFormReq.getDescription());
            findSkill.updateName(skillUpdateFormReq.getName());
            findSkill.updatePath(path);
            findSkill.updateType(Skill.Type.valueOf(skillUpdateFormReq.getType().toUpperCase()));
            findSkill.updateUse(Skill.Use.valueOf(skillUpdateFormReq.getUse().toUpperCase()));

            return toSkillRes(findSkill);

        }catch(Exception e){
            throw new SkillException("스킬 업데이트에 실패했습니다.");
        }
    }

    private Skill findSkill(Long skillId) {
        return skillRepository.findById(skillId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private SkillDto.SkillRes toSkillRes(Skill skill){
        return SkillDto.SkillRes.builder()
                .id(skill.getId())
                .name(skill.getName())
                .path(skill.getPath())
                .description(skill.getDescription())
                .type(skill.getType().name())
                .use(skill.getUse().name())
                .build();
    }
}
