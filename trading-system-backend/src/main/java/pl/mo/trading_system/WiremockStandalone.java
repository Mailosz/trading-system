package pl.mo.trading_system;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WiremockStandalone {
    public static void main(String[] args) {

        WiremockConfiguration wc = new WiremockConfiguration();

        // Create server instance and start
        WireMockServer wireMockServer = wc.setupServer();

        wc.configure(wireMockServer);

        System.out.println("WireMock server running at http://localhost:8088");

        // Keep running until manually stopped
        Runtime.getRuntime().addShutdownHook(new Thread(wireMockServer::stop));
    }


    static class WiremockConfiguration {

        int port = 8088;

        public WireMockServer setupServer() {

            var wireMockServer = new WireMockServer(WireMockConfiguration.options().port(port));
            wireMockServer.start();

            WireMock.configureFor("localhost", port);


            return wireMockServer;
        }

        public void configure(WireMockServer wireMockServer) {
            wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/gpw/tickers"))
                    .willReturn(WireMock.aResponse().withHeader("content-type", "application/json").withBody("""
                            {
                                "tickers": [
                                    {
                                        "name": "ING Bank Śląski",
                                        "ticker": "INGBSK",
                                        "isin": "PLBSK0000017",
                                        "tradeCurrency": "PLN",
                                        "mic": "XWAR"
                                    }
                                ]
                            }
                            """)));

            wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/gpw/prices/current"))
                    .willReturn(WireMock.aResponse().withHeader("content-type", "application/json").withBody("""
                            {
                                "results": [
                                    {
                                        "isin": "PLBSK0000017",
                                        "price": 314
                                    }
                                ]
                            }
    
                            """)));

            wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/gpw/orders"))
                    .willReturn(WireMock.aResponse().withHeader("content-type", "application/json").withBody("""
                            {
                                "orderId": 1111111,
                                "status": "Submitted",
                                "registrationTime": 1762444418
                            }
                            """)));


            wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/gpw/order/.*"))
                    .willReturn(WireMock.aResponse().withHeader("content-type", "application/json").withBody("""
                            {
                                "orderId": 1111111,
                                "status": "Filled",
                                "isin": "PLBSK0000017",
                                "side": "BUY",
                                "tradeCurrency": "PLN",
                                "quantity": 10,
                                "executionPrice": 315,
                                "registrationTime": 1762444418,
                                "executedTime": 1762448027
                             }
                            """)));
        }
    }
}
