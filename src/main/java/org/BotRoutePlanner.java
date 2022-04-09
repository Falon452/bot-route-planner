package org;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.System.exit;


class BotRoutePlanner {
    public int xBotStart;
    public int yBotStart;
    public int xReceiver;
    public int yReceiver;
    public Node[][] grid;
    public int nofLayers;
    public int width;
    public int height;
    public String productToFind;
    public Node bestNode;
    public Double bestTime;
    List<Pair<Integer,Integer>> modulesWithTarget = new ArrayList<Pair<Integer,Integer>>();
    List<Pair<Integer,Integer>> route = new ArrayList<Pair<Integer,Integer>>();


    public void solve(String[] args) {
        this.readJobFile(args[1]);
        this.readGrid(args[0]);

        dijkstra(true);
        resetVisited();
        dijkstra(false);

        findBestNode();
        fillRoute();
        printSolution();
    }


    public void dijkstra(Boolean startFromReceiver) {
        int xStart, yStart;
        Queue<Node> pq;

        if (startFromReceiver) {
            xStart = xReceiver;
            yStart = yReceiver;
            pq = new PriorityQueue<>(new Node.NodeReceiverComparator());
            grid[yStart][xStart].distanceFromReceiver = 0;
        } else {
            xStart = xBotStart;
            yStart = yBotStart;
            pq = new PriorityQueue<>(new Node.NodeStartComparator());
            grid[yStart][xStart].distanceFromStart = 0;
        }


        pq.add(grid[yStart][xStart]);

        while (!pq.isEmpty()) {
            Node v = pq.poll();

            if (v.visited)
                continue;

            v.visited = true;

            if (v.x + 1 < width) {
                Node u = grid[v.y][v.x + 1];
                if (relax(v, u, startFromReceiver))
                    pq.add(u);
            }
            if (v.y + 1 < height) {
                Node u = grid[v.y + 1][v.x];
                if (relax(v, u, startFromReceiver))
                    pq.add(u);
            }
            if (v.x - 1 >= 0) {
                Node u = grid[v.y][v.x - 1];
                if (relax(v, u, startFromReceiver))
                    pq.add(u);
            }
            if (v.y - 1 >= 0) {
                Node u = grid[v.y - 1][v.x];
                if (relax(v, u, startFromReceiver))
                    pq.add(u);;
            }
        }
    }


    public boolean relax(Node v, Node u, boolean startFromReceiver) {
        double distance, weight = edgeWeight(v, u);

        if (startFromReceiver) {
            distance = v.distanceFromReceiver + weight;
            if (distance < u.distanceFromReceiver && !u.visited) {
                u.distanceFromReceiver = distance;
                u.previousReceiver = v;
                return true;
            }
        } else {
            distance = v.distanceFromStart + weight;
            if (distance < u.distanceFromStart && !u.visited) {
                u.distanceFromStart = distance;
                u.previousStart = v;
                return true;
            }
        }
        return false;
    }


    public void printSolution() {
        System.out.println(route.size() - 1);
        System.out.println(bestTime);
        for (Pair<Integer, Integer> integerIntegerPair : route) {
            System.out.print(integerIntegerPair.getX() + " " + integerIntegerPair.getY() + "\n");
        }
    }


    private double edgeWeight(Node v, Node u) {
        double vDistanceTime, uDistanceTime;
        vDistanceTime = distanceTime(v);
        uDistanceTime = distanceTime(u);
        return max(vDistanceTime, uDistanceTime);
    }


    private double distanceTime(Node v) {
        switch (v.type) {
            case 'H' -> {
                 return 0.5;
            }
            case 'B' -> {
                return 1;
            }
            case 'S' -> {
                return 2;
            }
            case 'O' -> {
                return Double.POSITIVE_INFINITY;
            }
        }
        System.out.println("ERROR: Invalid Node.type");
        exit(1);
        return Double.POSITIVE_INFINITY;
    }


    private void readGrid(String filename) {
        try {
            FileInputStream fis= new FileInputStream(filename);
            Scanner input = new Scanner(fis);

            this.width = input.nextInt();
            this.height = input.nextInt();
            nofLayers = input.nextInt();

            grid = new Node[height][width];

            input.useDelimiter("");
            input.nextLine();


            for (int y = 0; y < height; y++) {
                String typeList = input.nextLine();

                for (int x = 0; x < width; x++) {
                    Character type = typeList.charAt(x);
                    grid[y][x] = new Node(x, y, type);
                    grid[y][x].product = new String[nofLayers];
                }
            }

            while(input.hasNextLine()) {
                String[] inputList = input.nextLine().split(" ");

                String productType = inputList[0];
                int x = Integer.parseInt(inputList[1]);
                int y = Integer.parseInt(inputList[2]);
                int position = Integer.parseInt(inputList[3]);

                grid[y][x].product[position] = productType;

                if (productType.equals(productToFind)) {
                    modulesWithTarget.add(new Pair<Integer, Integer>(x, y));
                }
            }

            input.close();
        }
        catch(IOException e) { e.printStackTrace();}
    }


    private void readJobFile(String filename) {
        try  {
            FileInputStream fis= new FileInputStream(filename);
            Scanner input = new Scanner(fis);

            this.xBotStart = input.nextInt();
            this.yBotStart = input.nextInt();
            this.xReceiver = input.nextInt();
            this.yReceiver = input.nextInt();
            this.productToFind = input.next();

            input.close();
        }
        catch(IOException e) { e.printStackTrace();}
    }


    private void findBestNode(){
        double bestTime = Double.POSITIVE_INFINITY, tmpTime;
        Node bestNode = new Node(-1, -1, 't');

        for (Pair<Integer, Integer> integerPair : modulesWithTarget) {
            tmpTime = 0;
            int x = integerPair.getX();
            int y = integerPair.getY();

            for (int n = 0; n < nofLayers; n++) {
                if (Objects.nonNull(grid[y][x].product[n])) {
                    if (grid[y][x].product[n].equals(productToFind)) {
                        switch (grid[y][x].type) {
                            case 'H' -> tmpTime = 3 * n + 4;
                            case 'B' -> tmpTime = 2 * n + 2;
                            case 'S' -> tmpTime = n + 1;
                            case 'O' -> tmpTime = Double.POSITIVE_INFINITY;
                        }
                        break; // don't search for product in deeper layers
                    }
                }
            }
            tmpTime += grid[y][x].distanceFromReceiver;
            tmpTime += grid[y][x].distanceFromStart;

            if (tmpTime < bestTime) {
                bestTime = tmpTime;
                bestNode = grid[y][x];
            }
        }
        this.bestNode = bestNode;
        this.bestTime = bestTime;
    }


    private void fillRoute() {
        route.add(0, new Pair<Integer, Integer>(bestNode.x, bestNode.y));

        Node tmp = bestNode;
        while (Objects.nonNull(tmp.previousStart)) {
            tmp = tmp.previousStart;
            route.add(0, new Pair<Integer, Integer>(tmp.x, tmp.y));
        }

        tmp = bestNode;
        while (Objects.nonNull(tmp.previousReceiver)) {
            tmp = tmp.previousReceiver;
            route.add(new Pair<Integer, Integer>(tmp.x, tmp.y));
        }
    }


    private void resetVisited() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x].visited = false;
            }
        }
    }
}


