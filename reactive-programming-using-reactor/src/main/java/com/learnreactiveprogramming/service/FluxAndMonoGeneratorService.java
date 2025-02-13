package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.lang.Long.parseLong;
import static java.time.Duration.ofMillis;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class FluxAndMonoGeneratorService {

    public static final List<String> NAMES = List.of("alex", "ben", "chloe");

    /**
     * 01:17:23.976 [main] INFO reactor.Flux.Iterable.1 - | onSubscribe([Synchronous Fuseable] FluxIterable.IterableSubscription)
     * 01:17:23.982 [main] INFO reactor.Flux.Iterable.1 - | request(unbounded)
     * 01:17:23.983 [main] INFO reactor.Flux.Iterable.1 - | onNext(alex)
     * Flux Name is: alex
     * 01:17:24.016 [main] INFO reactor.Flux.Iterable.1 - | onNext(ben)
     * Flux Name is: ben
     * 01:17:24.016 [main] INFO reactor.Flux.Iterable.1 - | onNext(chloe)
     * Flux Name is: chloe
     * 01:17:24.018 [main] INFO reactor.Flux.Iterable.1 - | onComplete()
     */
    public Flux<String> namesFlux() {
        return Flux.fromIterable(NAMES).log();
    }

    /**
     * 01:17:24.169 [main] INFO reactor.Mono.Just.2 - | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)
     * 01:17:24.171 [main] INFO reactor.Mono.Just.2 - | request(unbounded)
     * 01:17:24.171 [main] INFO reactor.Mono.Just.2 - | onNext(alex)
     * Mono Name is: alex
     * 01:17:24.172 [main] INFO reactor.Mono.Just.2 - | onComplete()
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
     * ------------------------------------------------------------------------------------------------------------
     * This pickup the names and iterates for each name in onNext, applying the uppercase in each name.
     * ------------------------------------------------------------------------------------------------------------
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
     * ------------------------------------------------------------------------------------------------------------
     * This pickup the names and iterates for each name in onNext, the uppercase happens after and don't apply the
     * changes since the flux is immutable.
     * ------------------------------------------------------------------------------------------------------------
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
     * ------------------------------------------------------------------------------------------------------------
     * This split the List.of(NAMES) filter the size name to print the names in onNext
     * ------------------------------------------------------------------------------------------------------------
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
     * ------------------------------------------------------------------------------------------------------------
     * It's the fastest one since you are not preserving the order of non-blocking calls.
     * ------------------------------------------------------------------------------------------------------------
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
     * The delay will simulate the time between the concurrency non-blocking calls.
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
     * ------------------------------------------------------------------------------------------------------------
     * Create a List in a single result
     * Mono -> flatMap -> Mono -> (input alex | output ([A, L, E, X])
     * ------------------------------------------------------------------------------------------------------------
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
     * ------------------------------------------------------------------------------------------------------------
     * Mono -> flatMapMay -> Flux (input alex | output (A L E X)
     * ------------------------------------------------------------------------------------------------------------
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
     * ------------------------------------------------------------------------------------------------------------
     * Transform using Function UnaryOperator<T> implements Function<T,T>
     * Flux -> flatMap -> FLux -> (input List.of(names) | output A L E X C H L O E
     * In case
     * ------------------------------------------------------------------------------------------------------------
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
                .defaultIfEmpty("default")
                .log();
    }

    /**
     * 01:32:18.937 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onSubscribe(FluxSwitchIfEmpty.SwitchIfEmptySubscriber)
     * 01:32:18.944 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - request(unbounded)
     * 01:32:18.959 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onNext(D)
     * 01:32:18.959 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onNext(E)
     * 01:32:18.959 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onNext(F)
     * 01:32:18.960 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onNext(A)
     * 01:32:18.960 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onNext(U)
     * 01:32:18.960 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onNext(L)
     * 01:32:18.960 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onNext(T)
     * 01:32:18.961 [Test worker] INFO reactor.Flux.SwitchIfEmpty.1 - onComplete()
     */
    public Flux<String> namesFluxTransformSwitchIfEmpty(int stringLength) {
        UnaryOperator<Flux<String>> filterMap =
                name -> name.map(String::toUpperCase) // default to DEFAULT
                        // filter List.of(NAMES) there is any with length is greater than the param
                        .filter(s -> s.length() > stringLength)
                        // split D E F A U L T put in a flux array
                        .flatMap(s -> Flux.fromArray(s.split("")));

        // D, E, F, A, U, L, T
        var defaultFlux = Flux.just("default").transform(filterMap);

        return Flux.fromIterable(NAMES)
                .transform(filterMap) // reuse the function unary created.
                .switchIfEmpty(defaultFlux) // if empty return the defaultFlux
                .log();
    }

    /**
     * 01:48:37.168 [Test worker] INFO reactor.Flux.ConcatArray.1 - onSubscribe(FluxConcatArray.ConcatArraySubscriber)
     * 01:48:37.179 [Test worker] INFO reactor.Flux.ConcatArray.1 - request(unbounded)
     * 01:48:37.183 [Test worker] INFO reactor.Flux.ConcatArray.1 - onNext(A)
     * 01:48:37.184 [Test worker] INFO reactor.Flux.ConcatArray.1 - onNext(B)
     * 01:48:37.185 [Test worker] INFO reactor.Flux.ConcatArray.1 - onNext(C)
     * 01:48:37.185 [Test worker] INFO reactor.Flux.ConcatArray.1 - onNext(D)
     * 01:48:37.186 [Test worker] INFO reactor.Flux.ConcatArray.1 - onNext(E)
     * 01:48:37.186 [Test worker] INFO reactor.Flux.ConcatArray.1 - onNext(F)
     * 01:48:37.188 [Test worker] INFO reactor.Flux.ConcatArray.1 - onComplete()
     */
    public Flux<String> exploreConcat() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux).log();
    }

    /**
     * concatenation of reactive streams happens in a sequence
     * first one is subscribed first and completes
     * second one is subscribed after that and completes
     */
    public Flux<String> exploreConcatWith() {
        final var aFlux = Mono.just("A");
        final var bFlux = Mono.just("B");
        final var cdfFlux = Flux.just("C", "D", "E", "F");

        return aFlux.concatWith(
                bFlux.concatWith(cdfFlux))
                .log();
    }

    /**
     * 02:03:26.139 [Test worker] INFO reactor.Flux.ConcatArray.1 - onSubscribe(FluxConcatArray.ConcatArraySubscriber)
     * 02:03:26.147 [Test worker] INFO reactor.Flux.ConcatArray.1 - request(unbounded)
     * 02:03:26.316 [parallel-1] INFO reactor.Flux.ConcatArray.1 - onNext(A)
     * 02:03:26.419 [parallel-2] INFO reactor.Flux.ConcatArray.1 - onNext(B)
     * 02:03:26.520 [parallel-3] INFO reactor.Flux.ConcatArray.1 - onNext(C)
     * 02:03:26.550 [parallel-4] INFO reactor.Flux.ConcatArray.1 - onNext(D)
     * 02:03:26.581 [parallel-1] INFO reactor.Flux.ConcatArray.1 - onNext(E)
     * 02:03:26.614 [parallel-2] INFO reactor.Flux.ConcatArray.1 - onNext(F)
     * 02:03:26.615 [parallel-2] INFO reactor.Flux.ConcatArray.1 - onComplete()
     * ------------------------------------------------------------------------------------------------------------
     * The order of the leathers show this is concurrent process
     * ------------------------------------------------------------------------------------------------------------
     */
    public Flux<String> exploreMerge() {
        final var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100)); //
        final var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(25)); //

        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> exploreMergeWith() {
        final var aFlux = Flux.just("X", "W", "X", "Z", "A");
        final var bFlux = Mono.just("B");
        final var cdfFlux = Flux.just("C", "A", "E", "F");

        return aFlux.mergeWith(bFlux.mergeWith(cdfFlux)).log();
    }

    /**
     * 02:15:20.208 [Test worker] INFO reactor.Flux.MergeSequential.1 - onSubscribe(FluxMergeSequential.MergeSequentialMain)
     * 02:15:20.213 [Test worker] INFO reactor.Flux.MergeSequential.1 - request(unbounded)
     * 02:15:20.368 [parallel-1] INFO reactor.Flux.MergeSequential.1 - onNext(A)
     * 02:15:20.474 [parallel-1] INFO reactor.Flux.MergeSequential.1 - onNext(B)
     * 02:15:20.580 [parallel-2] INFO reactor.Flux.MergeSequential.1 - onNext(C)
     * 02:15:20.580 [parallel-2] INFO reactor.Flux.MergeSequential.1 - onNext(D)
     * 02:15:20.581 [parallel-2] INFO reactor.Flux.MergeSequential.1 - onNext(E)
     * 02:15:20.581 [parallel-2] INFO reactor.Flux.MergeSequential.1 - onNext(F)
     * 02:15:20.582 [parallel-2] INFO reactor.Flux.MergeSequential.1 - onComplete()
     * ------------------------------------------------------------------------------------------------------------
     * The Flux execute at the same time but merge in a sequence so the order is keep.
     * ------------------------------------------------------------------------------------------------------------
     */
    public Flux<String> exploreMergeSeq() {
        final var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100)); //
        final var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(25)); //

        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    /**
     * 23:18:20.316 [Test worker] INFO reactor.Flux.Map.1 - onSubscribe(FluxMap.MapSubscriber)
     * 23:18:20.360 [Test worker] INFO reactor.Flux.Map.1 - request(unbounded)
     * 23:18:20.404 [Test worker] INFO reactor.Flux.Map.1 - onNext(AD)
     * 23:18:20.406 [Test worker] INFO reactor.Flux.Map.1 - onNext(BE)
     * 23:18:20.407 [Test worker] INFO reactor.Flux.Map.1 - onNext(CF)
     * 23:18:20.417 [Test worker] INFO reactor.Flux.Map.1 - onComplete()
     */
    public Flux<String> exploreZip() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux).map(a -> a.getT1() + a.getT2()).log();
    }

    /**
     * 23:22:55.625 [Test worker] INFO reactor.Flux.Map.1 - onSubscribe(FluxMap.MapSubscriber)
     * 23:22:55.644 [Test worker] INFO reactor.Flux.Map.1 - request(unbounded)
     * 23:22:55.712 [Test worker] INFO reactor.Flux.Map.1 - onNext(AD14)
     * 23:22:55.712 [Test worker] INFO reactor.Flux.Map.1 - onNext(BE25)
     * 23:22:55.713 [Test worker] INFO reactor.Flux.Map.1 - onNext(CF36)
     * 23:22:55.714 [Test worker] INFO reactor.Flux.Map.1 - onComplete()
     */
    public Flux<String> exploreZip1() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");
        final var flux123 = Flux.just("1", "2", "3");
        final var flux456 = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, flux123, flux456)
                .map(a -> a.getT1() + a.getT2() + a.getT3() + a.getT4())
                .log();
    }

    /**
     * 23:28:55.044 [Test worker] INFO reactor.Flux.Zip.1 - onSubscribe(FluxZip.ZipCoordinator)
     * 23:28:55.055 [Test worker] INFO reactor.Flux.Zip.1 - request(unbounded)
     * 23:28:55.087 [Test worker] INFO reactor.Flux.Zip.1 - onNext(AD)
     * 23:28:55.087 [Test worker] INFO reactor.Flux.Zip.1 - onNext(BE)
     * 23:28:55.088 [Test worker] INFO reactor.Flux.Zip.1 - onNext(CF)
     * 23:28:55.090 [Test worker] INFO reactor.Flux.Zip.1 - onComplete()
     */
    public Flux<String> exploreZipWithFlux() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second)
                .log();
    }

    /**
     * 23:29:22.598 [Test worker] INFO reactor.Mono.Map.1 - onSubscribe(FluxMap.MapSubscriber)
     * 23:29:22.606 [Test worker] INFO reactor.Mono.Map.1 - request(unbounded)
     * 23:29:22.631 [Test worker] INFO reactor.Mono.Map.1 - onNext(AD)
     * 23:29:22.633 [Test worker] INFO reactor.Mono.Map.1 - onComplete()
     */
    public Mono<String> exploreZipWithMono() {
        final var abcFlux = Mono.just("A");
        final var defFlux = Mono.just("D");

        return abcFlux.zipWith(defFlux)
                .map(a -> a.getT1() + a.getT2())
                .log();
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux().subscribe(name -> System.out.println("Flux Name is: " + name));
        fluxAndMonoGeneratorService.nameMono().subscribe(name -> System.out.println("Mono Name is: " + name));
    }
}
