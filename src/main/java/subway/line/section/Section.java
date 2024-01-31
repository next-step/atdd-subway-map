package subway.line.section;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    public static List<Section> firstSectionsOf(Line line) {
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

    public void setLine(@NotNull Line line) {
        if (this.line != null) {
            this.line.getSections().remove(line);
        }
        this.line = line;
        line.getSections().add(this);
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
