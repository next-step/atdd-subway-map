package subway.rds_module.entity;

import subway.subway.domain.Kilometer;
import subway.subway.domain.Station;
import subway.subway.domain.SubwaySection;
import subway.subway.domain.SubwaySectionStation;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "subway_sections")
public class SubwaySectionJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subway_section_id")
    private Long id;
    @Column(nullable = false)
    private Long startStationId;
    @Column(nullable = false)
    private String startStationName;
    @Column(nullable = false)
    private Long endStationId;
    @Column(nullable = false)
    private String endStationName;
    @Column(nullable = false)
    private BigDecimal distance;

    public SubwaySectionJpa() {
    }


    public SubwaySectionJpa(Long startStationId, String startStationName, Long endStationId, String endStationName, BigDecimal distance) {
        this.startStationId = startStationId;
        this.startStationName = startStationName;
        this.endStationId = endStationId;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public SubwaySectionJpa(Long id, Long startStationId, String startStationName, Long endStationId, String endStationName, BigDecimal distance) {
        this.id = id;
        this.startStationId = startStationId;
        this.startStationName = startStationName;
        this.endStationId = endStationId;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public boolean isNew() {
        return id == null;
    }
}
