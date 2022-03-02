import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class MyTests {

    private static String[] expected = new String[120];
    private static String basicCube4x4 =
            "000000000000000011111111111111112222222222222222333333333333333344444444444444445555555555555555";

    private void makeExpected() {

        expected[0] = "000000000000000022221111111111113333222222222222444433333333333311114444444444445555555555555555";
        expected[1] = "000000000000000022222222111111113333333322222222444444443333333311111111444444445555555555555555";
        expected[2] = "000000000000000022222222222211113333333333332222444444444444333311111111111144445555555555555555";
        expected[3] = "000000000000000022222222222222223333333333333333444444444444444411111111111111115555555555555555";
        expected[4] = "100010001000100022222222222222220333033303330333444444444444444411151115111511153555355535553555";
        expected[5] = "110011001100110022222222222222220033003300330033444444444444444411551155115511553355335533553355";
        expected[6] = "111011101110111022222222222222220003000300030003444444444444444415551555155515553335333533353335";
        expected[7] = "111111111111111122222222222222220000000000000000444444444444444455555555555555553333333333333333";
        expected[8] = "111111111111222222232223222322230000000000000000144414441444144455555555555555554444333333333333";
        expected[9] = "111111112222222222332233223322330000000000000000114411441144114455555555555555554444444433333333";
        expected[10] = "111122222222222223332333233323330000000000000000111411141114111455555555555555554444444444443333";
        expected[11] = "222222222222222233333333333333330000000000000000111111111111111155555555555555554444444444444444";
        expected[12] = "222022202220222033333333333333330004000400040004111111111111111125552555255525554445444544454445";
        expected[13] = "220022002200220033333333333333330044004400440044111111111111111122552255225522554455445544554455";
        expected[14] = "200020002000200033333333333333330444044404440444111111111111111122252225222522254555455545554555";
        expected[15] = "000000000000000033333333333333334444444444444444111111111111111122222222222222225555555555555555";
        expected[16] = "111100000000000003330333033303334444444444444444111511151115111522222222222222225555555555553333";
        expected[17] = "111111110000000000330033003300334444444444444444115511551155115522222222222222225555555533333333";
        expected[18] = "111111111111000000030003000300034444444444444444155515551555155522222222222222225555333333333333";
        expected[19] = "111111111111111100000000000000004444444444444444555555555555555522222222222222223333333333333333";
        expected[20] = "111111111111111100000000000022224444444444440000555555555555444422222222222255553333333333333333";
        expected[21] = "111111111111111100000000222222224444444400000000555555554444444422222222555555553333333333333333";
        expected[22] = "111111111111111100002222222222224444000000000000555544444444444422225555555555553333333333333333";
        expected[23] = "111111111111111122222222222222220000000000000000444444444444444455555555555555553333333333333333";
        expected[24] = "111111111111111100002222222222224444000000000000555544444444444422225555555555553333333333333333";
        expected[25] = "111111111111111100000000222222224444444400000000555555554444444422222222555555553333333333333333";
        expected[26] = "111111111111111100000000000022224444444444440000555555555555444422222222222255553333333333333333";
        expected[27] = "111111111111111100000000000000004444444444444444555555555555555522222222222222223333333333333333";
        expected[28] = "211121112111211100000000000000001444144414441444555555555555555522232223222322234333433343334333";
        expected[29] = "221122112211221100000000000000001144114411441144555555555555555522332233223322334433443344334433";
        expected[30] = "222122212221222100000000000000001114111411141114555555555555555523332333233323334443444344434443";
        expected[31] = "222222222222222200000000000000001111111111111111555555555555555533333333333333334444444444444444";
        expected[32] = "222222222222000000040004000400041111111111111111255525552555255533333333333333335555444444444444";
        expected[33] = "222222220000000000440044004400441111111111111111225522552255225533333333333333335555555544444444";
        expected[34] = "222200000000000004440444044404441111111111111111222522252225222533333333333333335555555555554444";
        expected[35] = "000000000000000044444444444444441111111111111111222222222222222233333333333333335555555555555555";
        expected[36] = "000100010001000144444444444444441115111511151115222222222222222203330333033303335553555355535553";
        expected[37] = "001100110011001144444444444444441155115511551155222222222222222200330033003300335533553355335533";
        expected[38] = "011101110111011144444444444444441555155515551555222222222222222200030003000300035333533353335333";
        expected[39] = "111111111111111144444444444444445555555555555555222222222222222200000000000000003333333333333333";
        expected[40] = "222211111111111114441444144414445555555555555555222322232223222300000000000000003333333333334444";
        expected[41] = "222222221111111111441144114411445555555555555555223322332233223300000000000000003333333344444444";
        expected[42] = "222222222222111111141114111411145555555555555555233323332333233300000000000000003333444444444444";
        expected[43] = "222222222222222211111111111111115555555555555555333333333333333300000000000000004444444444444444";
        expected[44] = "222222222222222211111111111100005555555555551111333333333333555500000000000033334444444444444444";
        expected[45] = "222222222222222211111111000000005555555511111111333333335555555500000000333333334444444444444444";
        expected[46] = "222222222222222211110000000000005555111111111111333355555555555500003333333333334444444444444444";
        expected[47] = "222222222222222200000000000000001111111111111111555555555555555533333333333333334444444444444444";
        expected[48] = "222222222222222211110000000000005555111111111111333355555555555500003333333333334444444444444444";
        expected[49] = "222222222222222211111111000000005555555511111111333333335555555500000000333333334444444444444444";
        expected[50] = "222222222222222211111111111100005555555555551111333333333333555500000000000033334444444444444444";
        expected[51] = "222222222222222211111111111111115555555555555555333333333333333300000000000000004444444444444444";
        expected[52] = "022202220222022211111111111111112555255525552555333333333333333300040004000400045444544454445444";
        expected[53] = "002200220022002211111111111111112255225522552255333333333333333300440044004400445544554455445544";
        expected[54] = "000200020002000211111111111111112225222522252225333333333333333304440444044404445554555455545554";
        expected[55] = "000000000000000011111111111111112222222222222222333333333333333344444444444444445555555555555555";
        expected[56] = "000000000000111111151115111511152222222222222222033303330333033344444444444444443333555555555555";
        expected[57] = "000000001111111111551155115511552222222222222222003300330033003344444444444444443333333355555555";
        expected[58] = "000011111111111115551555155515552222222222222222000300030003000344444444444444443333333333335555";
        expected[59] = "111111111111111155555555555555552222222222222222000000000000000044444444444444443333333333333333";
        expected[60] = "111211121112111255555555555555552223222322232223000000000000000014441444144414443334333433343334";
        expected[61] = "112211221122112255555555555555552233223322332233000000000000000011441144114411443344334433443344";
        expected[62] = "122212221222122255555555555555552333233323332333000000000000000011141114111411143444344434443444";
        expected[63] = "222222222222222255555555555555553333333333333333000000000000000011111111111111114444444444444444";
        expected[64] = "000022222222222225552555255525553333333333333333000400040004000411111111111111114444444444445555";
        expected[65] = "000000002222222222552255225522553333333333333333004400440044004411111111111111114444444455555555";
        expected[66] = "000000000000222222252225222522253333333333333333044404440444044411111111111111114444555555555555";
        expected[67] = "000000000000000022222222222222223333333333333333444444444444444411111111111111115555555555555555";
        expected[68] = "000000000000000022222222222211113333333333332222444444444444333311111111111144445555555555555555";
        expected[69] = "000000000000000022222222111111113333333322222222444444443333333311111111444444445555555555555555";
        expected[70] = "000000000000000022221111111111113333222222222222444433333333333311114444444444445555555555555555";
        expected[71] = "000000000000000011111111111111112222222222222222333333333333333344444444444444445555555555555555";
        expected[72] = "000000000000000022221111111111113333222222222222444433333333333311114444444444445555555555555555";
        expected[73] = "000000000000000022222222111111113333333322222222444444443333333311111111444444445555555555555555";
        expected[74] = "000000000000000022222222222211113333333333332222444444444444333311111111111144445555555555555555";
        expected[75] = "000000000000000022222222222222223333333333333333444444444444444411111111111111115555555555555555";
        expected[76] = "100010001000100022222222222222220333033303330333444444444444444411151115111511153555355535553555";
        expected[77] = "110011001100110022222222222222220033003300330033444444444444444411551155115511553355335533553355";
        expected[78] = "111011101110111022222222222222220003000300030003444444444444444415551555155515553335333533353335";
        expected[79] = "111111111111111122222222222222220000000000000000444444444444444455555555555555553333333333333333";
        expected[80] = "111111111111222222232223222322230000000000000000144414441444144455555555555555554444333333333333";
        expected[81] = "111111112222222222332233223322330000000000000000114411441144114455555555555555554444444433333333";
        expected[82] = "111122222222222223332333233323330000000000000000111411141114111455555555555555554444444444443333";
        expected[83] = "222222222222222233333333333333330000000000000000111111111111111155555555555555554444444444444444";
        expected[84] = "222022202220222033333333333333330004000400040004111111111111111125552555255525554445444544454445";
        expected[85] = "220022002200220033333333333333330044004400440044111111111111111122552255225522554455445544554455";
        expected[86] = "200020002000200033333333333333330444044404440444111111111111111122252225222522254555455545554555";
        expected[87] = "000000000000000033333333333333334444444444444444111111111111111122222222222222225555555555555555";
        expected[88] = "111100000000000003330333033303334444444444444444111511151115111522222222222222225555555555553333";
        expected[89] = "111111110000000000330033003300334444444444444444115511551155115522222222222222225555555533333333";
        expected[90] = "111111111111000000030003000300034444444444444444155515551555155522222222222222225555333333333333";
        expected[91] = "111111111111111100000000000000004444444444444444555555555555555522222222222222223333333333333333";
        expected[92] = "111111111111111100000000000022224444444444440000555555555555444422222222222255553333333333333333";
        expected[93] = "111111111111111100000000222222224444444400000000555555554444444422222222555555553333333333333333";
        expected[94] = "111111111111111100002222222222224444000000000000555544444444444422225555555555553333333333333333";
        expected[95] = "111111111111111122222222222222220000000000000000444444444444444455555555555555553333333333333333";
        expected[96] = "111111111111111100002222222222224444000000000000555544444444444422225555555555553333333333333333";
        expected[97] = "111111111111111100000000222222224444444400000000555555554444444422222222555555553333333333333333";
        expected[98] = "111111111111111100000000000022224444444444440000555555555555444422222222222255553333333333333333";
        expected[99] = "111111111111111100000000000000004444444444444444555555555555555522222222222222223333333333333333";
        expected[100] = "211121112111211100000000000000001444144414441444555555555555555522232223222322234333433343334333";
        expected[101] = "221122112211221100000000000000001144114411441144555555555555555522332233223322334433443344334433";
        expected[102] = "222122212221222100000000000000001114111411141114555555555555555523332333233323334443444344434443";
        expected[103] = "222222222222222200000000000000001111111111111111555555555555555533333333333333334444444444444444";
        expected[104] = "222222222222000000040004000400041111111111111111255525552555255533333333333333335555444444444444";
        expected[105] = "222222220000000000440044004400441111111111111111225522552255225533333333333333335555555544444444";
        expected[106] = "222200000000000004440444044404441111111111111111222522252225222533333333333333335555555555554444";
        expected[107] = "000000000000000044444444444444441111111111111111222222222222222233333333333333335555555555555555";
        expected[108] = "000100010001000144444444444444441115111511151115222222222222222203330333033303335553555355535553";
        expected[109] = "001100110011001144444444444444441155115511551155222222222222222200330033003300335533553355335533";
        expected[110] = "011101110111011144444444444444441555155515551555222222222222222200030003000300035333533353335333";
        expected[111] = "111111111111111144444444444444445555555555555555222222222222222200000000000000003333333333333333";
        expected[112] = "222211111111111114441444144414445555555555555555222322232223222300000000000000003333333333334444";
        expected[113] = "222222221111111111441144114411445555555555555555223322332233223300000000000000003333333344444444";
        expected[114] = "222222222222111111141114111411145555555555555555233323332333233300000000000000003333444444444444";
        expected[115] = "222222222222222211111111111111115555555555555555333333333333333300000000000000004444444444444444";
        expected[116] = "222222222222222211111111111100005555555555551111333333333333555500000000000033334444444444444444";
        expected[117] = "222222222222222211111111000000005555555511111111333333335555555500000000333333334444444444444444";
        expected[118] = "222222222222222211110000000000005555111111111111333355555555555500003333333333334444444444444444";
        expected[119] = "222222222222222200000000000000001111111111111111555555555555555533333333333333334444444444444444";

    }

    private void startJoinThreads(Thread[] threads, int threadNumber) {

        for (int i = 0; i < threadNumber; i++)
            threads[i].start();

        for (int i = 0; i < threadNumber; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    Checks the rotations' correctness of a 4x4 cube for every side and layer
    sequentially.
    */
    @Test
    public void testCorrectness() {

        Cube cube = new Cube(4,
                (x, y) -> {},
                (x, y) -> {},
                () -> {},
                () -> {});

        int idx = 0;
        makeExpected();

        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 6; k++) {
                for (int l = 0; l < 4; l++) {
                    int finalK = k;
                    int finalL = l;
                    Assertions.assertDoesNotThrow(() -> {
                        cube.rotate(finalK, finalL);
                    });
                    AtomicReference<String> showResult = new AtomicReference<>();
                    Assertions.assertDoesNotThrow(() -> {
                        showResult.set(cube.show());
                    });
                    assertEquals(showResult.get(), expected[idx]);
                    idx++;
                }
            }
        }

    }

    /*
    Checks if rotations of different layers of the same side of a cube will
    be done concurrently. It is tested by looking at a time of a running test.
    */
    @Test
    public void testConcurrency() {

        Cube cube = new Cube(3,
                (x, y) -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                (x, y) -> {},
                () -> {},
                () -> {});

        Thread[] threads = new Thread[3];

        threads[0] = new Thread(() -> {
            try {
                cube.rotate(0, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threads[1] = new Thread(() -> {
            try {
                cube.rotate(0, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threads[2] = new Thread(() -> {
            try {
                cube.rotate(0, 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final long startTime = System.currentTimeMillis();

        startJoinThreads(threads,3);

        final long endTime = System.currentTimeMillis();

        // Program powinien się wykonać w lekko ponad 100 ms.
        assertTrue(endTime - startTime < 2 * 100);

    }

    /*
    Checks if threads working on the same side and layer of a cube are
    done sequentially.
    */
    @Test
    public void testConcurrencySameLayer() {

        var counter = new Object() { int value = 0; };

        Cube cube = new Cube(4,
                (x, y) -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ++counter.value; },
                (x, y) -> { ++counter.value; },
                () -> {},
                () -> {});

        int threadNumber = 12;
        Thread[] threads = new Thread[threadNumber];

        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new Thread(() -> {
                try {
                    cube.rotate(0, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        final long startTime = System.currentTimeMillis();

        startJoinThreads(threads, threadNumber);

        final long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime > threadNumber * 100);

        assertEquals(counter.value, threadNumber * 2);

        AtomicReference<String> showResult = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            showResult.set(cube.show());
        });
        assertEquals(showResult.get(), basicCube4x4);

    }

    /*
    Checks if there is no thread starvation while all threads make
    operations on different sides of the cube.
    */
    @RepeatedTest(50)
    public void testConcurrencyDifferentSides() {

        var counter = new Object() {
            AtomicInteger value = new AtomicInteger(0);
        };

        int cubeSize = 4;
        Cube cube = new Cube(cubeSize,
                (x, y) -> { counter.value.getAndIncrement(); },
                (x, y) -> { counter.value.getAndIncrement(); },
                () -> { counter.value.getAndIncrement(); },
                () -> { counter.value.getAndIncrement(); });

        int threadNumber = 6;
        Thread[] threads = new Thread[threadNumber];

        for (int i = 0; i < threadNumber; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int k = 0; k < cubeSize; k++) {
                        cube.rotate(finalI, k);
                    }
                    cube.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        startJoinThreads(threads, threadNumber);

        assertEquals(counter.value.get(),
                threadNumber * cubeSize * 2 + threadNumber * 2);

    }

    private Cube createNewCube(int cubeSize, List<Integer> nextRotations) {

        Cube cube1 = new Cube(cubeSize,
                (x, y) -> {},
                (x, y) -> {},
                () -> {},
                () -> {});

        int side, layer;
        try {
            synchronized(nextRotations) {
                for (int next : nextRotations) {
                    layer = next % 10;
                    side = (next - layer) / 10;
                    cube1.rotate(side, layer);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return cube1;

    }

    private void createRandomizedThreads(int threadNumber,
                                         int cubeSize,
                                         Cube cube, Thread[] threads) {

        Random random = new Random();

        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new Thread(() -> {
                try {
                    for (int k = 0; k < 1000; k++) {
                        int randomSide = random.nextInt(6);
                        int randomLayer = random.nextInt(cubeSize);
                        cube.rotate(randomSide, randomLayer);
                    }
                } catch (InterruptedException e) {
                    // do nothing
                }
            });
        }

    }

    /*
    Checks if concurrently working threads work properly (if sides that
    shouldn't be rotating at the same time are done one after another).
    */
    @RepeatedTest(10)
    public void testConcurrencyAndCorrectness() {

        int threadNumber = 15;
        List<Integer> nextRotations =
                Collections.synchronizedList(new ArrayList<>());

        int cubeSize = 5;
        Cube cube = new Cube(cubeSize,
                (x, y) -> {
                    nextRotations.add(10 * x + y);
                },
                (x, y) -> {},
                () -> {},
                () -> {});

        Thread[] threads = new Thread[threadNumber];
        createRandomizedThreads(threadNumber, cubeSize, cube, threads);

        startJoinThreads(threads, threadNumber);

        Cube cube1 = createNewCube(cubeSize, nextRotations);
        AtomicReference<String> showResult = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            showResult.set(cube.show());
        });
        AtomicReference<String> showResult1 = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> {
            showResult1.set(cube1.show());
        });
        assertEquals(showResult.get(), showResult1.get());

    }

    /*
    Checks if after interrupting a thread before a rotation is made,
    other threads will continue to work properly and if an interrupted
    thread won't make a rotation.
    */
    @Test
    public void testOneInterrupted() {

        var counter = new Object() {
            AtomicInteger value = new AtomicInteger(0);
        };

        int cubeSize = 3;
        Cube cube = new Cube(cubeSize,
                (x, y) -> { counter.value.getAndIncrement(); },
                (x, y) -> { counter.value.getAndIncrement(); },
                () -> {},
                () -> {});

        int threadNumber = 5;
        Thread[] threads = new Thread[threadNumber];
        threads[0] = new Thread(() -> {
            try {
                Thread.sleep(1000);
                cube.rotate(0, 0);
            } catch (InterruptedException e) {
                // do nothing
            }
        });

        threads[0].start();

        int rotateNumber = 3;

        for (int i = 1; i < threadNumber; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int k = 0; k < rotateNumber; k++)
                        cube.rotate(finalI, k);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (int i = 1; i < threadNumber; i++)
            threads[i].start();

        threads[0].interrupt();

        for (int i = 0; i < threadNumber; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertEquals(counter.value.get(),
                (threadNumber - 1) * rotateNumber * 2);

    }

    /*
    Checks if a thread was interrupted.
    */
    @Test
    public void testInterruption() {

        int cubeSize = 3;
        Cube cube = new Cube(cubeSize,
                (x, y) -> {},
                (x, y) -> {},
                () -> {},
                () -> {});

        int threadNumber = 10;
        Thread[] threads = new Thread[threadNumber];
        createRandomizedThreads(threadNumber, cubeSize, cube, threads);

        for (int i = 0; i < threadNumber; i++)
            threads[i].start();

        for (int i = 0; i < threadNumber; i++)
            threads[i].interrupt();

    }

}
