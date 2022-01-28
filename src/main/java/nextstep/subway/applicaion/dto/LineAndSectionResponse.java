package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineAndSectionResponse {
    @JsonUnwrapped
    private LineResponse lineResponse;
    private List<StationResponse> stationResponses;

    private LineAndSectionResponse() {
    }

    public static LineAndSectionResponse of(Line line) {
        LineAndSectionResponse lineAndSectionResponse = new LineAndSectionResponse();

        lineAndSectionResponse.lineResponse = LineResponse.of(line);

        lineAndSectionResponse.stationResponses = line.getSections().getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return lineAndSectionResponse;
    }

    @JsonIgnore
    public Long getLineId() {
        return lineResponse.getId();
    }

    public List<StationResponse> getStations() {
        return stationResponses;
    }
}
