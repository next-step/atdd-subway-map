package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "NAME")
    private String name;

    @Column(name = "UP_STATION_ID")
    private Long upStationId;

    @Column(name = "DOWN_STATION_ID")
    private Long downStationId;

    @Column(name = "DISTANCE")
    private int distance;

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if(color != null ){
            this.color = color;
        }
    }

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void setName(String name) {
        if(name != null) {
            this.name = name;
        }
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

}
