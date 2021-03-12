package jclass;

import java.util.Random;

/**
 * Created by Home on 16.04.2020.
 */
public class MedianTask {

    Random random = new Random();

    public double quickSelectMedian(int[] l){
        if (l.length % 2 == 1){
            return quickSelect(l, l.length / 2);
        } else {
            return 0.5 * (quickSelect(l, l.length / 2 - 1) + quickSelect(l, l.length / 2));
        }
    }

    private int quickSelect(int[] l, int k){
        int len = l.length;
        if(len == 1){
            return l[0];
        }

        //берем рандомный индекс из всех индексов массива
        int pivot = random.nextInt(len);

        //считается количество элементов меньше, равно, больше элемента под индексом pivot
        int lowsIndex = 0;
        int hightsIndex = 0;
        int pivotsIndex = 0;
        for (int i = 0; i < l.length; i++) {
            if (l[i] < l[pivot]){
                lowsIndex++;
            } else if (l[i] > l[pivot]){
                hightsIndex++;
            } else {
                pivotsIndex++;
            }
        }
        int[] lows = new int[lowsIndex];
        int[] hights = new int[hightsIndex];
        int[] pivots = new int[pivotsIndex];
        lowsIndex = 0;
        hightsIndex = 0;
        pivotsIndex = 0;
        // Заполняем массивы элементов меньше, равно, больше
        for (int i = 0; i < l.length; i++) {
            if (l[i] < l[pivot]){
                lows[lowsIndex++] = l[i];
            } else if (l[i] > l[pivot]){
                hights[hightsIndex++] = l[i];
            } else {
                pivots[pivotsIndex++] = l[i];
            }
        }
        //если k < lows.len, ищем медиану в нём
        if (k < lows.length){
            return quickSelect(lows, k);
        // k лежит в pivots, то мы нашли медиану, берем ёё
        } else if (k < lows.length + pivots.length) {
            return pivots[0];
        // k лежит в hights, ищем медиану в нём
        } else {
            return quickSelect(hights, k - lows.length - pivots.length);
        }
    }

//    private int quickSelect2(int[] l, int k){
//        int len = l.length;
//        if(len == 1){
//            return l[0];
//        }
//        int pivot = len / 2;
//
//        int lowsIndex = 0;
//        int hightsIndex = 0;
//        int pivotsIndex = 0;
//        for (int i = 0; i < l.length; i++) {
//            if (l[i] < l[pivot]){
//                lowsIndex++;
//            } else if (l[i] > l[pivot]){
//                hightsIndex++;
//            } else {
//                pivotsIndex++;
//            }
//        }
//        int[] lows = new int[lowsIndex];
//        int[] hights = new int[hightsIndex];
//        int[] pivots = new int[pivotsIndex];
//        lowsIndex = 0;
//        hightsIndex = 0;
//        pivotsIndex = 0;
//        for (int i = 0; i < l.length; i++) {
//            if (l[i] < l[pivot]){
//                lows[lowsIndex++] = l[i];
//            } else if (l[i] > l[pivot]){
//                hights[hightsIndex++] = l[i];
//            } else {
//                pivots[pivotsIndex++] = l[i];
//            }
//        }
//        if (k < lows.length){
//            return quickSelect2(lows, k);
//        } else if (k < lows.length + pivots.length) {
//            return pivots[0];
//        } else {
//            return quickSelect2(hights, k - lows.length - pivots.length);
//        }
//    }
//
//    public double medianPointsByX(int[][] points){
//        int[] xs = new int[points.length];
//        for (int i = 0; i < points.length; i++) {
//            xs[i] = points[i][0];
//        }
//        return quickSelectMedian(xs);
//    }
}
