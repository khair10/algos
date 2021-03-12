package jclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Home on 17.03.2020.
 */
public class DijkstraTask {

    private double[] weights;
    private int[] previousVer;
    private int length;
    private int INF = -1;

    public DijkstraTask(double[][] graph, int ver){
        length = graph[0].length;
        weights = new double[length];
        previousVer = new int[length];
        // для всех вершин кроме текущей задаём неопределенное расстояние
        for (int i = 0; i < length; i++) {
            if(i != ver){
                weights[i] = INF;
            }
        }
        resolve(graph, ver);
    }

    private void resolve(double[][] graph, int ver) {
        previousVer[ver] = INF;

        int currentVer = ver;
        double min = Double.MAX_VALUE;
        int tempVer = currentVer;

        // инициализируем множество нерассчитанных вершин
        HashSet<Integer> unchecked = new HashSet<Integer>();
        for (int i = 0; i < length; i++) {
            unchecked.add(i);
        }

        // проходим по всем нерассчитанным вершинам, пока они все не будут рассчитаны
        Iterator<Integer> uncheckedIterator = unchecked.iterator();
        while(!unchecked.isEmpty()){
            // проходим по каждой вершине
            for (int i = 0; i < unchecked.size(); i++) {
                // сохраняем рассматриваемую вершину
                int temp = uncheckedIterator.next();
                // берем вершины кроме настоящей, для которых есть ребро из настоящей вершины
                if(currentVer != temp && graph[currentVer][temp] != INF){
                    // если протяженность пути рассматриваемой вершины больше чем суммарный путь до неё из настоящей вершины,
                    // то присваеваем ей новую протяженность пути и сохраняем настоящую вершину в качестве предыдущей для действующей вершины
                    if(weights[temp] > graph[currentVer][temp] + weights[currentVer] || weights[temp] == INF){
                        weights[temp] = graph[currentVer][temp] + weights[currentVer];
                        previousVer[temp] = currentVer;
                    }
                }
                //ищем минимальный путь и запонимаем вершины с этим путем
                if(min > weights[temp] && weights[temp] != INF) {
                    min = weights[temp];
                    tempVer = temp;
                }
            }
            min = Double.MAX_VALUE;
            //убираем действующую вершину из нерассмотренных
            unchecked.remove(currentVer);
            // присваеваем действующей вершине вершину с минимальным путем
            currentVer = tempVer;
            // обновляем итератор
            uncheckedIterator = unchecked.iterator();
        }
    }

    public double[] getShortestWayWeightFromStart(){
        return weights;
    }

    public ArrayList<Integer> getShortestPathFromStart(int ver){
        ArrayList<Integer> path = new ArrayList<Integer>();
        int tempVer = ver;
        while(previousVer[tempVer] != -1){
            path.add(previousVer[tempVer]);
            tempVer = previousVer[tempVer];
        }
        return path;
    }
}
