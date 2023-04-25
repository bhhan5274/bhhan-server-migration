package io.github.bhhan.server.domain.project;

import io.github.bhhan.server.domain.common.BaseEntity;
import io.github.bhhan.server.domain.common.Period;
import io.github.bhhan.server.domain.skill.Skill;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROJECTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(length = 80, nullable = false)
    private String title;

    @Column(nullable = false)
    private Long member;

    @Column(nullable = false)
    @Lob
    private String summary;

    @Column(nullable = false)
    @Lob
    private String description;

    @Embedded
    private Period period;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PROJECT_ID")
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "PROJECT_SKILLS", joinColumns = @JoinColumn(name = "PROJECT_ID"), inverseJoinColumns = @JoinColumn(name = "SKILL_ID"))
    private List<Skill> skills = new ArrayList<>();

    @Builder
    public Project(Long id, String title, Long member, String summary, String description, Period period, List<Skill> skills, List<Image> images){
        this.id = id;
        this.title = title;
        this.member = member;
        this.summary = summary;
        this.description = description;
        this.period = period;
        this.skills.addAll(skills);
        this.images.addAll(images);
    }

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateMember(Long member){
        this.member = member;
    }

    public void updateSummary(String summary){
        this.summary = summary;
    }

    public void updateDescription(String description){
        this.description = description;
    }

    public void updatePeriod(Period period){
        this.period = period;
    }

    public void addImage(Image image){
        this.images.add(image);
    }

    public void updateImages(List<Image> images){
        this.images.clear();
        this.images.addAll(images);
    }

    public boolean removeImage(Long imageId){
        return images.removeIf(image -> image.getId().equals(imageId));
    }

    public void updateSkills(List<Skill> skills){
        this.skills.clear();
        this.skills.addAll(skills);
    }
}
