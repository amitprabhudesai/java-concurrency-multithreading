public class DaemonTokenBucketFilter {

    private final int maxtokens;
    private int tokens; 

    public DaemonTokenBucketFilter(int maxtokens) {
        this.maxtokens = maxtokens;
        this.tokens = 0;
        init();
    }

    void init() {
        Thread dt = new Thread(() -> {
           daemonThread(); 
        }); 
        dt.setDaemon(true);
        dt.start();
    }

    private void daemonThread() {
        while (true) {
            synchronized (this) {
                if (tokens < maxtokens) tokens++;
                this.notify();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                // ignore, for now
            }
        }
    }

    public void getToken() throws InterruptedException {
        synchronized (this) {
            while (0 == tokens) wait();
            tokens--;
        }
        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + 
            (System.currentTimeMillis()/1000L));            
    }
}
