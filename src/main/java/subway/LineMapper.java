package subway;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LineMapper {
    LineMapper LINE_MAPPER = Mappers.getMapper(LineMapper.class);

    LineResponse mapToLineResponse(Line line);

    ModifyLineResponse mapToModifyLineResponse(Line line);

    Line mapToLine(AddLineRequest modifyAddLineRequest);
}
