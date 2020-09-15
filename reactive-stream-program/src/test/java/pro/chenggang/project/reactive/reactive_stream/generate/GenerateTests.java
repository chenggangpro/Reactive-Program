package pro.chenggang.project.reactive.reactive_stream.generate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class GenerateTests {

    @BeforeEach
    public void beforeEach(){
        Hooks.onOperatorDebug();
    }

    @Test
    public void generateFromSupplier(){
        AtomicInteger integer = new AtomicInteger();
        Supplier<Integer> supplier = integer::incrementAndGet;
        Flux<Integer> integerFlux = Flux.fromStream(Stream.generate(supplier)).log();
        StepVerifier.create(integerFlux.take(3))
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .verifyComplete();
    }

    @Test
    public void fluxCreate(){
        FluxCreator fluxCreator = new FluxCreator();
        Flux<Integer> integers = Flux.create(emitter -> fluxCreator.launch(emitter, 5));
        StepVerifier.create(integers.log().doFinally(signalType -> fluxCreator.shutdown()))
                .expectNextCount(5)
                .verifyComplete();
    }
}
