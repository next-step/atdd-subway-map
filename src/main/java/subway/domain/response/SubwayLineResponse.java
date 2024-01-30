package subway.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.entity.Station;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubwayLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;
    private int distance;

//    public SubwayLineResponse(Long id, String name, String color, List<StationResponse> stations, int distance) {
//        this.id = id;
//        this.name = name;
//        this.color = color;
//        this.stations = stations;
//        this.distance = distance;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public List<StationResponse> getStations() {
//        return stations;
//    }


}
