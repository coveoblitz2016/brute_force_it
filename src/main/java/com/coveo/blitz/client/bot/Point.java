package com.coveo.blitz.client.bot;

import com.coveo.blitz.client.dto.GameState;

import java.util.ArrayList;

public class Point
{
    public GameState.Position position;
    public Point from;
    public int cost;
    public boolean blocking;

    public Point(GameState.Position position, Point from, int cost, boolean blocking)
    {
        this.position = position;
        this.from = from;
        this.cost = cost;
        this.blocking = blocking;
    }

    public ArrayList<BotMove> getPath()
    {
        if (from == null) {
            return new ArrayList<>();
        }
        ArrayList<BotMove> path = from.getPath();
        int diffX = position.getX() - from.position.getX();
        int diffY = position.getY() - from.position.getY();
        if (diffY == -1) {
            path.add(BotMove.WEST);
        } else if (diffY == 1) {
            path.add(BotMove.EAST);
        } else if (diffX == -1) {
            path.add(BotMove.NORTH);
        } else if (diffX == 1) {
            path.add(BotMove.SOUTH);
        }
        return path;
    }
}
