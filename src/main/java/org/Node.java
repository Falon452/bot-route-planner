package org;

import java.util.Comparator;

public class Node {
    public final int x;
    public final int y;
    public Character type;
    public String[] product;
    public boolean visited = false;
    public double distanceFromStart = Double.POSITIVE_INFINITY;
    public double distanceFromReceiver = Double.POSITIVE_INFINITY;
    public Node previousStart;
    public Node previousReceiver;

    Node (int x, int y, Character type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    static class NodeStartComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2)
        {
            if (o1.distanceFromStart == o2.distanceFromStart)
                return 0;
            return o1.distanceFromStart > o2.distanceFromStart ? 1 : -1;
        }
    }

    static class NodeReceiverComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2)
        {   if (o1.distanceFromReceiver == o2.distanceFromReceiver)
            return 0;
            return o1.distanceFromReceiver > o2.distanceFromReceiver ? 1 : -1;
        }
    }
}
