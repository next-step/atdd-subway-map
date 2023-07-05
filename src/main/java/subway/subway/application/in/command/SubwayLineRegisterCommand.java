package subway.subway.application.in.command;

import subway.subway.domain.Kilometer;
import subway.subway.domain.Station;
import subway.subway.domain.Station.Id;

import java.math.BigDecimal;

public class SubwayLineRegisterCommand {
    private final String name;
    private final String color;
    private final Station.Id upStationId;
    private final Station.Id downStationId;
    private final Kilometer distance;

    public SubwayLineRegisterCommand(String name, String color, Station.Id upStationId, Station.Id downStationId, Kilometer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Station.Id getUpStationId() {
        return upStationId;
    }

    public Station.Id getDownStationId() {
        return downStationId;
    }

    public Kilometer getDistance() {
        return distance;
    }
}
