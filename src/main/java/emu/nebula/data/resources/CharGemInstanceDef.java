package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.game.instance.InstanceData;
import emu.nebula.game.inventory.ItemParamMap;
import emu.nebula.util.JsonUtils;
import lombok.Getter;

@Getter
@ResourceType(name = "CharGemInstance.json")
public class CharGemInstanceDef extends BaseDef implements InstanceData {
    private int Id;
    private int PreLevelId;
    private int PreLevelStar;
    private int NeedWorldClass;
    private int EnergyConsume;
    private String BaseAwardPreview;
    
    private transient ItemParamMap firstRewards;
    private transient ItemParamMap rewards;
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        // Init reward maps
        this.firstRewards = new ItemParamMap();
        this.rewards = new ItemParamMap();
        
        // Parse rewards
        var awards = JsonUtils.decodeList(this.BaseAwardPreview, int[].class);
        if (awards == null) {
            return;
        }
        
        for (int[] award : awards) {
            int itemId = award[0];
            int count = award[1];
            boolean isFirst = award[2] == 1;
            
            if (isFirst) {
                this.firstRewards.put(itemId, count);
            } else {
                this.rewards.put(itemId, count);
            }
        }
    }
}
