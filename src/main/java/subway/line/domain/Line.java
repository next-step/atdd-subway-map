package subway.line.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20)
    private String color;

    @OneToMany(mappedBy = "line")
    List<Section> sections;

    @Builder
    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line create(String name, String color) {
        return Line.builder()
                .name(name)
                .color(color)
                .build();
    }

    public void update(String name, String color) {
        if (name != null) {
            this.name = name;
        }

        if (color != null) {
            this.color = color;
        }

    }
}
