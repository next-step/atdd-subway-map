package subway.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    @Builder
    private Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>();
    }

    public void addSection(Section section) {
        section.setLine(this);
        sections.add(section);
    }
}
