package com.molean.isletopia.isletopiaskywar;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TeamSystem {
    private static final Map<Player, Integer> map = new ConcurrentHashMap<>();

    private static void removeOfflinePlayer(Map<Player, Integer> map) {
        ArrayList<Player> players = new ArrayList<>(map.keySet());
        for (Player player : players) {
            if (!player.isOnline()) {
                map.remove(player);
            }
        }
    }

    private static int countGroupMember(Map<Player, Integer> map, int x) {
        if (x <= 0 || x >= 9) {
            throw new RuntimeException();
        }
        removeOfflinePlayer(map);
        int cnt = 0;
        for (Integer value : map.values()) {
            if (value == x) {
                cnt++;
            }
        }
        return cnt;
    }

    public static boolean requestGroup( Player player, int x) {

        if (x <= 0 || x >= 9) {
            throw new RuntimeException();
        }
        removeOfflinePlayer(map);
        if (countGroupMember(map,x) >= 2) {
            return false;
        }
        map.put(player, x);
        return true;
    }

    public static Map<Player, Integer> group(List<Player> playerList) {
        removeOfflinePlayer(map);
        ConcurrentHashMap<Player, Integer> groupMap = new ConcurrentHashMap<>();

        //把选好队伍的玩家分配好
        ArrayList<Player> tempPlayerList = new ArrayList<>(playerList);
        for (Player player : playerList) {
            if (map.get(player) != null) {
                groupMap.put(player, map.get(player));
                tempPlayerList.remove(player);
            }
        }

        //将剩余玩家均匀分配到各个岛上
        while (!tempPlayerList.isEmpty()) {

            //如果有一队人数为1, 则分一个队友

            for (int i = 1; i <= 8; i++) {
                if (countGroupMember(groupMap, i) == 1) {
                    groupMap.put(tempPlayerList.get(0), i);
                    tempPlayerList.remove(0);
                    break;
                }
            }

            //如果每个队都满二人, 则从所有岛中选人最少的队

            int minGroup = 1;
            int minGroupCount = 0;
            for (int i = 1; i <= 8; i++) {
                int groupMember = countGroupMember(groupMap, i);
                if (groupMember < minGroupCount) {
                    minGroup = i;
                    minGroupCount = groupMember;
                }
            }
            groupMap.put(tempPlayerList.get(0), minGroup);
            tempPlayerList.remove(0);
        }



        //合并一些队伍
        return groupMap;
    }

}
