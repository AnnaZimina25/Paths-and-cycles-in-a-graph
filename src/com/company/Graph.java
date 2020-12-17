package com.company;

import java.util.ArrayList;
import java.util.List;

public final class Graph {

    private final int vertexCount; // количество вершин
    private final int[][] graphMatrix; // матрица смежности
    private ArrayList<Integer> graphCenter, pathList; // списки вершин центра и пути
    private String centerStr; // центр графа (строка с вершинами через пробел)
    private String pathStr; // путь между вершинами (строка с вершинами через пробел)

    // Конструкторы класса:
    public Graph(int vertexCount, int[][] graphMatrix) {

        this.vertexCount = vertexCount;
        this.graphMatrix = graphMatrix;
    }

    // Геттеры и сеттеры полей класса
    public int[][] getMatrix () {
        return this.graphMatrix;
    }
    public int getVertexCount () {
        return this.vertexCount;
    }
    public ArrayList<Integer> getCenter(){ return this.graphCenter;}

    public String getCenterStr () {
        return this.centerStr;
    }

    public String getPathStr () {
        return this.pathStr;
    }
    public ArrayList<Integer> getPathList () {
        return this.pathList;
    }

    // Находим центр графа:
    public void setCenter () {
        int n = this.vertexCount;
        ArrayList<Integer> center = new ArrayList<>(); // переменная для списка вершин центра
        int[][] distM = new int[n][n]; // переменная матрицы расстояний
        int[] excent = new int[n]; // переменная массива эксцентриситета

        // Получаем матрицу расстояний:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.graphMatrix[i][j] == 0) {
                    distM[i][j] = Integer.MAX_VALUE/100; // если между вершинами нет ребер, помечаем расстояние между ними как бесконечность/100
                } else {
                    distM[i][j] = 1;
                }
            }
        }
        // Алгоритм Флойда-Уоршелла (поиск кратчайших расстояний):
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    distM[i][j] = Math.min(distM[i][j],
                            distM[i][k] + distM[k][j]);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            distM[i][i] = 0;
        }
        System.out.println("Матрица кратчайших расстояний:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(distM[i][j] + " ");
            }
            System.out.println();
        }
        // Поиск эксцентриситета:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                excent[i] = Math.max(excent[i], distM[i][j]);
            }
        }
        System.out.println("Массив эксцентриситета: ");
        for (int elem : excent) {
            System.out.print(elem + " ");
        }
        System.out.println();
        // Поиск радиуса графа:
        int rad = Integer.MAX_VALUE; // радиус принимаем равным бесконечности
        for (int e : excent) {
            rad = Math.min(rad, e);
        }
        System.out.println("rad = " + rad);
        // Поиск центра графа:
        for (int i = 0; i < n; i++) {
            if (excent[i] == rad) {
                center.add(i);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Центр графа: ");

        for (int c : center) {
            sb.append(c + " ");
        }
        this.centerStr = sb.toString();
        this.graphCenter = center;

    }

    // Удаление смежных ребер центра:
    public int[][] removeCenterMatrix(List<Integer> center) {

        int n = this.vertexCount;
        int[][] matrix = this.graphMatrix;

        for (int c : center) {
            for (int i = 0; i < n; i++) {
                matrix[i][c] = 0;
                matrix[c][i] = 0;
            }
        }

        return matrix;
    }

    // Поиск пути между вершинами.
    // Обход графа в глубину (Depth-first search, DFS):
    public ArrayList<Integer> getPathDFS (int start, int end, ArrayList<Integer> path) {

        if (start == end) {
            path.add(start);

            StringBuilder sb = new StringBuilder();
            sb.append("Путь между вершинами " + ProgramMenu.start + " и " + end + " : ");
            for (int p : path) {
                sb.append(p + " ");
            }
            this.pathStr = sb.toString();
            this.pathList = path;
            return path;
        }

        path.add(start);
        for (int i = 0; i < this.graphMatrix[start].length; i++) { // перебираем вершины
            if (!path.contains(i) && this.graphMatrix[start][i] == 1) { // если вершина не помечена, и смежна с текущей
                ArrayList<Integer> newPath = getPathDFS(i, end, path); // рекурсивно запускаем от нее DFS
                return newPath;
            }
        }

        this.pathStr = "Пути между вершинами " + ProgramMenu.start + " и " + end + " не существует";
        this.pathList = null;
        return path;
    }

}

