package subway;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 31, nullable = false)
    private String name;
    @Column(length = 31, nullable = false)
    private String color;
    @JoinColumn(name = "up_station_id", nullable = false)
    @OneToOne
    private Station upStation;
    @JoinColumn(name = "down_station_id", nullable = false)
    @OneToOne
    private Station downStation;
    @Column(nullable = false)
    private Integer distance;


    public SubwayLine(String name, String color, Station upStationId,
        Station downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStationId;
        this.downStation = downStationId;
        this.distance = distance;
    }

    public void update(Long id, SubwayLineRequest subwayLineRequest) {
        this.id = id;
        this.name = subwayLineRequest.getName();
        this.color = subwayLineRequest.getColor();
    }
}
