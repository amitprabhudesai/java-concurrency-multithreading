import java.util.LinkedList;

public class CountingSemaphoreClient {

    public static void main(String[] args) throws InterruptedException {

        CountingSemaphore semaphore = new CountingSemaphore(5);
        LinkedList<Integer> holder = new LinkedList<>();
        Thread t1 = new Thread(new Runnable() {

            public void run() {
                try {
                    for (int i = 0; i < 20; i++) {
                        semaphore.acquire();
                        holder.add(i);
                        System.out.println("Producer emitted " + i);
                    }
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread t2 = new Thread(new Runnable() {

            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        semaphore.release();
                        System.out.println("Consumer consumed " + holder.removeFirst());
                    }
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread t3 = new Thread(new Runnable() {

            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        semaphore.release();
                        System.out.println("Consumer consumed " + holder.removeFirst());
                    }
                } catch (InterruptedException ie) {

                }
            }
        });

        t1.start();
        // Thread.sleep(4000);
        t2.start();
        t2.join();

        t3.start();
        t1.join();
        t3.join();

    }
    
}
