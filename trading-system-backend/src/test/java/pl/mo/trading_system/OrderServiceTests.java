package pl.mo.trading_system;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.mo.trading_system.gpw.GpwConnector;
import pl.mo.trading_system.orders.*;
import pl.mo.trading_system.orders.dto.OrderRequest;
import pl.mo.trading_system.orders.model.OrderStatus;
import pl.mo.trading_system.orders.model.OrderType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestConfig.class, GpwConnector.class, OrderService.class})
@EnableJpaRepositories(basePackages = "pl.mo.trading_system.orders")
class OrderServiceTests {


    private WireMockServer wireMockServer;

    @Autowired
    WiremockStandalone.WiremockConfiguration wiremockConfiguration;

    @Autowired
    OrderService orderService;

    @BeforeEach
    void setup() {
        wireMockServer = wiremockConfiguration.setupServer();
        wiremockConfiguration.configure(wireMockServer);
    }

    @AfterEach
    void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

	@Test
	void placeOrderTest() {

        OrderRequest orderRequest = OrderRequest.builder()
                .orderType(OrderType.PCR)
                .isin("1234")
                .expiresAt(null)
                .quantity(1)
                .priceLimit(200.0)
                .build();
        var response = orderService.placeOrder(orderRequest);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.FILLED);
	}


}
