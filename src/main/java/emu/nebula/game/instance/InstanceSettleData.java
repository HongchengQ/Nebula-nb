package emu.nebula.game.instance;

import emu.nebula.game.inventory.ItemParamMap;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InstanceSettleData {
    private boolean isWin;
    private boolean isFirst;
    private int exp;
    
    private ItemParamMap firstRewards;
    private ItemParamMap rewards;
    
    public InstanceSettleData() {
        
    }
    
    public void generateRewards(InstanceData data) {
        this.generateRewards(data, 0);
    }
    
    public void generateRewards(InstanceData data, int rewardType) {
        if (this.isFirst) {
            this.firstRewards = data.getFirstRewards(rewardType).generate();
        }
        
        this.rewards = data.getRewards(rewardType).generate();
    }
}
