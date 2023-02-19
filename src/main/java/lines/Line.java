package lines;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String upStationId;

    @Column(length = 20, nullable = false)
    private String downStationId;

    @Column(length = 20, nullable = false)
    private String distance;




    public Line() {
    }

    public Line(String name, String color, String upStationId, String downStationId,
        String distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public void setUpStationId(String upStationId) {
        this.upStationId = upStationId;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public void setDownStationId(String downStationId) {
        this.downStationId = downStationId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
