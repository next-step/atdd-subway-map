package nextstep.subway.acceptance.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import nextstep.subway.acceptance.client.LineClient;
import nextstep.subway.acceptance.client.StationClient;
import nextstep.subway.acceptance.client.dto.LineCreationRequest;
import nextstep.subway.acceptance.client.dto.SectionRegistrationRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GivenUtils {

    public static final String GREEN = "green";
    public static final String RED = "red";
    public static final String YELLOW = "yellow";
    public static final int TEN = 10;
    public static final int FIVE = 5;
    public static final String 이호선_이름 = "2호선";
    public static final String 신분당선_이름 = "신분당선";
    public static final String 분당선_이름 = "분당선";
    public static final String 강남역_이름 = "강남역";
    public static final String 역삼역_이름 = "역삼역";
    public static final String 양재역_이름 = "양재역";
    public static final String 선릉역_이름 = "선릉역";
    public static final List<String> 이호선역_이름들 = List.of(강남역_이름, 역삼역_이름);
    public static final List<String> 신분당선역_이름들 = List.of(강남역_이름, 양재역_이름);

    private final StationClient stationClient;
    private final LineClient lineClient;
    private final JsonResponseConverter responseConverter;

    public static Station 강남역() {
        return new Station(1L, 강남역_이름);
    }

    public static Station 역삼역() {
        return new Station(2L, 역삼역_이름);
    }

    public static Station 양재역() {
        return new Station(3L, 양재역_이름);
    }

    public static Station 선릉역() {
        return new Station(4L, 선릉역_이름);
    }

    public static Line 이호선() {
        return new Line(1L, 이호선_이름, GREEN);
    }

    public static Line 분당선() {
        return new Line(2L, 분당선_이름, YELLOW);
    }

    public static Section 강남_역삼_구간() {
        return new Section(이호선(), 강남역(), 역삼역(), TEN);
    }

    public static Section 역삼_선릉_구간() {
        return new Section(이호선(), 역삼역(), 선릉역(), FIVE);
    }

    public ExtractableResponse<Response> 강남역_생성() {
        return stationClient.createStation(강남역_이름);
    }

    public LineCreationRequest 이호선_생성_요청() {
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(이호선역_이름들));
        return new LineCreationRequest(
                이호선_이름,
                GREEN,
                stationIds.get(0),
                stationIds.get(1),
                TEN
        );
    }

    public LineCreationRequest 신분당선_생성_요청() {
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(신분당선역_이름들));
        return new LineCreationRequest(
                신분당선_이름,
                RED,
                stationIds.get(0),
                stationIds.get(1),
                FIVE
        );
    }

    public ExtractableResponse<Response> 이호선_생성() {
        return lineClient.createLine(이호선_생성_요청());
    }

    public SectionRegistrationRequest 역삼_선릉_구간_생성_요청() {
        List<Long> stationIds = responseConverter.convertToIds(stationClient.fetchStations());
        Long downStationId = responseConverter.convertToId(stationClient.createStation(선릉역_이름));
        return new SectionRegistrationRequest(
                stationIds.get(stationIds.size() - 1),
                downStationId,
                TEN
        );
    }

}
