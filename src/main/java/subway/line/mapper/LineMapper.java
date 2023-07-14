package subway.line.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import subway.line.domain.Line;
import subway.line.dto.AddLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineResponse;
import subway.station.mapper.StationMapper;

@Mapper(uses = StationMapper.class)
public interface LineMapper {
    LineMapper LINE_MAPPER = Mappers.getMapper(LineMapper.class);

    LineResponse mapToLineResponse(Line line);

    ModifyLineResponse mapToModifyLineResponse(Line line);

    @Mapping(target = "stations", ignore = true)
    Line mapToLine(AddLineRequest modifyAddLineRequest);
}
