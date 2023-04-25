package io.github.bhhan.server.service.skill.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SkillDto {

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SkillFormReq {

        @NotBlank(message = "공백을 허용하지 않습니다.")
        private String name;

        @NotNull(message = "이미지를 추가하세요.")
        private MultipartFile file;

        @NotBlank(message = "USE를 설정하세요.")
        private String use;

        @NotBlank(message = "TYPE을 설정하세요.")
        private String type;

        @NotBlank(message = "공백을 허용하지 않습니다.")
        private String description;

        @Builder
        public SkillFormReq(String name, MultipartFile file, String use, String type, String description){
            this.name = name;
            this.file = file;
            this.use = use;
            this.type = type;
            this.description = description;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SkillRes {
        private Long id;
        private String name;
        private String use;
        private String type;
        private String description;
        private String path;

        @Builder
        public SkillRes(Long id, String name, String use, String type, String description, String path){
            this.id = id;
            this.name = name;
            this.use = use;
            this.type = type;
            this.description = description;
            this.path = path;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SkillSelectedRes {
        private Long id;
        private String name;
        private String path;
        private String use;
        private String type;
        private boolean selected = false;
        private String description;

        public SkillSelectedRes(SkillRes skillRes){
            this.id = skillRes.getId();
            this.name = skillRes.getName();
            this.path = skillRes.getPath();
            this.use = skillRes.getUse();
            this.type = skillRes.getType();
            this.description = skillRes.getDescription();
        }

        public SkillSelectedRes select(){
            this.selected = true;
            return this;
        }
    }
}
