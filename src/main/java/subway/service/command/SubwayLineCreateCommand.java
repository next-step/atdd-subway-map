package subway.service.command;

public interface SubwayLineCreateCommand {

    String getName();

    String getColor();

    Long getUpStationId();

    Long getDownStationId();

    Long getDistance();
}
