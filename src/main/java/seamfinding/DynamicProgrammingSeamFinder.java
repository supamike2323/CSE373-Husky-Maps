package seamfinding;

import seamfinding.energy.EnergyFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        int width = picture.width();
        int height = picture.height();

        double[][] energyTable = new double[width][height];
        int[][] backPointer = new int[width][height];

        for (int y = 0; y < height; y++) {
            energyTable[0][y] = f.apply(picture, 0, y);
        }

        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double pixelEnergy = f.apply(picture, x, y);

                double minEnergy = Double.POSITIVE_INFINITY;
                int bestPrevY = -1;

                for (int dy = -1; dy <= 1; dy++) {
                    int prevY = y + dy;
                    if (prevY >= 0 && prevY < height) {
                        double pathEnergy = energyTable[x-1][prevY] + pixelEnergy;
                        if (pathEnergy < minEnergy) {
                            minEnergy = pathEnergy;
                            bestPrevY = prevY;
                        }
                    }
                }

                energyTable[x][y] = minEnergy;
                backPointer[x][y] = bestPrevY;
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int endY = 0;
        for (int y = 0; y < height; y++) {
            if (energyTable[width-1][y] < minEnergy) {
                minEnergy = energyTable[width-1][y];
                endY = y;
            }
        }

        List<Integer> seam = new ArrayList<>();
        int currentY = endY;
        seam.add(currentY);

        for (int x = width-1; x > 0; x--) {
            currentY = backPointer[x][currentY];
            seam.add(currentY);
        }

        Collections.reverse(seam);
        return seam;
    }
}
