import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

//import static sun.jvm.hotspot.runtime.PerfMemory.start;

/**
 * Организуем гонки:
 * 1. Все участники должны стартовать одновременно, несмотря на то, что на подготовку
 * у каждого из них уходит разное время.
 *
 * 2. В туннель не может заехать одновременно больше половины участников (условность).
 *
 * 3. Попробуйте всё это синхронизировать.
 *
 * 4. Только после того как все завершат гонку, нужно выдать объявление об окончании.
 * Можете корректировать классы (в т.ч. конструктор машин)
 * и добавлять объекты классов из пакета util.concurrent.
 */


public class MainClass {

    public static final int CARS_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        CountDownLatch cdl = new CountDownLatch(CARS_COUNT);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT + 1);
        CyclicBarrier cb = new CyclicBarrier(CARS_COUNT+1);
        for (int i = 0; i < cars.length; i++) {
            new Thread(new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier, cb)).start();

        }
        try {
            cyclicBarrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }


        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            cb.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

