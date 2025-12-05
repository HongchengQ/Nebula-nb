package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.GameData;
import emu.nebula.data.ResourceType;
import emu.nebula.data.ResourceType.LoadPriority;

import lombok.Getter;

/**
 * We don't need a DataTable for this, since we are only using this class to verify event options for the client
 */
@Getter
@ResourceType(name = "EventOptions.json", loadPriority = LoadPriority.LOW)
public class EventOptionsDef extends BaseDef {
    private int Id;
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        // Get event
        var event = GameData.getStarTowerEventDataTable().get(this.Id / 100);
        if (event == null) {
            return;
        }
        
        // Add to avaliable options
        event.getOptionIds().add(this.getId());
    }
}
