package nextstep.subway.line.dto;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations; // DTO에서 Entity접근하지 않기!
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // TODO 지저분하다...
    // Line -> LineResponse
    public static LineResponse of(Line line) {
        List<StationResponse> list = new ArrayList<>();

        if(line.sectionSize() > 0){
            Long id = line.getFirstStationId();
            list.add(StationResponse.of(line.getFirstStation()));

            Station station = null;
            while((station = line.getNextStation(id)) != null){
                id = station.getId();
                list.add(StationResponse.of(station));
            }
        }

        return new LineResponse(line.getId(), line.getName(), line.getColor(), list, line.getCreatedDate(), line.getModifiedDate());
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

}
