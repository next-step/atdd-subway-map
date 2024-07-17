package subway.model.response;

import lombok.Getter;
import subway.domain.Station;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;

    private Station upStation;
    private Station downStation;
    private Long distance;

    public LineResponse(Long id,
                        String name,
                        String color,
                        Station upStation,
                        Station downStation,
                        Long distance){
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

}
