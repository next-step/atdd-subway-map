package subway;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Lines {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String color;
    @ManyToOne()
    @JoinColumn(name="upStationId")
    private Station upStation;
    @ManyToOne()
    @JoinColumn(name="downStationId")
    private Station downStation;
    private Long distance;

    public Lines(String name, String color, Station upStation, Station downStation,
        Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Lines() {

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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
