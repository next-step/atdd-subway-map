package subway.models;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Embedded
    private Sections sections = new Sections();

    @Builder
    private Line(@NonNull String name, @NonNull String color, @NonNull Station upStation,
        @NonNull Station downStation, @NonNull Long distance) {
        this.name = name;
        this.color = color;
        addSection(Section.builder()
            .line(this)
            .downStation(downStation)
            .upStation(upStation)
            .distance(distance)
            .build());
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
