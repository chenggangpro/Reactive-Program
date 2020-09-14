package pro.chenggang.project.reactive.java_async.async_file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import pro.chenggang.project.reactive.java_async.async_file.support.FileCopyUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

/**
 * @author: chenggang
 * @date 2020-09-11.
 */
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class AsyncFileCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private int bytesRead;
    private long position;
    private AsynchronousFileChannel fileChannel;
    private final Consumer<Bytes> consumer;
    private final Runnable finished;

    public void read(){
        ByteBuffer buffer = ByteBuffer.allocate(FileCopyUtils.BUFFER_SIZE);
        fileChannel.read(buffer, this.getPosition(), buffer, this);
    }

    @SneakyThrows
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        this.bytesRead = result;
        if (this.bytesRead < 0) {
            this.finished.run();
            return;
        }
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        consumer.accept(Bytes.from(data, data.length));
        buffer.clear();
        this.position = this.position + this.bytesRead;
        this.fileChannel.read(buffer, this.getPosition(), buffer, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error("Error --> ",exc);
    }

    @SneakyThrows
    public void close(){
        this.fileChannel.close();
    }
}
