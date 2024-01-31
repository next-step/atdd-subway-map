package subway.line.section;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;
import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Section implements Persistable {
    @EmbeddedId
    private SectionId sectionId;
    @Column
    private Long distanceFromPrev;
    @ManyToOne
    @MapsId(value = "stationId")
//    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne
    @MapsId(value = "lineId")
//    @JoinColumn(name = "line_id")
    private Line line;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Section() {
    }

    public Section(Line line, Station station, Long distanceFromPrev) {
        this(new SectionId(line.getId(), station.getId()), distanceFromPrev);
    }

    public Section(SectionId sectionId, Long distanceFromPrev) {
        this.sectionId = sectionId;
        this.distanceFromPrev = distanceFromPrev;
    }

    public static List<Section> firstSectionsOf(Line line) throws CannotAddSectionException {
        Section up = new Section(line, line.getUpStation(), 0L);
        Section down = new Section(line, line.getDownStation(), line.getDistance());

        up.setLine(line);
        down.setLine(line);
        up.setStation(line.getUpStation());
        down.setStation(line.getDownStation());
        return List.of(up, down);
    }

    public SectionId getSectionId() {
        return sectionId;
    }

    public Long getDistanceFromPrev() {
        return distanceFromPrev;
    }

    @Override
    public Object getId() {
        return sectionId;
    }

    public Station getStation() {
        return station;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }

    public void setLine(@NotNull Line line) throws CannotAddSectionException {
        if (this.line != null) {
            this.line.getSections().remove(line);
        }
        this.line = line;

        List<Section> sections = line.getSections();
        if (sections.contains(this)) {
            throw new CannotAddSectionException("이미 등록된 역은 구간으로 추가할 수 없습니다.");
        }
        sections.add(this);
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(sectionId, section.sectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionId);
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionId=" + sectionId +
                ", distanceFromPrev=" + distanceFromPrev +
                ", createdAt=" + createdAt +
                '}';
    }
}
