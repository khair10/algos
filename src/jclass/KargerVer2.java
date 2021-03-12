package jclass;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Home on 15.04.2020.
 */
public class KargerVer2 {

    Random random = new Random();

    /**
     * Метод принимает на вход ребра и веса ребер и возвращает индексы ребер минимального разреза
     * @param edges ребра
     * @return Массив с индексами ребер минимального разреза
     */
    public int[][] minCut(int[][] edges){
        int edgeCount = edges.length;
        int vertexCount = getVertexCount(edges, edgeCount);
        boolean[] minCut = new boolean[edgeCount]; // индексы ребер минимального разреза
        int minCutLength = Integer.MAX_VALUE; // количество ребер в минимальном разрезе

        int count = vertexCount * vertexCount;
        for (int i = 0; i < count; i++) {
            // ищем минимальный разрез алгоритмом рандомного стягивания вершин
            boolean[] curCut = getCut(edges, vertexCount);
            int answer = 0; // вес разреза
            for (int j = 0; j < curCut.length; j++) {
                if(!curCut[j]){
                    answer++;
                }
            }
            if(answer < minCutLength){
                minCutLength = answer;
                minCut = curCut;
            }
        }
        int[][] result = new int[minCutLength][2];
        int index = 0;
        for (int i = 0; i < edgeCount; i++) {
            if(!minCut[i]){
                result[index][0] = edges[i][0];
                result[index][1] = edges[i][1];
                index++;
            }
        }
        return result;
    }

    private boolean[] getCut(int[][] graph, int vertexCount){
        int edgesCount = graph.length;
        int nextVertex = vertexCount + 1; // индекс следующей вершины для добавления
        // копирование массива в массив для рещения
        int[][] edges = new int[edgesCount][2];
        for (int i = 0; i < edgesCount; i++) {
            for (int j = 0; j < 2; j++) {
                edges[i][j] = graph[i][j];
            }
        }
        boolean[] checked = new boolean[edgesCount]; // массив отмечающий уже стянутые ребра
        int vertexIndex = 0;
        int vertexsCount = vertexCount; // изменяемое число вершин в решающем графе
        int tempEdgesCount = edgesCount; // количество незадестваванных вершин

        // стягиваем ребра пока не останется 2 вершины
        while (vertexsCount > 2){
            // выбираем рандомное ребро
            int edge = random.nextInt(tempEdgesCount);
            int tempIndex = 0;
            // проходим по ребрам и ищем индекс в графе
            for(int i = 0; i < edgesCount && vertexIndex <= edge; i++){
                if(!checked[i]){
                    vertexIndex++;
                    tempIndex = i;
                }
            }
            // отмечаем ребро и идентичные ему ребра задействованными
            for (int i = 0; i < edgesCount; i++) {
                if((edges[i][0] == edges[tempIndex][0] && edges[i][1] == edges[tempIndex][1]) ||
                        (edges[i][1] == edges[tempIndex][0] && edges[i][0] == edges[tempIndex][1])){
                    checked[i] = true;
                    tempEdgesCount--;
                }
            }
            // стягиваем ребро, смотрим на ребра в которых есть вершины из выбранного ребра и меняем эти вершины на новую вершину
            for (int i = 0; i < edgesCount; i++) {
                if(!checked[i] && (edges[tempIndex][0] == edges[i][0] || edges[tempIndex][0] == edges[i][1]
                        || edges[tempIndex][1] == edges[i][0] || edges[tempIndex][1] == edges[i][1])){
                    if (edges[tempIndex][0] == edges[i][0]){
                        edges[i][0] = nextVertex;
                    }
                    if (edges[tempIndex][0] == edges[i][1]){
                        edges[i][1] = nextVertex;
                    }
                    if (edges[tempIndex][1] == edges[i][0]){
                        edges[i][0] = nextVertex;
                    }
                    if (edges[tempIndex][1] == edges[i][1]){
                        edges[i][1] = nextVertex;
                    }
                }
            }
            nextVertex++;
            vertexIndex = 0;
            vertexsCount--;
        }
        return checked;
    }

    private int getVertexCount(int[][] edges, int edgeCount){
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < edgeCount; i++) {
            for (int j = 0; j < 2; j++) {
                set.add(edges[i][j]);
            }
        }
        return set.size();
    }
}
