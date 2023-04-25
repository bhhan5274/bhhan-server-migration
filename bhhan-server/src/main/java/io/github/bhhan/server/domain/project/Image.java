package io.github.bhhan.server.domain.project;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "IMAGES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    @Column(nullable = false)
    private String path;

    @Builder
    public Image(Long id, String path) {
        this.id = id;
        this.path = path;
    }
}
