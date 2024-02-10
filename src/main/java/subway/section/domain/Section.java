package subway.section.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import subway.line.domain.Line;
import subway.station.domain.Station;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE section SET deleted_at = CURRENT_TIMESTAMP where section_id = ?")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

    @Column
    private Integer distance;

    @Column
    private Timestamp deleted_at;

    protected Section() {
    }

    private Section(Station upStation, Station downStation, Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, Integer distance) {
        return new Section(upStation, downStation, distance);
    }

    public boolean checkAddStation(Station upStation) {
        return this.downStation.equals(upStation);
    }

    private Long getId() {
        return this.id;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public Timestamp getDeleted_at() {
        return deleted_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Section section = (Section) o;
        return Objects.equals(id, section.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
