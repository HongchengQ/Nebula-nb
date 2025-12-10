package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.game.inventory.ItemParamMap;
import lombok.Getter;

@Getter
@ResourceType(name = "ActivityTaskGroup.json")
public class ActivityTaskGroupDef extends BaseDef {
    private int Id;
    private int ActivityId;
    
    private int Reward1;
    private int RewardQty1;
    private int Reward2;
    private int RewardQty2;
    private int Reward3;
    private int RewardQty3;
    private int Reward4;
    private int RewardQty4;
    private int Reward5;
    private int RewardQty5;
    private int Reward6;
    private int RewardQty6;
    
    private transient ItemParamMap rewards;
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        this.rewards = new ItemParamMap();
        
        if (this.Reward1 > 0) {
            this.rewards.add(this.Reward1, this.RewardQty1);
        }
        if (this.Reward2 > 0) {
            this.rewards.add(this.Reward2, this.RewardQty2);
        }
        if (this.Reward3 > 0) {
            this.rewards.add(this.Reward3, this.RewardQty3);
        }
        if (this.Reward4 > 0) {
            this.rewards.add(this.Reward4, this.RewardQty4);
        }
        if (this.Reward5 > 0) {
            this.rewards.add(this.Reward5, this.RewardQty5);
        }
        if (this.Reward6 > 0) {
            this.rewards.add(this.Reward6, this.RewardQty6);
        }
    }
}
