package subway.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class StationResponseHelper extends ResponseHelper {
    public StationResponseHelper(ExtractableResponse<Response> response) {
        super(response);
    }

    public Long extractId() {
        return this.response.jsonPath().getLong("id");
    }

    public List<Long> extractIds() {
        return this.response.jsonPath().getList("id", Long.class);
    }

    public List<String> extractNames() {
        return this.response.jsonPath().getList("name", String.class);
    }
}
