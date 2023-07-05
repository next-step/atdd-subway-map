package subway.line;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import subway.station.Station;

import javax.persistence.ManyToOne;


@AllArgsConstructor
@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Station upStation;
    private Station downStation;

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

}
