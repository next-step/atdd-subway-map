package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToOne
    @JoinColumn(name = "UP_ENDPOINT_STATION_ID")
    private Station upEndpointStation;

    @OneToOne
    @JoinColumn(name = "DOWN_ENDPOINT_STATION_ID")
    private Station downEndpointStation;


    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
    public Station getUpEndpointStation() {
        return upEndpointStation;
    }

    public Station getDownEndpointStation() {
        return downEndpointStation;
    }

    public void addUpEndpointStation(Station station) {
        this.upEndpointStation = station;
    }

    public void addDownEndpointStation(Station station) {
        this.downEndpointStation = station;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }

}
