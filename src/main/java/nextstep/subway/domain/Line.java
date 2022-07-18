package nextstep.subway.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Embedded
    Sections sections;

    @Builder
    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(this, section);
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

    public void addSection(Section section) {
        sections.addSection(this, section);
    }

    public Section deleteSection(Station station) {
        return sections.deleteSection(station);
    }

    public Station getUpStationTerminal() {
        return sections.getUpStationTerminal();
    }

    public Station getDownStationTerminal() {
        return sections.getDownStationTerminal();
    }

    public int getTotalDistance() {
        return sections.getTotalDistance();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
