package emu.nebula.game.tower;

import emu.nebula.proto.PublicStarTower.PotentialInfo;
import lombok.Getter;

@Getter
public class StarTowerPotentialInfo {
    private int id;
    private int level;
    
    public StarTowerPotentialInfo(int id, int level) {
        this.id = id;
        this.level = level;
    }
    
    // Proto
    
    public PotentialInfo toProto() {
        var proto = PotentialInfo.newInstance()
                .setTid(this.getId())
                .setLevel(this.getLevel());
        
        return proto;
    }
    
}
