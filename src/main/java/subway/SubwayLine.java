package subway;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SubwayLine {
    @Id
    @GeneratedValue
    private Long id;

    private String color;

    @Column(length=20, nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name="upStationId")
    private Station upStation;


    @ManyToOne()
    @JoinColumn(name="downStationId")
    private Station downStation;
    private Integer distance;

    public SubwayLine(
        String name,
        String color,
        Station upStation,
        Station downStation,
        Integer distance
    ) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public SubwayLine() {

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

    public void setName(String name) {
        this.name = name;
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
}
