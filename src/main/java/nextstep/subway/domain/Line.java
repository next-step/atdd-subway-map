package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "지하철 노선의 색은 필수 값 입니다.")
    @Length(min = 1, max = 100, message = "지하철 노선 색의 길이를 확인해주세요.")
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    @Builder
    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.addSection(section);
    }

    public static Line createLine(String name, String color, Section section) {
        return Line.builder()
                .name(name)
                .color(color)
                .section(section)
                .build();
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public int getTotalDistance() {
        int totalDistance = 0;
        for (Section section : sections) {
            totalDistance += section.getDistance();
        }
        return totalDistance;
    }

    public Station getUpStationTerminal() {
        return sections.get(0).getUpStation();
    }

    public Station getDownStationTerminal() {
        return sections.get(0).getDownStation();
    }

    private void addSection(Section section) {
        sections.add(section);
        section.setLine(this);
    }
}
