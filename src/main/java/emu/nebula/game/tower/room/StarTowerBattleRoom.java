package emu.nebula.game.tower.room;

import emu.nebula.data.resources.StarTowerStageDef;
import emu.nebula.game.tower.StarTowerGame;
import emu.nebula.game.tower.cases.StarTowerBattleCase;
import emu.nebula.game.tower.cases.StarTowerSyncHPCase;
import lombok.Getter;

@Getter
public class StarTowerBattleRoom extends StarTowerBaseRoom {
    public StarTowerBattleRoom(StarTowerGame game, StarTowerStageDef stage) {
        super(game, stage);
    }

    @Override
    public void onEnter() {
        // Create battle case
        this.addCase(new StarTowerBattleCase());
        
        // Create sync hp case
        this.addCase(new StarTowerSyncHPCase());
    }
}
