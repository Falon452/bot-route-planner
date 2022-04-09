package org;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotRoutePlannerTest {

    @Test
    void solve() {
        BotRoutePlanner botRoutePlanner = new BotRoutePlanner();
    }

    @Test
    void dijkstra() {
        BotRoutePlanner botRoutePlanner = new BotRoutePlanner();
        botRoutePlanner.width = 4;
        botRoutePlanner.height = 3;
        botRoutePlanner.xReceiver = 0;
        botRoutePlanner.yReceiver = 0;
        botRoutePlanner.grid = new Node[3][4];
        String types = "HHSHHBHHHHOS";
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                botRoutePlanner.grid[y][x] = new Node(x, y, types.charAt(x + 4*y));
            }
        }
        botRoutePlanner.dijkstra(true);
        assert(botRoutePlanner.grid[0][0].distanceFromReceiver == 0);
        assert(botRoutePlanner.grid[0][1].distanceFromReceiver == 0.5);
        assert(botRoutePlanner.grid[0][2].distanceFromReceiver == 2.5);
        assert(botRoutePlanner.grid[0][3].distanceFromReceiver == 3.5);

        assert(botRoutePlanner.grid[1][0].distanceFromReceiver == 0.5);
        assert(botRoutePlanner.grid[1][1].distanceFromReceiver == 1.5);
        assert(botRoutePlanner.grid[1][2].distanceFromReceiver == 2.5);
        assert(botRoutePlanner.grid[1][3].distanceFromReceiver == 3);

        assert(botRoutePlanner.grid[2][0].distanceFromReceiver == 1);
        assert(botRoutePlanner.grid[2][1].distanceFromReceiver == 1.5);
        assert(botRoutePlanner.grid[2][2].distanceFromReceiver == Double.POSITIVE_INFINITY);
        assert(botRoutePlanner.grid[2][3].distanceFromReceiver == 5);
    }

    @Test
    void relax() {
        BotRoutePlanner botRoutePlanner = new BotRoutePlanner();
        Node n1 = new Node(0, 0, 'H');
        Node n2 = new Node(1, 1, 'S');
        n1.distanceFromReceiver = 1;
        botRoutePlanner.relax(n1, n2, true);
        assert(n2.distanceFromReceiver == 3);

        n2.distanceFromReceiver = 0;
        botRoutePlanner.relax(n1, n2, true);
        assert(n2.distanceFromReceiver == 0);

        n1.distanceFromStart = 10;
        n2.distanceFromStart = 100;
        botRoutePlanner.relax(n1, n2, false);
        assert(n2.distanceFromStart == 12);
        n2.distanceFromStart = 8;
        botRoutePlanner.relax(n1, n2, false);
        assert(n2.distanceFromStart == 8);
    }
}