package subway.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StationMapper {
    StationMapper INSTANCE = Mappers.getMapper(StationMapper.class);

    StationResponse toResponse(Station station);

    List<StationResponse> toResponseList(List<Station> stations);
}
