package subway.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import subway.domain.Line;
import subway.dto.LineResponse;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LineMapper {
    LineMapper INSTANCE = Mappers.getMapper(LineMapper.class);

    @Mapping(target = "stations", ignore = true)
    LineResponse toResponse(Line line);

    List<LineResponse> toResponseList(List<Line> line);

    @AfterMapping
    default void setStations(@MappingTarget LineResponse response, Line line){
        response.setStations(List.of(line.getUpStation(), line.getDownStation()));
    }
}
