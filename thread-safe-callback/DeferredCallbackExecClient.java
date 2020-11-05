import java.util.HashSet;
import java.util.Set;

public class DeferredCallbackExecClient {

    public static void main(String[] args) throws InterruptedException {
        Set<Thread> allThreads = new HashSet<>();
        final DeferredCallbackExecutor executor = new DeferredCallbackExecutor();
        Thread service = new Thread(() -> {
            try {
                executor.start();
            } catch (InterruptedException ie) {
                // ignore, for now
            }
        });
        service.start();
       
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                DeferredCallbackExecutor.Callback cb = new DeferredCallbackExecutor.Callback(1, "Hello, this is " + Thread.currentThread().getName());
                executor.registerCallback(cb);
            });
            thread.setName("thread_" + (i+1));
            thread.start();
            allThreads.add(thread);
            Thread.sleep(1000);
        }

        for (Thread t : allThreads) t.join();
    }
    
}
