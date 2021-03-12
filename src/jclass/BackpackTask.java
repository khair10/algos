package jclass;

import java.util.*;

/**
 * Created by Home on 03.03.2020.
 */
public class BackpackTask {

    /**
     * Метод принимает на вход вместимость рюкзака, вещи определнные весами и ценностями и возвращает вещи в максимально
     * укомплектованном рюкзаке
     * @param backpackWeight вместимость рюкзака по весу
     * @param weight массив весов вещей
     * @param value массив весов вещей
     * @return Множество номеров вещей максимально укомплектованного рюкзака
     */
    public Set<Integer> backpack(int backpackWeight, int[] weight, int[] value) {
        int INF = -1; // условная бесконечность, не берем отрицательные числа
        int itemsQuantity = value.length; // количество вещей
        int[] backpacksValues = new int[backpackWeight + 1]; // суммы ценностей для каждого условного рюкзака с весами от 0 до максимального веса
        Map<Integer, Set<Integer>> backpacks = new HashMap<Integer, Set<Integer>>(); // условные рюкзаки для решения

        // инициализация
        for (int i = 0; i < backpackWeight + 1; i++) {
            backpacksValues[i] = i == 0 ? i : INF; // рюкзак с нулевым весом получает ценность 0, остальные пока не определены
            backpacks.put(i, new HashSet<Integer>()); // по весу инициализируется рюкзак предметов
        }

        // проходим по всем вещам
        for (int i = 0; i < itemsQuantity; i++) {
            // для каждой вещи проходим по всем рюкзакам, пока вместимомсть рюкзака больше или равна весу вещи
            for (int j = backpackWeight; j >= weight[i]; j--) {
                // если рюкзак с меньшей вместимостью на вес вещи и вещь имеют в сумме ценность больше ценности текущего
                // рюкзака меняем ценность текущего рюкзака и меняем его содержимое
                if (backpacksValues[j - weight[i]] != INF && backpacksValues[j - weight[i]] + value[i] > backpacksValues[j]) {
                    backpacksValues[j] = backpacksValues[j - weight[i]] + value[i];
                    Set<Integer> set = new HashSet<Integer>();
                    set.addAll(backpacks.get(j - weight[i]));
                    set.add(i);
                    backpacks.put(j, set);
                }
            }
        }

        // ищем максимальный по ценности рюкзак
        int max = INF;
        int index = 0;
        for (int i = 0; i < backpackWeight + 1; i++) {
            if (max < backpacksValues[i]) {
                max = backpacksValues[i];
                index = i;
            }
        }

        // return max;
        return backpacks.get(index);
    }
}
