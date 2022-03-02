import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;

public class Cube {

    private int size;
    private BiConsumer<Integer, Integer> beforeRotation;
    private BiConsumer<Integer, Integer> afterRotation;
    private Runnable beforeShowing;
    private Runnable afterShowing;

    // First dimension indicates a layer, second dimension a position in a layer
    // and third dimension indicates a side of a cube.
    private int[][][] cube;

    private int currentGroup = -1;
    private int inCriticalSectionCounter = 0;
    private int waitingCounter = 0;
    private int[] waitingGroupCounter = {0, 0, 0, 0};

    private final Semaphore barrier = new Semaphore(1, true);
    private final Semaphore[] group = new Semaphore[4];
    private final Semaphore[] takenLayer;

    public Cube(int size,
                BiConsumer<Integer, Integer> beforeRotation,
                BiConsumer<Integer, Integer> afterRotation,
                Runnable beforeShowing,
                Runnable afterShowing) {

        this.size = size;
        this.cube = new int[size][size][6];
        initializeCube();
        this.beforeRotation = beforeRotation;
        this.afterRotation = afterRotation;
        this.beforeShowing = beforeShowing;
        this.afterShowing = afterShowing;

        for (int i = 0; i < 4; i++) {
            Semaphore s = new Semaphore(0, true);
            group[i] = s;
        }

        takenLayer = new Semaphore[size];
        for (int i = 0; i < size; i++) {
            Semaphore s = new Semaphore(1, true);
            takenLayer[i] = s;
        }

    }

    private void rotateSide(int side) {

        for (int i = 0; i < size / 2; i++) {
            for (int k = i; k < size - i - 1; k++) {
                int helper = cube[i][k][side];
                cube[i][k][side] = cube[size - 1 - k][i][side];
                cube[size - 1 - k][i][side] =
                        cube[size - 1 - i][size - 1 - k][side];
                cube[size - 1 - i][size - 1 - k][side] =
                        cube[k][size - 1 - i][side];
                cube[k][size - 1 - i][side] = helper;
            }
        }

    }

    private void checkRotateSide(int side, int layer) {

        if (layer == 0)
            rotateSide(side);
        else if (layer == size - 1) {
            for (int i = 0; i < 3; i++) {
                switch (side) {
                    case 0:
                        rotateSide(5);
                        break;
                    case 1:
                        rotateSide(3);
                        break;
                    case 2:
                        rotateSide(4);
                        break;
                    case 3:
                        rotateSide(1);
                        break;
                    case 4:
                        rotateSide(2);
                        break;
                    default:
                        rotateSide(0);
                }
            }
        }

    }

    private void rotateSideUp(int layer) {

        for (int i = 0; i < size; i++) {
            int helper = cube[layer][i][1];
            cube[layer][i][1] = cube[layer][i][2];
            cube[layer][i][2] = cube[layer][i][3];
            cube[layer][i][3] = cube[layer][i][4];
            cube[layer][i][4] = helper;
        }

    }

    private void rotateSideLeft(int layer) {

        for (int i = 0; i < size; i++) {
            int helper = cube[i][size - layer - 1][4];
            cube[i][size - layer - 1][4] =
                    cube[size - i - 1][layer][5];
            cube[size - i - 1][layer][5] =
                    cube[size - i - 1][layer][2];
            cube[size - i - 1][layer][2] =
                    cube[size - i - 1][layer][0];
            cube[size - i - 1][layer][0] = helper;
        }

    }

    private void rotateSideFront(int layer) {

        for (int i = 0; i < size; i++) {
            int helper = cube[i][size - layer - 1][1];
            cube[i][size - layer - 1][1] =
                    cube[layer][i][5];
            cube[layer][i][5] = cube[size - i -1][layer][3];
            cube[size - i -1][layer][3] =
                    cube[size - layer - 1][size - i - 1][0];
            cube[size - layer - 1][size - i - 1][0] = helper;
        }

    }

    private void rotateSideUpDown(int side, int layer) {

        checkRotateSide(side, layer);
        if (side == 0) {
            rotateSideUp(layer);
        } else {
            for (int i = 0 ; i < 3; i++)
                rotateSideUp(size - layer - 1);
        }

    }

