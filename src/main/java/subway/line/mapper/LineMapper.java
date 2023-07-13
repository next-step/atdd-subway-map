package subway.line.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import subway.line.domain.Line;
import subway.line.dto.AddLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineResponse;

@Mapper
public interface LineMapper {
    LineMapper LINE_MAPPER = Mappers.getMapper(LineMapper.class);

    LineResponse mapToLineResponse(Line line);

    ModifyLineResponse mapToModifyLineResponse(Line line);

    Line mapToLine(AddLineRequest modifyAddLineRequest);
}
