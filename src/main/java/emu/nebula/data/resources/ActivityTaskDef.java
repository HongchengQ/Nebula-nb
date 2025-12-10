package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.game.inventory.ItemParamMap;
import lombok.Getter;

@Getter
@ResourceType(name = "ActivityTask.json")
public class ActivityTaskDef extends BaseDef {
    private int Id;
    private int ActivityTaskGroupId;
    
    private int CompleteCond;
    private int AimNumShow;
    
    private int Tid1;
    private int Qty1;
    private int Tid2;
    private int Qty2;
    
    private transient ItemParamMap rewards;
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        this.rewards = new ItemParamMap();
        
        if (this.Tid1 > 0) {
            this.rewards.add(this.Tid1, this.Qty1);
        }
        if (this.Tid2 > 0) {
            this.rewards.add(this.Tid2, this.Qty2);
        }
    }
}
