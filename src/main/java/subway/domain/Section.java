package subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int distance;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    @ManyToOne
    private Line line;

    public Section(int distance, Station upStation, Station downStation, Line line) {
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return getDistance() == section.getDistance() && Objects.equals(getId(), section.getId()) && Objects.equals(getUpStation(), section.getUpStation()) && Objects.equals(getDownStation(), section.getDownStation()) && Objects.equals(getLine(), section.getLine());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDistance(), getUpStation(), getDownStation(), getLine());
    }
}
