package subway.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import subway.domain.Line;
import subway.dto.LineResponse;

@Mapper(componentModel = "spring")
public interface LineMapper {
    LineMapper INSTANCE = Mappers.getMapper(LineMapper.class);

    LineResponse toResponse(Line line);
}
