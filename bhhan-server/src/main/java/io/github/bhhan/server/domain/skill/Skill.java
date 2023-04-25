package io.github.bhhan.server.domain.skill;

import io.github.bhhan.server.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "SKILLS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Skill extends BaseEntity {
    public enum Use {
        WORK, TOY, STUDY
    }

    public enum Type {
        APPLICATION, FRONTEND, BACKEND, DEVOPS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKILL_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "SKILL_USE")
    private Use use;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Builder
    public Skill(Long id, String name, String path, Use use, Type type, String description){
        this.id = id;
        this.name = name;
        this.path = path;
        this.use = use;
        this.type = type;
        this.description = description;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateDescription(String description){
        this.description = description;
    }

    public void updateUse(Use use){
        this.use = use;
    }

    public void updateType(Type type){
        this.type = type;
    }

    public void updatePath(String path){
        this.path = path;
    }
}
