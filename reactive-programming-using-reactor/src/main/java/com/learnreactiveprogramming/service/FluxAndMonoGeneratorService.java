package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.lang.Long.parseLong;
import static java.time.Duration.ofMillis;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class FluxAndMonoGeneratorService {

    public static final List<String> NAMES = List.of("alex", "ben", "chloe");

    /**
     * 00:30:38.807 [main] INFO reactor.Mono.Just.2 - | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)
     * 00:30:38.807 [main] INFO reactor.Mono.Just.2 - | request(unbounded)
     * 00:30:38.807 [main] INFO reactor.Mono.Just.2 - | onNext(alex)
     */
    public Flux<String> namesFlux() {
        return Flux.fromIterable(NAMES).log();
    }

    /**
     * 00:30:38.704 [main] INFO reactor.Flux.Iterable.1 - | onSubscribe([Synchronous Fuseable] FluxIterable.IterableSubscription)
     * 00:30:38.707 [main] INFO reactor.Flux.Iterable.1 - | request(unbounded)
     * 00:30:38.708 [main] INFO reactor.Flux.Iterable.1 - | onNext(alex)
     * Flux Name is: alex
     * 00:30:38.736 [main] INFO reactor.Flux.Iterable.1 - | onNext(ben)
     * Flux Name is: ben
     * 00:30:38.736 [main] INFO reactor.Flux.Iterable.1 - | onNext(chloe)
     * Flux Name is: chloe
     * 00:30:38.737 [main] INFO reactor.Flux.Iterable.1 - | onComplete()
     */
    public Mono<String> nameMono()  {
        return Mono.just("alex").log();
    }

    /**
     * 00:30:06.507 [Test worker] INFO reactor.Flux.MapFuseable.1 - | onSubscribe([Fuseable] FluxMapFuseable.MapFuseableSubscriber)
     * 00:30:06.514 [Test worker] INFO reactor.Flux.MapFuseable.1 - | request(unbounded)
     * 00:30:06.515 [Test worker] INFO reactor.Flux.MapFuseable.1 - | onNext(ALEX)
     * 00:30:06.515 [Test worker] INFO reactor.Flux.MapFuseable.1 - | onNext(BEN)
     * 00:30:06.516 [Test worker] INFO reactor.Flux.MapFuseable.1 - | onNext(CHLOE)
     * 00:30:06.517 [Test worker] INFO reactor.Flux.MapFuseable.1 - | onComplete()
     */
    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(NAMES).map(String::toUpperCase).log();
    }

    /**
     * 00:29:35.987 [Test worker] INFO reactor.Flux.Iterable.1 - | onSubscribe([Synchronous Fuseable] FluxIterable.IterableSubscription)
     * 00:29:35.993 [Test worker] INFO reactor.Flux.Iterable.1 - | request(unbounded)
     * 00:29:35.994 [Test worker] INFO reactor.Flux.Iterable.1 - | onNext(alex)
     * 00:29:35.995 [Test worker] INFO reactor.Flux.Iterable.1 - | onNext(ben)
     * 00:29:35.995 [Test worker] INFO reactor.Flux.Iterable.1 - | onNext(chloe)
     * 00:29:35.998 [Test worker] INFO reactor.Flux.Iterable.1 - | onComplete()
     */
    public Flux<String> namesFluxImmutability() {
        var namesFlux = Flux.fromIterable(NAMES).log();
        // this won't be applied because wasn't applied in the direct function or the operations together in the above
        // line this show why the reactive streams are immutable.
        namesFlux.map(String::toUpperCase);

        return namesFlux;
    }

    /**
     * 00:28:18.854 [Test worker] INFO reactor.Flux.FilterFuseable.1 - | onSubscribe([Fuseable] FluxFilterFuseable.FilterFuseableSubscriber)
     * 00:28:18.863 [Test worker] INFO reactor.Flux.FilterFuseable.1 - | request(unbounded)
     * 00:28:18.864 [Test worker] INFO reactor.Flux.FilterFuseable.1 - | onNext(ALEX)
     * 00:28:18.865 [Test worker] INFO reactor.Flux.FilterFuseable.1 - | onNext(CHLOE)
     * 00:28:18.865 [Test worker] INFO reactor.Flux.FilterFuseable.1 - | onComplete()
     */
    public Flux<String> namesFluxFilter(int nameFilter) {
        return Flux.fromIterable(NAMES)
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameFilter)
                .log();
    }

    /**
     * 00:27:49.568 [Test worker] INFO reactor.Flux.FlatMap.1 - onSubscribe(FluxFlatMap.FlatMapMain)
     * 00:27:49.574 [Test worker] INFO reactor.Flux.FlatMap.1 - request(unbounded)
     * 00:27:49.578 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(A)
     * 00:27:49.579 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(L)
     * 00:27:49.579 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(E)
     * 00:27:49.580 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(X)
     * 00:27:49.580 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(C)
     * 00:27:49.580 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(H)
     * 00:27:49.582 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(L)
     * 00:27:49.582 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(O)
     * 00:27:49.582 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(E)
     * 00:27:49.583 [Test worker] INFO reactor.Flux.FlatMap.1 - onComplete()
     */
    public Flux<String> namesFluxFlatMap(int nameFilter) {
        return Flux.fromIterable(NAMES)
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameFilter)
                // A, L, E, X, C, H, L, O, E
                .flatMap(name -> Flux.fromArray(name.split("")))
                .log();
    }

    /**
     * 00:27:17.672 [Test worker] INFO reactor.Flux.FlatMap.1 - onSubscribe(FluxFlatMap.FlatMapMain)
     * 00:27:17.692 [Test worker] INFO reactor.Flux.FlatMap.1 - request(unbounded)
     * 00:27:18.122 [parallel-2] INFO reactor.Flux.FlatMap.1 - onNext(C)
     * 00:27:18.278 [parallel-3] INFO reactor.Flux.FlatMap.1 - onNext(H)
     * 00:27:18.435 [parallel-4] INFO reactor.Flux.FlatMap.1 - onNext(L)
     * 00:27:18.448 [parallel-1] INFO reactor.Flux.FlatMap.1 - onNext(A)
     * 00:27:18.589 [parallel-1] INFO reactor.Flux.FlatMap.1 - onNext(O)
     * 00:27:18.740 [parallel-3] INFO reactor.Flux.FlatMap.1 - onNext(E)
     * 00:27:19.085 [parallel-2] INFO reactor.Flux.FlatMap.1 - onNext(L)
     * 00:27:19.728 [parallel-4] INFO reactor.Flux.FlatMap.1 - onNext(E)
     * 00:27:20.362 [parallel-1] INFO reactor.Flux.FlatMap.1 - onNext(X)
     * 00:27:20.364 [parallel-1] INFO reactor.Flux.FlatMap.1 - onComplete()
     * ------------------------------------------------------------------------------------------------------------
     * It's the fastest one since you are not preserving the order.
     * ------------------------------------------------------------------------------------------------------------
     */
    public Flux<String> namesFluxFlatMapDelay(int nameFilter) {
        return Flux.fromIterable(NAMES)
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameFilter)
                // A, L, E, X, C, H, L, O, E
                // with a random delay element we can see onNext method running in parallel not keeping name words order
                .flatMap(name ->  Flux.fromArray(name.split(""))
                            .delayElements(ofMillis(parseLong(randomNumeric(3)))))
                .log();
    }

    /**
     * 00:26:37.443 [Test worker] INFO reactor.Flux.ConcatMap.1 - onSubscribe(FluxConcatMap.ConcatMapImmediate)
     * 00:26:37.449 [Test worker] INFO reactor.Flux.ConcatMap.1 - request(unbounded)
     * 00:26:38.402 [parallel-1] INFO reactor.Flux.ConcatMap.1 - onNext(A)
     * 00:26:39.260 [parallel-2] INFO reactor.Flux.ConcatMap.1 - onNext(L)
     * 00:26:40.120 [parallel-3] INFO reactor.Flux.ConcatMap.1 - onNext(E)
     * 00:26:40.978 [parallel-4] INFO reactor.Flux.ConcatMap.1 - onNext(X)
     * 00:26:41.918 [parallel-1] INFO reactor.Flux.ConcatMap.1 - onNext(C)
     * 00:26:42.856 [parallel-2] INFO reactor.Flux.ConcatMap.1 - onNext(H)
     * 00:26:43.796 [parallel-3] INFO reactor.Flux.ConcatMap.1 - onNext(L)
     * 00:26:44.738 [parallel-4] INFO reactor.Flux.ConcatMap.1 - onNext(O)
     * 00:26:45.680 [parallel-1] INFO reactor.Flux.ConcatMap.1 - onNext(E)
     * 00:26:45.681 [parallel-1] INFO reactor.Flux.ConcatMap.1 - onComplete()
     * ------------------------------------------------------------------------------------------------------------
     * concatMa()
     * Works similar to flatMap().
     * Only difference is that concatMap() preserves the ordering sequence of the Reactive Streams
     * It will take a lot of time to do.
     * ------------------------------------------------------------------------------------------------------------
     */
    public Flux<String> namesFluxFlatMapConcatMap(int nameFilter) {
        return Flux.fromIterable(NAMES)
                .map(String::toUpperCase)
                .filter(name -> name.length() > nameFilter)
                // A, L, E, X, C, H, L, O, E
                .concatMap(name -> Flux.fromArray(name.split(""))
                        .delayElements(ofMillis(parseLong(randomNumeric(3)))))
                .log();
    }

    /**
     * 00:25:19.800 [Test worker] INFO reactor.Mono.FlatMap.1 - | onSubscribe([Fuseable] MonoFlatMap.FlatMapMain)
     * 00:25:19.808 [Test worker] INFO reactor.Mono.FlatMap.1 - | request(unbounded)
     * 00:25:19.812 [Test worker] INFO reactor.Mono.FlatMap.1 - | onNext([A, L, E, X])
     * 00:25:19.816 [Test worker] INFO reactor.Mono.FlatMap.1 - | onComplete()
     */
    public Mono<List<String>> namesMonoFlatMapFilter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                .flatMap(name -> Mono.just(List.of(name.split(""))))
                .log();
    }

    /**
     * 00:26:11.372 [Test worker] INFO reactor.Flux.MonoFlatMapMany.1 - onSubscribe(MonoFlatMapMany.FlatMapManyMain)
     * 00:26:11.378 [Test worker] INFO reactor.Flux.MonoFlatMapMany.1 - request(unbounded)
     * 00:26:11.380 [Test worker] INFO reactor.Flux.MonoFlatMapMany.1 - onNext(A)
     * 00:26:11.380 [Test worker] INFO reactor.Flux.MonoFlatMapMany.1 - onNext(L)
     * 00:26:11.380 [Test worker] INFO reactor.Flux.MonoFlatMapMany.1 - onNext(E)
     * 00:26:11.380 [Test worker] INFO reactor.Flux.MonoFlatMapMany.1 - onNext(X)
     * 00:26:11.381 [Test worker] INFO reactor.Flux.MonoFlatMapMany.1 - onComplete()
     */
    public Flux<String> namesMonoFlatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(name -> name.length() > stringLength)
                // FLUX (A, L, E, X)
                .flatMapMany(name -> Flux.fromArray(name.split("")))
                .log();
    }

    /**
     * 00:59:48.118 [Test worker] INFO reactor.Flux.FlatMap.1 - onSubscribe(FluxFlatMap.FlatMapMain)
     * 00:59:48.123 [Test worker] INFO reactor.Flux.FlatMap.1 - request(unbounded)
     * 00:59:48.126 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(A)
     * 00:59:48.127 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(L)
     * 00:59:48.127 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(E)
     * 00:59:48.127 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(X)
     * 00:59:48.128 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(C)
     * 00:59:48.128 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(H)
     * 00:59:48.128 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(L)
     * 00:59:48.128 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(O)
     * 00:59:48.128 [Test worker] INFO reactor.Flux.FlatMap.1 - onNext(E)
     * 00:59:48.129 [Test worker] INFO reactor.Flux.FlatMap.1 - onComplete()
     */
    public Flux<String> namesFluxTransform(int stringLength) {
        UnaryOperator<Flux<String>> filterMap =
                name -> name.map(String::toUpperCase)
                        .filter(s -> s.length() > stringLength);

        return Flux.fromIterable(NAMES)
                .transform(filterMap)
                //.map(String::toUpperCase)
                //.filter(name -> name.length() > stringLength)
                // FLUX (A, L, E, X)
                .flatMap(name -> Flux.fromArray(name.split("")))
                .log();
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux().subscribe(name -> System.out.println("Flux Name is: " + name));
        fluxAndMonoGeneratorService.nameMono().subscribe(name -> System.out.println("Mono Name is: " + name));
    }
}
