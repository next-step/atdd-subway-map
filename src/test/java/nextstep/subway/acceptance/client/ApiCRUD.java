package nextstep.subway.acceptance.client;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public interface ApiCRUD {

    ExtractableResponse<Response> create(String path, Map<String, Object> jsonBody);

    ExtractableResponse<Response> read(String path);

    <T> ExtractableResponse<Response> read(String path, T pathVariable);

    ExtractableResponse<Response> update(String path, Map<String, Object> jsonBody);

    <T> ExtractableResponse<Response> delete(String path, T pathVariable);

}
