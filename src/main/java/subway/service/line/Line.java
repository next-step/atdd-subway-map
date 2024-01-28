package subway.service.line;

import subway.service.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Station upStation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Station downStation;

    public Line(){

    }
    public Line(String name, String color, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
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

    public void changeName(String name) {
        if (name == null) {
            return;
        }
        if (name.isEmpty()) {
            return;
        }
        if (name.isBlank()) {
            return;
        }
        this.name = name;
    }

    public void changeColor(String color) {
        if (color == null) {
            return;
        }
        if (color.isEmpty()) {
            return;
        }
        if (color.isBlank()) {
            return;
        }
        this.color = color;
    }
}
