package pro.chenggang.project.reactive.reactive_stream.generate;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.FluxSink;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: chenggang
 * @date 2020-09-15.
 */
@Slf4j
public class FluxCreator {

    private ExecutorService executorService = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setNameFormat("single-executor-%d").build());

    public void launch(FluxSink<Integer> integerFluxSink, int count) {
        this.executorService.submit(() -> {
            AtomicInteger integer = new AtomicInteger();
            Objects.requireNonNull(integerFluxSink);
            while (integer.get() < count) {
                double random = Math.random();
                integerFluxSink.next(integer.incrementAndGet());
                this.sleep((long) (random * 1_000));
            }
            integerFluxSink.complete();
        });
    }

    public  void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (Exception e) {
            log.error("Error:{}",e);
        }
    }

    public void shutdown(){
        executorService.shutdown();
    }
}
