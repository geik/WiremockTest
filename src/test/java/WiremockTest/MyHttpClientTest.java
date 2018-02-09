package WiremockTest;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MyHttpClientTest {

    @Before
    public init() {
        // to do
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();


    @Test
    public void returnFromWebService() throws IOException {

        File req = new File("src/test/resources/sapBp001.req.xml");
        String strReq = FileUtils.readFileToString(req, "UTF-8");
        File rsp = new File("src/test/resources/sapBp001.rsp.xml");
        String strRsp = FileUtils.readFileToString(rsp, "UTF-8");

        wireMockRule.stubFor(post(urlEqualTo("/HandleBusinessPartner"))
                .withRequestBody(equalToXml(strReq))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/xml")
                        .withBody(strRsp)));

        wireMockRule.stubFor(any(anyUrl())
                .atPriority(10)
                .willReturn(aResponse()
                        .withStatus(404)
                        .withStatusMessage("Er is geen request gevonden die matcht met het inkomende request")
                        .withBody("{\"status\":\"Error\",\"message\":\"Endpoint niet gevonden\"}")));

        System.out.println(MyHttpClient.getResponseFromWebService());

    }
}