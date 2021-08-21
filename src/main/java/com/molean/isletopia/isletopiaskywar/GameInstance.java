package com.molean.isletopia.isletopiaskywar;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInstance {
    public enum GameStatus {
        PREPARING, STAGE_1, STAGE_2, STAGE_3, STAGE_4, ENDING, WAITING;

        public boolean isGaming() {
            return this.equals(STAGE_1) || this.equals(STAGE_2) || this.equals(STAGE_3) || this.equals(STAGE_4);
        }

    }

    public GameStatus gameStatus = GameStatus.WAITING;

    public Map<Player, Integer> gamePlayers = new HashMap<>();

    public int mineBonus = 1;


}
