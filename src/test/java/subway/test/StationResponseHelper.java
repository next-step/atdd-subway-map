package subway.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class StationResponseHelper {
    private final ExtractableResponse<Response> response;

    public StationResponseHelper(ExtractableResponse<Response> response) {
        this.response = response;
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

    public Integer getStatusCode() {
        return this.response.statusCode();
    }
}
