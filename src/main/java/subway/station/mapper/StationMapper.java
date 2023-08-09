package subway.station.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import subway.station.domain.Station;
import subway.station.dto.StationResponse;

import java.util.List;

@Mapper
public interface StationMapper {
    StationMapper STATION_MAPPER = Mappers.getMapper(StationMapper.class);

    List<StationResponse> toStationResponses(List<Station> stations);

    StationResponse toStationResponse(Station station);
}
