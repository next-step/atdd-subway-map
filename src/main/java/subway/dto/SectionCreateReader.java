package subway.dto;

public interface SectionCreateReader {
    Long getDownStationId();

    Long getUpStationId();

    Long getDistance();
}
