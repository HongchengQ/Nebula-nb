package emu.nebula.game.tower.room;

import emu.nebula.data.resources.StarTowerStageDef;
import emu.nebula.game.tower.StarTowerGame;
import emu.nebula.game.tower.cases.StarTowerHawkerCase;
import emu.nebula.game.tower.cases.StarTowerStrengthenMachineCase;
import emu.nebula.game.tower.cases.StarTowerSyncHPCase;
import lombok.Getter;

@Getter
public class StarTowerHawkerRoom extends StarTowerBaseRoom {
    
    public StarTowerHawkerRoom(StarTowerGame game, StarTowerStageDef stage) {
        super(game, stage);
    }

    @Override
    public void onEnter() {
        // Create hawker case (shop)
        this.addCase(new StarTowerHawkerCase());
        
        // Create strengthen machine
        if (this.getModifiers().isEnableShopStrengthen()) {
            this.addCase(new StarTowerStrengthenMachineCase());
        }
        
        // Create sync hp case
        this.addCase(new StarTowerSyncHPCase());
        
        // Create door case
        this.createExit();
    }
}
