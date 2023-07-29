package subway.section.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import subway.section.domain.Section;
import subway.section.dto.*;
import subway.station.mapper.StationMapper;

@Mapper(uses = StationMapper.class)
public interface SectionMapper {
    SectionMapper SECTION_MAPPER = Mappers.getMapper(SectionMapper.class);

    @Mapping(target = "upStationId", source = "upStation.id")
    @Mapping(target = "downStationId", source = "downStation.id")
    CreateSectionResponse mapToAddSectionResponse(Section section);

    @Mapping(target = "upStationId", source = "upStation.id")
    @Mapping(target = "downStationId", source = "downStation.id")
    SectionResponse mapToSectionResponse(Section section);
}
