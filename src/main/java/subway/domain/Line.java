package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static subway.common.constants.ErrorConstant.*;
import static subway.common.constants.ErrorConstant.NOT_EXIST_SECTION;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public Line(String name, String color, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Station station) {
        this.downStation = station;
    }


    public void isAddValidation(Station upStation, Station downStation) {
        if (!getDownStation().equals(upStation)) {
            throw new IllegalArgumentException(NOT_LAST_STATION);
        }

        if (getUpStation().equals(downStation)) {
            throw new IllegalArgumentException(ALREADY_ENROLL_STATION);
        }
    }


    public void isDeleteValidation(Station station) {
        if (!getDownStation().equals(station)) {
            throw new IllegalArgumentException(NOT_DELETE_LAST_STATION);
        }

        if (sections.size() < 2) {
            throw new IllegalArgumentException(NOT_EXIST_SECTION);
        }
    }

}
