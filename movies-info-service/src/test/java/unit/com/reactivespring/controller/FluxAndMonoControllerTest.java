package com.reactivespring.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Nested
    class FluxTesting {
        @Test
        void flux() {
            webTestClient.get()
                    .uri("/flux")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .expectBodyList(Integer.class)
                    .hasSize(3);
        }

        @Test
        void fluxApproach2() {
            final var response = webTestClient.get()
                    .uri("/flux")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .returnResult(Integer.class)
                    .getResponseBody();

            StepVerifier.create(response)
                    .expectNext(1, 2, 3)
                    .verifyComplete();
        }

        @Test
        void fluxApproach3() {
            webTestClient.get()
                    .uri("/flux")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .expectBodyList(Integer.class)
                    .consumeWith(listEntityExchangeResult -> {
                        var responseBody = listEntityExchangeResult.getResponseBody();
                        assert (Objects.requireNonNull(responseBody).size() == 3);
                    });
        }
    }


    @Nested
    class MonoTesting {
        @Test
        void helloWorldMono() {
            webTestClient.get()
                    .uri("/mono")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .expectBodyList(String.class)
                    .hasSize(1);
        }

        @Test
        void helloWorldMonoApproach3() {
            webTestClient.get()
                    .uri("/mono")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .expectBody(String.class)
                    .consumeWith(stringEntityExchangeResult -> {
                        var responseBody = stringEntityExchangeResult.getResponseBody();
                        assertEquals("hello-world", responseBody);
                    });
        }
    }

    @Nested
    class StreamTesting {
        @Test
        void stream() {
            final var response = webTestClient.get()
                    .uri("/stream")
                    .exchange()
                    .expectStatus()
                    .is2xxSuccessful()
                    .returnResult(Long.class)
                    .getResponseBody();

            StepVerifier.create(response)
                    .expectNext(0L, 1L, 2L, 3L)
                    .thenCancel()
                    .verify();
        }
    }
}