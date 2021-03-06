package pro.chenggang.project.reactive.java_async.async_file;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: chenggang
 * @date 2020-09-11.
 */
public class Entrypoint {

    public static void main(String[] args) throws Exception{
        String fileName = Entrypoint.class.getClassLoader().getResource("cdp_ma_tags.sql").getFile();
        AtomicBoolean isFinished = new AtomicBoolean(false);
        AsyncFileCompletionHandler asyncFileCompletionHandler = new AsyncFileCompletionHandler(
                bytes -> {
                    System.out.println("------"+Thread.currentThread().getName()+"------Read Content----");
                    System.out.println(new String(bytes.getBytes(), StandardCharsets.UTF_8));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println();
                },
                ()-> {
                    isFinished.compareAndSet(false,true);
                    System.out.println(Thread.currentThread().getName()+" --->  Done");
                });
        AsyncFileReader asyncFileReader = new AsyncFileReader(asyncFileCompletionHandler);
        asyncFileReader.read(new File(fileName));
        /*
         * 这里关闭异步文件读取
         * 必须等到文件读取结束后才能进行
         * 这里不可以只关闭FileChannel，还要关闭ExecutorService ,否则异步线程池会一直阻塞WAITING状态
         */
        while (true){
            if(isFinished.get()){
                asyncFileReader.finished();
                break;
            }
        }
    }
}
