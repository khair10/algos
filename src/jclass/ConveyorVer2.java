package jclass;

public class ConveyorVer2 {

    int shortestWayLength;

    /**
     * Метод принимает на вход время сборки на каждом отксеке каждого конвейера, время транспортировки с конвейера на другой
     * время начала, время конца.
     * @param workTime время работы отсеков двух конвейеров
     * @param transportTime время транспортировки между коневейерами
     * @param startupTime время начала работ
     * @param finishTime время окончания работ
     * @return Массив в котором на каждое состояние указывается номер конвейера
     */
    public int[] fastestWay(int[][] workTime, int[][] transportTime, int[] startupTime, int[] finishTime){
        int conveyorLength = workTime[0].length;
        shortestWayLength = Integer.MAX_VALUE;
        int[][] fastestWays = new int[startupTime.length][conveyorLength]; // время самых быстрых путей
        int[][] conveyorPathIndexes = new int[startupTime.length][conveyorLength]; // массив хранит предыдущий конвейер
        int[] conveyorsPath = new int[conveyorLength]; // результат
        int bestConveyorIndex = 0; // индекс конечного конвейера при самом быстром пути

        // начало
        for (int i = 0; i < startupTime.length; i++) {
            fastestWays[i][0] = startupTime[i] + workTime[i][0];
        }

        // проходим по каждом и смотрим на предыдущие состояния, с какого быстрее прийти в нынешнее

        for(int i = 1; i < conveyorLength; i++){
            int index = 0;
            for (int k = 0; k < startupTime.length; k++) { // фиксируем конвейер и для него ищем самый мелкий путь
                int answer = Integer.MAX_VALUE;
                int conveyorIndex = 0;
                for (int j = 0; j < startupTime.length; j++) { // проходим по всем путям
                    int tempAnswer;
                    int tempConveyorIndex;
                    if(k == j){
                        tempAnswer = fastestWays[k][i - 1] + workTime[k][i];
                        tempConveyorIndex = j;
                    } else {
                        tempAnswer = fastestWays[j][i - 1] + transportTime[index][i - 1] + workTime[k][i];
                        tempConveyorIndex = j;
                        index++;
                    }
                    if(tempAnswer < answer){
                        answer = tempAnswer;
                        conveyorIndex = tempConveyorIndex;
                    }
                }
                fastestWays[k][i] = answer;
                conveyorPathIndexes[k][i] = conveyorIndex;
            }
        }

        // сморим в каком конвейере самый быстрый путь
        for (int i = 0; i < finishTime.length; i++) {
            int answer = finishTime[i] + fastestWays[i][conveyorLength - 1];
            if(answer < shortestWayLength){
                shortestWayLength = answer;
                bestConveyorIndex = i;
            }
        }

        // вычисляем этот путь
        int tempIndex = bestConveyorIndex;
        for (int i = conveyorLength - 1; i >= 0; i--) {
            conveyorsPath[i] = tempIndex;
            tempIndex = conveyorPathIndexes[tempIndex][i];
        }
        return conveyorsPath;
    }

    public int getShortestWayLength() {
        return shortestWayLength;
    }
}
