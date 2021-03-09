public class Main {

    static volatile boolean cash = false;
    public static int account = 100;
    public static Object key = new Object();

    public static void main(String[] args) {
        System.out.println("Ваш счет в банке равен " + account);
        Thread refill = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (key) {
                    int random = (int) (Math.random() * 100);
                    while (!cash) {
                        account += random;
                        System.out.println("Ваш счет в банке равен " + account);
                        try {
                            Thread.sleep(1000);
                            key.notify();
                            key.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Thread withdrawal = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (key) {
                    int random = (int) (Math.random() * 100);
                    while (!cash) {
                        if (account > 0) {
                            account -= random;
                            System.out.println("Ваш счет в банке равен " + account);
                        } else System.out.println("Недостаточно средств, ваш баланс равен " + account);
                        try {
                            Thread.sleep(1000);
                            key.notify();
                            key.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        refill.start();
        withdrawal.start();
    }
}
