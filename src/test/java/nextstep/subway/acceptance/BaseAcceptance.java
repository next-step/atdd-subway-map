package nextstep.subway.acceptance;

import nextstep.subway.config.DataBaseCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
public abstract class BaseAcceptance {

    @LocalServerPort
    protected int port;

    @Autowired
    protected DataBaseCleaner dataBaseCleaner;

}
