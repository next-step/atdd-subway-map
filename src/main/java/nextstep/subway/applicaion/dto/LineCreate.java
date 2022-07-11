package nextstep.subway.applicaion.dto;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.domain.Color;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

public class LineCreate {

  public static class Request {
    private String name;

    private Color color;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    public static Line createLine(LineCreate.Request request, Station upStation, Station downStation) {
      return new Line(request.getName(), request.getColor(), upStation, downStation, request.distance);
    }

    public String getName() {
      return name;
    }

    public Color getColor() {
      return color;
    }

    public Long getUpStationId() {
      return upStationId;
    }

    public Long getDownStationId() {
      return downStationId;
    }

    public int getDistance() {
      return distance;
    }
  }

  public static class Response {
    private Long id;

    private String name;

    private Color color;

    private List<StationResponse> stations;

    public Response() {}

    public Response(Long id, String name, Color color, List<StationResponse> stations) {
      this.id = id;
      this.name = name;
      this.color = color;
      this.stations = stations;
    }

    public static Response createResponse(Line line) {
      StationResponse upStationResponse = new StationResponse(line.getUpStation().getId(), line.getUpStation().getName());
      StationResponse downStationResponse = new StationResponse(line.getDownStation().getId(), line.getDownStation().getName());

      return new Response(line.getId(), line.getName(), line.getColor(), Arrays.asList(upStationResponse, downStationResponse));
    }

    public Long getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public Color getColor() {
      return color;
    }

    public List<StationResponse> getStations() {
      return stations;
    }
  }
}
