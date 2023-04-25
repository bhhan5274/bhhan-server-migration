package io.github.bhhan.server.service.project.dto;

import io.github.bhhan.server.service.common.dto.TimeRange;
import io.github.bhhan.server.service.skill.dto.SkillDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectDto {
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectFormReq {
        @NotBlank(message = "공백을 허용하지 않습니다.")
        @Length(max = 80, message = "80자를 넘길 수 없습니다.")
        private String title;

        @NotNull(message = "인원은 최소 1이상 입니다.")
        @Min(value = 1, message = "최소값은 1입니다.")
        @Max(value = 20, message = "최대값은 20입니다.")
        private Long member;

        @NotBlank(message = "공백을 허용하지 않습니다.")
        private String description;

        @NotBlank(message = "공백을 허용하지 않습니다.")
        private String summary;

        @NotNull(message = "기간을 입력하세요.")
        @io.github.bhhan.server.service.common.validator.TimeRange(message = "기간을 다시 설정하세요.")
        private TimeRange timeRange;

        @NotNull(message = "스킬은 최소 1개 이상입니다.")
        @Size(min = 1, message = "스킬은 최소 1개 이상입니다.")
        private List<Long> skills = new ArrayList<>();

        @NotNull(message = "이미지는 최소 1개 이상입니다.")
        @Size(min = 1, message = "이미지는 최소 1개 이상입니다.")
        private List<MultipartFile> files = new ArrayList<>();

        @Builder
        public ProjectFormReq(String title, Long member, String description, String summary, TimeRange timeRange, List<Long> skills, List<MultipartFile> files){
            this.title = title;
            this.member = member;
            this.description = description;
            this.timeRange = timeRange;
            this.summary = summary;

            if(Objects.nonNull(skills)){
                this.skills.addAll(skills);
            }

            if(Objects.nonNull(files)){
                this.files.addAll(files);
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectPagingRes {
        private int totalPages;
        private long totalElements;
        private int size;
        private int number;
        private boolean last;
        private boolean first;
        private boolean empty;
        private List<ProjectRes> content = new ArrayList<>();

        @Builder
        public ProjectPagingRes(long totalElements, int totalPages, int size, int number, boolean last, boolean first, boolean empty, List<ProjectRes> content){
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.size = size;
            this.number = number;
            this.last = last;
            this.first = first;
            this.empty = empty;
            this.content.addAll(content);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectRes {
        private Long id;
        private String title;
        private Long member;
        private String description;
        private String summary;
        private TimeRange timeRange;
        private List<SkillDto.SkillRes> skills = new ArrayList<>();
        private List<ImageRes> images = new ArrayList<>();

        @Builder
        public ProjectRes(Long id, String title, Long member, String description, String summary, String startDate, String endDate, List<SkillDto.SkillRes> skills, List<ImageRes> images){
            this.id = id;
            this.title = title;
            this.member = member;
            this.description = description;
            this.summary = summary;
            this.timeRange = TimeRange.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            this.skills.addAll(skills);
            this.images.addAll(images);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProjectViewRes {
        private Long id;
        private String title;
        private Long member;
        private String description;
        private String summary;
        private TimeRange timeRange;
        private List<SkillDto.SkillRes> skills = new ArrayList<>();
        private List<ImageRes> images = new ArrayList<>();
        private Long prevId;
        private Long nextId;

        @Builder
        public ProjectViewRes(Long id, String title, Long member, String description, String summary, String startDate, String endDate, List<SkillDto.SkillRes> skills, List<ImageRes> images,
                              Long prevId, Long nextId){
            this.id = id;
            this.title = title;
            this.member = member;
            this.description = description;
            this.summary = summary;
            this.timeRange = TimeRange.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            this.skills.addAll(skills);
            this.images.addAll(images);
            this.prevId = prevId;
            this.nextId = nextId;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ImageRes {
        private Long id;
        private String path;

        @Builder
        public ImageRes(Long id, String path){
            this.id = id;
            this.path = path;
        }
    }
}
