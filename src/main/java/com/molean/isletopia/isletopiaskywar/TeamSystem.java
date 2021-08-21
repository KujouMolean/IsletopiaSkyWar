package com.molean.isletopia.isletopiaskywar;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TeamSystem {
    private static final Map<Player, Integer> map = new ConcurrentHashMap<>();

    private static void removeOfflinePlayer() {
        ArrayList<Player> players = new ArrayList<>(map.keySet());
        for (Player player : players) {
            if (!player.isOnline()) {
                map.remove(player);
            }
        }
    }

    private static int countGroupMember(int x) {
        if (x <= 0 || x >= 9) {
            throw new RuntimeException();
        }
        removeOfflinePlayer();
        int cnt = 0;
        for (Integer value : map.values()) {
            if (value == x) {
                cnt++;
            }
        }
        return cnt;
    }

    public static boolean requestGroup(Player player, int x) {
        if (x <= 0 || x >= 9) {
            throw new RuntimeException();
        }
        removeOfflinePlayer();
        if (countGroupMember(x) >= 4) {
            return false;
        }
        map.put(player, x);
        return true;
    }

    public static Map<Player, Integer> group(List<Player> playerList) {
        removeOfflinePlayer();
        ConcurrentHashMap<Player, Integer> groupMap = new ConcurrentHashMap<>();
        int max = 0;
        for (int i = 1; i < 8; i++) {
            max = Math.max(max, countGroupMember(i));
        }

        //计算每队人数
        max = (int) Math.max(max, Math.ceil(playerList.size() / 8.0));

        //计算这次一共有几个队伍
        int groupCnt = playerList.size() / max;

        //把选好队伍的玩家分配好
        ArrayList<Player> tempPlayerList = new ArrayList<>(playerList);
        for (Player player : playerList) {
            if (map.get(player) != null) {
                groupMap.put(player, map.get(player));
                tempPlayerList.remove(player);
            }
        }

        //选出几个岛不分配玩家
        HashSet<Integer> ignoredGroup = new HashSet<>();
        while (ignoredGroup.size() != (8 - groupCnt)) {
            for (int i = 1; i <= 8; i++) {
                if (!groupMap.containsValue(i)) {
                    ignoredGroup.add(i);
                }
            }
        }



        //将剩余玩家均匀分配到各个岛上
        for (Player player : tempPlayerList) {
            int minPlayerGroup = 1;
            int minPlayerGroupValue = 4;

            for (int i = 1; i < 8; i++) {
                int cnt = 0;
                for (Integer value : groupMap.values()) {
                    if (value == i) {
                        cnt++;
                    }
                }
                if (minPlayerGroupValue > cnt) {
                    minPlayerGroupValue = cnt;
                    minPlayerGroup = i;
                }
            }

            groupMap.put(player, minPlayerGroup);
        }

        return groupMap;


    }


}
