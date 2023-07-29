package subway.line.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import subway.line.domain.Line;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineResponse;
import subway.station.mapper.StationMapper;

@Mapper(uses = StationMapper.class)
public interface LineMapper {
    LineMapper LINE_MAPPER = Mappers.getMapper(LineMapper.class);

    LineResponse toLineResponse(Line line);

    ModifyLineResponse toModifyLineResponse(Line line);

    @Mapping(target = "sections", ignore = true)
    @Mapping(target = "stations", ignore = true)
    Line mapToLine(CreateLineRequest createLineRequest);
}
