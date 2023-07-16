package subway.section;

import subway.line.SubwayLine;
import subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubwaySection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    @Column
    private Integer distance;

    @ManyToOne
    @JoinColumn(name = "subway_line_id")
    private SubwayLine line;

    public SubwaySection() {}

    public SubwaySection(Station upStation, Station downStation, SubwayLine line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
    }

    public Long getId() { return id; }

    public Integer getDistance() { return distance; }

    public SubwayLine getLine() { return line; }

    public void calculateAndInsertDistance(Integer totalDistance) {
        Integer remainingDistance = totalDistance;
        List<SubwaySection> sections = this.line.getSections();

        if (sections.size() != 1) {
            remainingDistance = sections.stream()
                    .mapToInt(SubwaySection::getDistance)
                    .reduce(totalDistance, (subtotal, distance) -> subtotal - distance);
        }

        System.out.println("remainingDistance : " + remainingDistance);
        this.distance = remainingDistance;
    }
}