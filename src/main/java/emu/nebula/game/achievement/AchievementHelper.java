package emu.nebula.game.achievement;

import java.util.List;

import emu.nebula.GameConstants;
import emu.nebula.data.GameData;
import emu.nebula.data.resources.AchievementDef;
import emu.nebula.game.tower.room.RoomType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;

// Because achievements in the data files do not have params, we will hardcode them here
public class AchievementHelper {
    // Cache
    private static IntSet incrementalAchievementSet = new IntOpenHashSet();
    
    @Getter
    private static Int2ObjectMap<List<AchievementDef>> cache = new Int2ObjectOpenHashMap<>();
    
    public static List<AchievementDef> getAchievementsByCondition(int condition) {
        return cache.get(condition);
    }
    
    //
    
    public static boolean isIncrementalAchievement(int condition) {
        return incrementalAchievementSet.contains(condition);
    }
    
    // Fix params
    
    public static void init() {
        // Cache total achievements
        for (var condition : AchievementCondition.values()) {
            if (condition.name().endsWith("Total") || condition.name().endsWith("Times")) {
                incrementalAchievementSet.add(condition.getValue());
            }
        }
        
        incrementalAchievementSet.remove(AchievementCondition.AchievementTotal.getValue());
        
        incrementalAchievementSet.add(AchievementCondition.ItemsAdd.getValue());
        incrementalAchievementSet.add(AchievementCondition.ItemsDeplete.getValue());
        
        incrementalAchievementSet.add(AchievementCondition.TowerItemsGet.getValue());
        incrementalAchievementSet.add(AchievementCondition.TowerEnterRoom.getValue());
        
        // Fix params
        fixParams();
    }
    
    private static void fixParams() {
        // Clear "Misstep On One"
        addParam(56, 401, 0); // Custom trigger
        
        // Clear "Currents and Shadows"
        addParam(57, 102, 0);
        addParam(58, 103, 0);
        addParam(59, 104, 0);
        addParam(60, 105, 0);
        addParam(61, 106, 0);
        addParam(62, 107, 0);
        addParam(63, 108, 0);
        
        // Clear "Dust and Flames"
        addParam(64, 202, 0);
        addParam(65, 203, 0);
        addParam(66, 204, 0);
        addParam(67, 205, 0);
        addParam(68, 206, 0);
        addParam(69, 207, 0);
        addParam(70, 208, 0);
        
        // Clear "Storm and Thunder"
        addParam(71, 302, 0);
        addParam(72, 303, 0);
        addParam(73, 304, 0);
        addParam(74, 305, 0);
        addParam(75, 306, 0);
        addParam(76, 307, 0);
        addParam(77, 308, 0);
        
        // First Ascension
        addParam(498, 0, 1);
        
        // Monolith Conqueror
        addParam(78, 2, 0);
        addParam(79, 4, 0);
        addParam(80, 6, 0);
        addParam(81, 7, 0);
        
        // Money
        addParam(25, GameConstants.GOLD_ITEM_ID, 0);
        addParam(26, GameConstants.GOLD_ITEM_ID, 0);
        addParam(27, GameConstants.GOLD_ITEM_ID, 0);
        addParam(28, GameConstants.GOLD_ITEM_ID, 0);
        addParam(29, GameConstants.GOLD_ITEM_ID, 0);
        
        // Ininfite tower
        for (int diff = 10, id = 270; diff <= 60; diff += 10) {
            addParam(id++, 11000 + diff, 0); // Infinite Arena
            addParam(id++, 51000 + diff, 0); // Shake the Floor
            addParam(id++, 41000 + diff, 0); // Elegance and Flow
            addParam(id++, 71000 + diff, 0); // Upbeat Party
            addParam(id++, 31000 + diff, 0); // Thrilling Beat
            addParam(id++, 21000 + diff, 0); // Flames and Beats
            addParam(id++, 61000 + diff, 0); // Sinister Ritual
        }
        
        // Character count
        addParams(393, 398, 1, 0);
        
        // Disc count
        addParams(382, 387, 1, 0);
        
        // Star Tower team clear
        addParams(95, 98, 1, 0);   // Aqua team clear
        addParams(99, 102, 2, 0);  // Fire team clear
        addParams(103, 106, 3, 0); // Earth team clear
        addParams(107, 110, 4, 0); // Wind team clear
        addParams(111, 114, 5, 0); // Light team clear
        addParams(115, 118, 6, 0); // Dark team clear
        
        // Star tower items
        addParams(139, 144, GameConstants.TOWER_COIN_ITEM_ID, 0);
        
        addParams(145, 149, 90011, 0);
        addParams(150, 154, 90012, 0);
        addParams(155, 159, 90013, 0);
        addParams(160, 164, 90014, 0);
        addParams(165, 169, 90015, 0);
        addParams(170, 174, 90016, 0);
        addParams(175, 179, 90017, 0);
        
        addParams(180, 184, 90018, 0);
        addParams(185, 189, 90019, 0);
        addParams(190, 194, 90020, 0);
        addParams(195, 199, 90021, 0);
        addParams(200, 204, 90022, 0);
        addParams(205, 209, 90023, 0);
        
        // Star tower rooms
        addParams(210, 216, RoomType.BattleRoom.getValue() + 1, 0);
        addParams(217, 223, RoomType.EliteBattleRoom.getValue() + 1, 0);
        addParams(224, 230, RoomType.BossRoom.getValue() + 1, 0);
        addParams(231, 237, RoomType.FinalBossRoom.getValue() + 1, 0);
        addParams(238, 244, RoomType.ShopRoom.getValue() + 1, 0);
        addParams(245, 251, RoomType.EventRoom.getValue() + 1, 0);
    }
    
    private static void addParam(int achievementId, int param1, int param2) {
        var data = GameData.getAchievementDataTable().get(achievementId);
        if (data == null) return;
        
        data.setParams(param1, param2);
    }
    
    private static void addParams(int start, int end, int param1, int param2) {
        for (int id = start; id <= end; id++) {
            addParam(id, param1, param2);
        }
    }
}
