package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given

        //when
        var namesFlux = service.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("alex", "ben", "chloe")
                //.expectNextCount(3)
                .expectNext("alex").expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        //given

        //when
        var namesFlux = service.namesFluxMap();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        //given

        //when
        var namesFlux = service.namesFluxImmutability();

        //then - The only way you can see the changes is by chaining the function together or the operators together to get the desired result.
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxFilterStringByLength() {
        //given

        //when
        var namesFlux = service.namesFluxFilter(3);

        //then - The only way you can see the changes is by chaining the function together or the operators together to get the desired result.
        StepVerifier.create(namesFlux)
                .expectNext("ALEX", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        //given

        //when
        var namesFlux = service.namesFluxFlatMap(3);

        //then - The only way you can see the changes is by chaining the function together or the operators together to get the desired result.
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapDelay() {
        //given

        //when
        var namesFlux = service.namesFluxFlatMapDelay(3);

        //then - The only way you can see the changes is by chaining the function together or the operators together to get the desired result.
        StepVerifier.create(namesFlux)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapConcatMap() {
        //given

        //when
        var namesFlux = service.namesFluxFlatMapConcatMap(3);

        //then - The only way you can see the changes is by chaining the function together or the operators together to get the desired result.
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMapFilter() {
        //given

        //when
        var namesFlux = service.namesMonoFlatMapFilter(3);

        //then - The only way you can see the changes is by chaining the function together or the operators together to get the desired result.
        StepVerifier.create(namesFlux)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMapMany() {
        //given

        //when
        var namesFlux = service.namesMonoFlatMapMany(3);

        //then - The only way you can see the changes is by chaining the function together or the operators together to get the desired result.
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        //given

        //when
        var namesFlux = service.namesFluxTransform(3);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform1() {
        //given

        //when
        var namesFlux = service.namesFluxTransform(6);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }



    @Test
    void namesFluxTransformSwitchIfEmpty() {
        //given

        //when
        var namesFlux = service.namesFluxTransformSwitchIfEmpty(6);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void exploreConcat() {
        //given

        //when
        var concatFlux = service.exploreConcat();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWith() {
        //given

        //when
        var concatFlux = service.exploreConcatWith();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreMerge() {
        //given

        //when
        var concatFlux = service.exploreMerge();

        //then
        StepVerifier.create(concatFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void exploreMergeWith() {
        //given

        //when
        var concatFlux = service.exploreMergeWith();

        //then
        StepVerifier.create(concatFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void exploreMergeSeq() {
        //given

        //when
        var concatFlux = service.exploreMergeSeq();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreZip() {
        //given

        //when
        var concatFlux = service.exploreZip();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void exploreZip1() {
        //given

        //when
        var concatFlux = service.exploreZip1();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void exploreZipWithFlux() {
        //given

        //when
        var concatFlux = service.exploreZipWithFlux();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void exploreZipWithMono() {
        //given

        //when
        var concatFlux = service.exploreZipWithMono();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("AD")
                .verifyComplete();
    }
}