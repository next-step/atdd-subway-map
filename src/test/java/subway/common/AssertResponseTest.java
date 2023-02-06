package subway.common;

import subway.dto.LineResponse;
import subway.dto.SectionResponse;
import subway.dto.StationResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertResponseTest {

    public static void 응답_정보_검증(Object expectedLineInfo, Object responseLineInfo) {
        assertEquals(expectedLineInfo, responseLineInfo, "fail equals info");
    }

    public static void 응답_상태코드_검증(int expectedStatusCode, int statusCode) {
        assertEquals(expectedStatusCode, statusCode, "Fail https status code");
    }

    public static void 응답_정보_갯수_검증(int expectedSize, List<Object> responseInfos) {
        assertEquals(expectedSize, responseInfos.size(), "fail equals size info");
    }

    public static void 응답_구간정보_미포함_검증(Object expectedSectionInfo, List<SectionResponse> responseSectionInfos) {
        assertEquals(false, responseSectionInfos.contains(expectedSectionInfo), "fail include section info");
    }

    public static void 응답_노선정보_미포함_검증(Object expectedLineInfo, List<LineResponse> responseLineInfos) {
        assertEquals(false, responseLineInfos.contains(expectedLineInfo), "fail include line info");
    }

    public static void 응답_역정보_미포함_검증(Object expectedStationInfo, List<StationResponse> responseStationInfos) {
        assertEquals(false, responseStationInfos.contains(expectedStationInfo), "fail include station info");
    }
}
