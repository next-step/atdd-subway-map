package subway.domain;

import org.springframework.util.StringUtils;
import subway.domain.exception.AlreadyRegisteredSectionDownStationException;
import subway.domain.exception.NotEnoughSectionException;
import subway.domain.exception.NotMatchesSectionStationException;
import subway.domain.vo.Sections;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @Embedded
    private Sections sections = new Sections();

    protected SubwayLine() {
    }

    public Station getUpStation() {
        return this.sections.getUpStation();
    }

    public Station getDownStation() {
        return this.sections.getDownStation();
    }

    public static SubwayLineBuilder builder() {
        return new SubwayLineBuilder();
    }

    public void expandLine(Section newSection) {
        if (this.sections.isNotEndWith(newSection.getUpStation())) {
            throw new NotMatchesSectionStationException(this.getDownStation(), newSection.getUpStation());
        }
        if (this.sections.contains(newSection.getDownStation())) {
            throw new AlreadyRegisteredSectionDownStationException(newSection.getDownStation());
        }

        this.sections.add(newSection);
        this.distance = sections.sumOfDistance();
    }

    public void pop(Station targetStation) {
        if (this.sections.isMinimumSize()) {
            throw new NotEnoughSectionException();
        }
        if (this.sections.isNotEndWith(targetStation)) {
            throw new NotMatchesSectionStationException(this.getDownStation(), targetStation);
        }

        this.sections.pop();
        this.distance = sections.sumOfDistance();
    }

    public long getDistance() {
        return this.distance;
    }

    public static class SubwayLineBuilder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private Long distance;

        private SubwayLineBuilder() {
        }

        public SubwayLineBuilder name(String name) {
            assert StringUtils.hasText(name);
            this.name = name;
            return this;
        }

        public SubwayLineBuilder color(String color) {
            assert StringUtils.hasText(color);
            this.color = color;
            return this;
        }

        public SubwayLineBuilder upStation(Station upStation) {
            Objects.requireNonNull(upStation);
            this.upStation = upStation;
            return this;
        }

        public SubwayLineBuilder downStation(Station downStation) {
            Objects.requireNonNull(downStation);
            this.downStation = downStation;
            return this;
        }

        public SubwayLineBuilder distance(Long distance) {
            Objects.requireNonNull(distance);
            this.distance = distance;
            return this;
        }

        public SubwayLine build() {
            return new SubwayLine(this.name, this.color, upStation, downStation, distance);
        }


    }

    public SubwayLine(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections.add(Section.of(upStation, downStation, distance));
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void modify(String lineName, String color) {
        this.name = lineName;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubwayLine that = (SubwayLine) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
