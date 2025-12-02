package pl.mo.trading_system;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import pl.mo.trading_system.exchanges.gpw.GpwConnector;
import pl.mo.trading_system.exchanges.gpw.dto.GpwTicker;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestConfiguration
@ContextConfiguration(classes = {GpwConnector.class})
class GpwConnectorTests {


    private WireMockServer wireMockServer;

    @Autowired
    WiremockStandalone.WiremockConfiguration wiremockConfiguration;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    RestTemplate restTemplate;

    @Autowired
    GpwConnector gpwConnector;

    @BeforeEach
    void setup() {
        wireMockServer = wiremockConfiguration.setupServer();
        wiremockConfiguration.configure(wireMockServer);
        restTemplate = restTemplateBuilder.build();
    }

    @AfterEach
    void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

	@Test
	void contextLoads() {

        var response = restTemplate.getForEntity("http://localhost:8088/gpw/tickers", String.class);

        var body = response.getBody();

        System.out.println(body);
	}

    @Test
    void gpwConnectorGetTickers() {

        var tickers = gpwConnector.getTickers();

        assertThat(tickers).hasSize(1).first().extracting(GpwTicker::isin).isEqualTo("PLBSK0000017");

    }

    @Test
    void gpwPlaceOrder() {



    }

}
