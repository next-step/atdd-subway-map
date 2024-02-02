package subway.service.dto;

import subway.domain.Station;

public class LineSectionDto {
    private Long id;
    private StationDto upStation;
    private StationDto downStation;
    private Integer distance;

    public LineSectionDto(Long id, StationDto upStation, StationDto downStation, Integer distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public StationDto getUpStation() {
        return upStation;
    }

    public StationDto getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
