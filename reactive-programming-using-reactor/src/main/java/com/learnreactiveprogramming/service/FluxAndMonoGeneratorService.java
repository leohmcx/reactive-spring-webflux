package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Mono<String> nameMono()  {
        return Mono.just("alex").log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).map(String::toUpperCase).log();
    }

    public Flux<String> namesFluxImmutability() {
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        // this won't be applied because wasn't applied in the direct function or the operations together in the above
        // line this show why the reactive streams are immutable.
        namesFlux.map(String::toUpperCase);

        return namesFlux;
    }

    public Flux<String> namesFluxFilter(int nameFilter) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameFilter)
                .log();
    }

    public Flux<String> namesFluxFlatMap(int nameFilter) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameFilter)
                // A, L, E, X, C, H, L, O, E
                .flatMap(this::splitString)
                .log();
    }

    private Flux<String> splitString(String name) {
        var charArray = name.split("");

        return Flux.fromArray(charArray);
    }

    public Flux<String> namesFluxFlatMapDelay(int nameFilter) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameFilter)
                // A, L, E, X, C, H, L, O, E
                .flatMap(this::splitStringDelay)
                .log();
    }

    private Flux<String> splitStringDelay(String name) {
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);

        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux().subscribe(name -> System.out.println("Flux Name is: " + name));
        fluxAndMonoGeneratorService.nameMono().subscribe(name -> System.out.println("Mono Name is: " + name));
    }
}
