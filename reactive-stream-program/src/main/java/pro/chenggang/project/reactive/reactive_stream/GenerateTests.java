package pro.chenggang.project.reactive.reactive_stream;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author: chenggang
 * @date 2020-09-14.
 */
public class GenerateTests {

    public static void main (String[] args){
        generateFromSupplier();
    }

    private static void generateFromSupplier(){
        AtomicInteger integer = new AtomicInteger();
        Supplier<Integer> supplier = integer::incrementAndGet;
        Flux<Integer> integerFlux = Flux.fromStream(Stream.generate(supplier));
        Duration duration = StepVerifier.create(integerFlux.take(3))
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .verifyComplete();
        System.out.println(duration);
    }
}
