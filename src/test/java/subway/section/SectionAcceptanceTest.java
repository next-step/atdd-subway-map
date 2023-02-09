package subway.section;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.common.AcceptanceTest;

import static subway.common.constants.ErrorConstant.*;
import static subway.utils.AssertUtil.*;
import static subway.utils.LineUtil.createLineResultResponse;
import static subway.utils.LineUtil.showLineResultResponse;
import static subway.utils.SectionUtil.addSectionResponse;
import static subway.utils.SectionUtil.deleteSectionResponse;
import static subway.utils.StationUtil.createStationResultResponse;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
}
