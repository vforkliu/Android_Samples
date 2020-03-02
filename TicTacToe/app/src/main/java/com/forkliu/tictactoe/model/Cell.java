package com.forkliu.tictactoe.model;

import com.forkliu.tictactoe.util.StringUtil;

public class Cell {
    public Player player;

    public Cell(Player player) {
        this.player = player;
    }

    public boolean isEmpty() {
        return player == null || StringUtil.isNullOrEmpty(player.value);
    }
}
