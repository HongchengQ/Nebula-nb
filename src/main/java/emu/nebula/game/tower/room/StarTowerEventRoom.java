package emu.nebula.game.tower.room;

import java.util.Arrays;
import java.util.Objects;

import emu.nebula.GameConstants;
import emu.nebula.data.GameData;
import emu.nebula.data.resources.StarTowerEventDef;
import emu.nebula.data.resources.StarTowerStageDef;
import emu.nebula.game.tower.StarTowerGame;
import emu.nebula.game.tower.cases.StarTowerBaseCase;
import emu.nebula.game.tower.cases.StarTowerNpcEventCase;
import emu.nebula.game.tower.cases.StarTowerSyncHPCase;
import emu.nebula.util.Utils;

import lombok.Getter;

@Getter
public class StarTowerEventRoom extends StarTowerBaseRoom {
    
    public StarTowerEventRoom(StarTowerGame game, StarTowerStageDef stage) {
        super(game, stage);
    }
    
    private StarTowerEventDef getRandomEvent() {
        /*
        var list = GameData.getStarTowerEventDataTable()
                .values()
                .stream()
                .toList();
        */
        
        var list = Arrays.stream(GameConstants.TOWER_EVENTS_IDS)
                .mapToObj(GameData.getStarTowerEventDataTable()::get)
                .filter(Objects::nonNull)
                .toList();
        
        if (list.isEmpty()) {
            return null;
        }
        
        return Utils.randomElement(list);
    }
    
    public StarTowerBaseCase createNpcEvent() {
        // Get random event
        var event = this.getRandomEvent();
        
        if (event == null) {
            return null;
        }
        
        // Get random npc
        int npcId = Utils.randomElement(event.getRelatedNPCs());
        
        // Create case with event
        return new StarTowerNpcEventCase(npcId, event);
    }

    @Override
    public void onEnter() {
        // Create npc
        this.addCase(this.createNpcEvent());
        
        // Create sync hp case
        this.addCase(new StarTowerSyncHPCase());
        
        // Create door case
        this.createExit();
    }
}
