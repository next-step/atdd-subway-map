package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import subway.station.Station;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String color;

    @OneToOne(mappedBy = "id")
    private Station upStation;

    @OneToOne(mappedBy = "id")
    private Station downStation;

    @Column(nullable = false)
    private Integer distance;
}
