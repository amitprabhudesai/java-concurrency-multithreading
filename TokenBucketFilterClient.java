import java.util.HashSet;
import java.util.Set;

/** 
 * Token bucket filter test client.
 */
public class TokenBucketFilterClient {

    public static void main(String[] args) throws InterruptedException {
        TokenBucketFilter filter = new TokenBucketFilter(1);
        DaemonTokenBucketFilter dtbf = new DaemonTokenBucketFilter(1);
        Set<Thread> allThreads = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //filter.getToken();
                        dtbf.getToken();
                    } catch (InterruptedException ie) {
                        // ignore, for now
                    }
                }
            }); 
            thread.setName("thread_" + i+1);
            allThreads.add(thread);
        }

        for (Thread t : allThreads) t.start();
        for (Thread t : allThreads) t.join();
    }
}