package io.github.bhhan.server.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ROLES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role {
    public static final String ADMIN = "ADMIN";
    private static final String ROLE = "ROLE_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    private String name;

    @Builder
    public Role(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public String getRole(){
        return ROLE + name;
    }
}

