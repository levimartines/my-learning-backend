package com.levimartines.mylearningbackend.it;

import com.levimartines.mylearningbackend.utils.Base64;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureWireMock(port = 3031)
public class PrincipalControllerIntegrationTest extends BaseIntegrationTest {

    private final String basePath = "/principal";


    @Nested
    class GenerateQrUrl {

        @Test
        void shouldReturn200WhenLoggedIsAnAdmin() {
            byte[] stubResponse = "myBase64QrCode".getBytes();
            stubFor(get(urlPathEqualTo("/chart")).willReturn(aResponse().withHeader("Content-Type", "text/plain").withBody(stubResponse)));

            String url = basePath + "/generate-qr-url";
            HttpEntity<?> entity = getEntity(true);
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());

            String expected = Base64.encode(stubResponse);
            assertEquals(expected, response.getBody());
        }

    }
}
