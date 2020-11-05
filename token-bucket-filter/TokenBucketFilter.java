public class TokenBucketFilter {

    private final int maxtokens;
    private long lastRequestTime;
    private long tokens;

    public TokenBucketFilter(int maxtokens) {
        this.maxtokens = maxtokens;
        this.lastRequestTime = System.currentTimeMillis();
        this.tokens = 0;
    }

    public synchronized void getToken() throws InterruptedException {
        tokens += (System.currentTimeMillis() - lastRequestTime)/1000L;
        tokens = Math.min(tokens, maxtokens);
        if (0 == tokens) {
            Thread.sleep(1000);
        } else {
            tokens--;
        }

        lastRequestTime = System.currentTimeMillis();
        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + 
            (System.currentTimeMillis()/1000L));
    }
    
}
