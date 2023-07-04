package subway.service.command;

public interface StationLineCreateCommand {

    String getName();

    String getColor();

    Long getUpStationId();

    Long getDownStationId();

    Long getDistance();
}
