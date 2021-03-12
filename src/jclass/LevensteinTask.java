package jclass;

/**
 * Created by Home on 26.02.2020.
 */

// PAST
public class LevensteinTask {

    /**
     * Метод принимает на две строки и возвращает минимальное количество мутаций нужное для преобразования одного слова в другое
     * @param first первая строка в нижнем регистре
     * @param second вторая строка в нижнем регистре
     * @return Количество мутаций
     */
    public int getLevensteinLength(String first, String second){
        // вычисляем длины обеих строк
        int firstLength = first.length();
        int secondLength = second.length();
        //инициализируем массив для решения
        int[][] solutionMatrix = new int[firstLength + 1][secondLength + 1];
        for(int i = 0; i <= firstLength; i++){
            for (int j = 0; j <= secondLength; j++) {
                // заполняем массив уже известными, частными значениями
                solutionMatrix[i][j] = j * i == 0? j + i : 0;
            }
        }
        for(int i = 1; i <= firstLength; i++){
            for (int j = 1; j <= secondLength; j++) {
                if(first.charAt(i - 1) == second.charAt(j - 1)){
                    // если буквы одинаковые, то количество способов будет таким же как и для слов на букву меньше
                    solutionMatrix[i][j] = solutionMatrix[i - 1][j - 1];
                } else {
                    // если буквы разные, то у нас будет как минимум одна мутация + минимальное из тех, что мы можем
                    // получить удалением символа i-1,j, добавлением или изменением
                    solutionMatrix[i][j] = 1 + Math.min(
                            Math.min(
                                    solutionMatrix[i - 1][j],
                                    solutionMatrix[i][j - 1]),
                            solutionMatrix[i - 1][j - 1]);
                }
            }
        }
        // в пправой нижней ячейке хранится наше значение
        return solutionMatrix[firstLength][secondLength];
    }

    /**
     * Метод принимает на две строки и возвращает минимальное количество мутаций нужное для преобразования одного слова в другое c памятью O(n)
     * @param first первая строка в нижнем регистре
     * @param second вторая строка в нижнем регистре
     * @return Количество мутаций
     */
    public int getLevensteinLengthWithOptimisedMemory(String first, String second){
        // вычисляем длины обеих строк
        int sourceLength = first.length();
        int targetLength = second.length();
        //инициализируем массив для решения
        int[] solution = new int[targetLength + 1];
        int tempLeft; // будет временно хранить левый элемент(добавление)
        int tempSolution; // будет временно хранить число мутаций для текущих подслов

        // // заполняем массив уже известными, частными значениями
        for (int i = 0; i < targetLength + 1; i++) {
            solution[i] = i;
        }
        for(int i = 1; i <= sourceLength; i++){
            tempLeft = i;
            for (int j = 1; j <= targetLength; j++) {
                if(first.charAt(i - 1) == second.charAt(j - 1)){
                    // если буквы одинаковые, то количество способов будет таким же как и для слов на букву меньше
                    tempSolution = solution[j - 1];
                } else {
                    // если буквы разные, то у нас будет как минимум одна мутация + минимальное из тех, что мы можем
                    // получить удалением символа i-1,j, добавлением или изменением
                    tempSolution = 1 + Math.min(
                            Math.min(
                                    solution[j - 1],
                                    solution[j]),
                            tempLeft);
                }
                solution[j - 1] = tempLeft;
                tempLeft = tempSolution;
            }
            solution[targetLength] = tempLeft;
        }
        return solution[targetLength];
    }
}
