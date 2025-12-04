package emu.nebula.game.tower.room;

import emu.nebula.data.resources.StarTowerStageDef;
import emu.nebula.game.tower.StarTowerGame;
import emu.nebula.game.tower.cases.StarTowerBattleCase;
import emu.nebula.game.tower.cases.StarTowerSyncHPCase;
import emu.nebula.util.Utils;
import lombok.Getter;

@Getter
public class StarTowerBattleRoom extends StarTowerBaseRoom {
    
    public StarTowerBattleRoom(StarTowerGame game, StarTowerStageDef stage) {
        super(game, stage);
    }

    @Override
    public void onEnter() {
        // Create battle case
        this.getGame().setPendingSubNotes(Utils.randomRange(1, 3));
        this.addCase(new StarTowerBattleCase(this.getGame().getPendingSubNotes()));
        
        // Create sync hp case
        this.addCase(new StarTowerSyncHPCase());
    }
}
