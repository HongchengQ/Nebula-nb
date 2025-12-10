package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.GameData;
import emu.nebula.data.ResourceType;
import emu.nebula.data.ResourceType.LoadPriority;
import emu.nebula.game.instance.InstanceData;
import emu.nebula.game.inventory.ItemRewardList;
import emu.nebula.game.inventory.ItemRewardParam;
import emu.nebula.util.JsonUtils;

import lombok.Getter;

@Getter
@ResourceType(name = "WeekBossLevel.json", loadPriority = LoadPriority.LOW)
public class WeekBossLevelDef extends BaseDef implements InstanceData {
    private int Id;
    private int Difficulty;
    private int PreLevelId;
    private int NeedWorldClass;
    private String BaseAwardPreview;
    
    private transient ItemRewardList firstRewards;
    private transient ItemRewardList rewards;
    
    @Override
    public int getId() {
        return Id;
    }
    
    public int getEnergyConsume() {
        return 0;
    }
    
    @Override
    public void onLoad() {
        // Init reward lists
        this.firstRewards = new ItemRewardList();
        this.rewards = new ItemRewardList();
        
        // Parse rewards
        var awards = JsonUtils.decodeList(this.BaseAwardPreview, int[].class);
        if (awards == null) {
            return;
        }
        
        for (int[] award : awards) {
            int itemId = award[0];
            int min = award[1];
            int max = award[1];
            boolean isFirst = award[award.length - 1] == 1;
            
            // Set reward count based on difficulty
            if (min == -1) {
                min = this.Difficulty;
                max = this.Difficulty;
                
                var item = GameData.getItemDataTable().get(itemId);
                if (item != null) {
                    switch (item.getRarity()) {
                        case 2:
                            max = this.Difficulty * 3;
                            break;
                        case 3:
                            min = this.Difficulty * 2;
                            max = this.Difficulty * 6;
                            break;
                        case 4:
                            min = this.Difficulty * 3;
                            max = this.Difficulty * 9;
                            break;
                    }
                }
            }
            
            // Create reward param
            var reward = new ItemRewardParam(itemId, min, max);
            
            if (isFirst) {
                this.firstRewards.add(reward);
            } else {
                this.rewards.add(reward);
            }
        }
    }
}
