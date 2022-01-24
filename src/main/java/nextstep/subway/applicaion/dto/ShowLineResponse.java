package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowLineResponse {

    @JsonUnwrapped
    private LineResponse lineResponse;
    private List<StationResponse> stations = new ArrayList();

    private ShowLineResponse() {
    }

    public static ShowLineResponse of(
            Long id,
            String name,
            String color,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {

        ShowLineResponse result = new ShowLineResponse();
        result.lineResponse = new LineResponse(id, name, color, createdDate, modifiedDate);

        return result;
    }

    @JsonIgnore
    public long getLineId() {
        return lineResponse.getId();
    }

    @JsonIgnore
    public String getLineName() {
        return lineResponse.getName();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

}
