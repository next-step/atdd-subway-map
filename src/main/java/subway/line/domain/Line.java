package subway.line.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.util.CollectionUtils;

import subway.section.domain.Section;
import subway.station.domain.Station;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String color;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST)
    private List<Section> sections;

    public static Line firstLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(null, name, color, upStation, downStation, distance);
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;

        this.sections = List.of(Section.firstSection(this, upStation, downStation, distance));
    }

    public boolean isLastStation(Station station) {
        if (CollectionUtils.isEmpty(this.sections)) {
            return false;
        }

        if (this.sections.size() != 1) {
            return false;
        }

        return Objects.equals(this.sections.get(0).getUpStation().getId(), station.getId()) ||
                Objects.equals(this.sections.get(0).getDownStation().getId(), station.getId());
    }
}
