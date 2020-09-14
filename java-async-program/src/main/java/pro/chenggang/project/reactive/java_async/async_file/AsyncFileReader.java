package pro.chenggang.project.reactive.java_async.async_file;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author: chenggang
 * @date 2020-09-11.
 */
@Slf4j
@RequiredArgsConstructor
public class AsyncFileReader {

    private ExecutorService executorService = new ScheduledThreadPoolExecutor(10, new ThreadFactoryBuilder().setNameFormat("async-file-executor-%d").build());
    private final AsyncFileCompletionHandler asyncFileCompletionHandler;

    public void read(File file) throws IOException{
        Path path = file.toPath();
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, Collections.singleton(StandardOpenOption.READ), this.executorService);
        this.asyncFileCompletionHandler.setFileChannel(fileChannel);
        this.asyncFileCompletionHandler.read();
    }

    public void finished(){
        this.executorService.shutdown();
    }
}
