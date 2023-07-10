package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import subway.dto.LineRequest;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(length = 20, nullable = false)
    private Long distance;

    @OneToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    public Line() {

    }

    public Line(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.distance = lineRequest.getDistance();
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void updateLine(LineRequest lineRequest) {
        this.name = lineRequest.getName().isBlank() ? this.name : lineRequest.getName();
        this.color = lineRequest.getColor().isBlank() ? this.color : lineRequest.getColor();
        this.distance = lineRequest.getDistance() == null ? this.distance : lineRequest.getDistance();
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

    public Long getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }


}