    private void rotateSideLeftRight(int side, int layer) {

        checkRotateSide(side, layer);
        if (side == 1) {
            rotateSideLeft(layer);
        } else {
            for (int i = 0 ; i < 3; i++)
                rotateSideLeft(size - layer - 1);
        }

    }

    private void rotateSideFrontBack(int side, int layer) {

        checkRotateSide(side, layer);
        if (side == 2) {
            rotateSideFront(layer);
        } else {
            for (int i = 0 ; i < 3; i++)
                rotateSideFront(size - layer - 1);
        }

    }

    private void exitProtocolWithoutLayers(int groupNumber) {

        barrier.acquireUninterruptibly();
        inCriticalSectionCounter--;
        // If we are the last process of our group to exit critical section,
        // we try to let a representative of other group in.
        if (inCriticalSectionCounter == 0) {
            if (waitingGroupCounter[(groupNumber + 1) % 4] > 0)
                group[(groupNumber + 1) % 4].release();
            else if (waitingGroupCounter[(groupNumber + 2) % 4] > 0)
                group[(groupNumber + 2) % 4].release();
            else if (waitingGroupCounter[(groupNumber + 3) % 4] > 0)
                group[(groupNumber + 3) % 4].release();
            else
                barrier.release();
        } else {
            barrier.release();
        }

    }

    private void entryProtocol(int groupNumber) throws InterruptedException {

        barrier.acquire();
        if (inCriticalSectionCounter > 0 &&
                (currentGroup != groupNumber || waitingCounter > 0)) {
            waitingCounter++;
            waitingGroupCounter[groupNumber]++;
            barrier.release();
            group[groupNumber].acquireUninterruptibly();
            // We inherit an unlocked barrier.
            waitingCounter--;
            waitingGroupCounter[groupNumber]--;
        }
        currentGroup = groupNumber;
        inCriticalSectionCounter++;
        // Letting in others waiting from our group.
        if (waitingGroupCounter[groupNumber] > 0)
            group[groupNumber].release();
        else
            barrier.release();

    }

    private void entryProtocolLayers(int side, int layer, int groupNumber)
            throws InterruptedException {

        try {
            if (side == 2 || side == 1 || side == 0)
                takenLayer[layer].acquire();
            else
                takenLayer[size - layer - 1].acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            exitProtocolWithoutLayers(groupNumber);
            throw e;
        }

    }

    private void exitProtocol(int groupNumber, int side, int layer) {

        if (groupNumber != 3) {
            if (side == 2 || side == 1 || side == 0)
                takenLayer[layer].release();
            else
                takenLayer[size - layer - 1].release();
        }

        exitProtocolWithoutLayers(groupNumber);

    }

    private void criticalSectionRotate(int side, int layer) {

        beforeRotation.accept(side, layer);

        if (side == 2 || side == 4)
            rotateSideFrontBack(side, layer);
        else if (side == 1 || side == 3)
            rotateSideLeftRight(side, layer);
        else
            rotateSideUpDown(side, layer);

        afterRotation.accept(side, layer);

    }

    public void rotate(int side, int layer) throws InterruptedException {

        int groupNumber;
        // Grouping based on side number. Sides that can rotate concurrently
        // are put in the same group. Group number 3 is reserved for show().
        if (side == 2 || side == 4)
            groupNumber = 0;
        else if (side == 1 || side == 3)
            groupNumber = 1;
        else
            groupNumber = 2;
        entryProtocol(groupNumber);
        entryProtocolLayers(side, layer, groupNumber);
        if (!Thread.currentThread().isInterrupted())
            criticalSectionRotate(side, layer);
        exitProtocol(groupNumber, side, layer);
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException();

    }

    private String criticalSectionShow() {

        beforeShowing.run();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < size; k++) {
                for (int l = 0; l < size; l++)
                    result.append(cube[k][l][i]);
            }
        }

        afterShowing.run();

        return result.toString();

    }

    public String show() throws InterruptedException {

        entryProtocol(3);
        String cube = "";
        if (!Thread.currentThread().isInterrupted())
            cube = criticalSectionShow();
        exitProtocol(3, 0, 0);
        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException();

        return cube;

    }

    public void initializeCube() {

        for (int i = 0; i < 6; i++) {
            for (int k = 0; k < size; k++) {
                for (int l = 0; l < size; l++)
                    cube[k][l][i] = i;
            }
        }

    }

}
