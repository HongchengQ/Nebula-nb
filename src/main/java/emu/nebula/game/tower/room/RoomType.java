package emu.nebula.game.tower.room;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

public enum RoomType {
    BattleRoom       (0),
    EliteBattleRoom  (1),
    BossRoom         (2),
    FinalBossRoom    (3),
    DangerRoom       (4),
    HorrorRoom       (5),
    ShopRoom         (6),
    EventRoom        (7),
    UnifyBattleRoom  (15);
    
    @Getter
    private final int value;
    private final static Int2ObjectMap<RoomType> map = new Int2ObjectOpenHashMap<>();
    
    static {
        for (RoomType type : RoomType.values()) {
            map.put(type.getValue(), type);
        }
    }
    
    private RoomType(int value) {
        this.value = value;
    }
    
    public static RoomType getByValue(int value) {
        return map.get(value);
    }
}
