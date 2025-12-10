package emu.nebula.game.activity.type;

import java.util.HashMap;
import java.util.Map;

import dev.morphia.annotations.Entity;
import emu.nebula.data.GameData;
import emu.nebula.data.resources.ActivityDef;
import emu.nebula.game.activity.ActivityManager;
import emu.nebula.game.activity.ActivityTaskQuest;
import emu.nebula.game.activity.GameActivity;
import emu.nebula.proto.ActivityDetail.ActivityMsg;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import lombok.Getter;

@Getter
@Entity
public class TaskActivity extends GameActivity {
    private Map<Integer, ActivityTaskQuest> quests;
    private IntSet completedGroups;
    
    @Deprecated // Morphia only
    public TaskActivity() {
        
    }
    
    public TaskActivity(ActivityManager manager, ActivityDef data) {
        super(manager, data);
        this.quests = new HashMap<>();
        this.completedGroups = new IntOpenHashSet();
        
        var groupSet = new IntOpenHashSet();
        for (var group : GameData.getActivityTaskGroupDataTable()) {
            if (group.getActivityId() != this.getId()) {
                continue;
            }
            
            groupSet.add(group.getId());
        }
        
        for (var task : GameData.getActivityTaskDataTable()) {
            if (!groupSet.contains(task.getActivityTaskGroupId())) {
                continue;
            }
            
            var quest = new ActivityTaskQuest(task);
            
            this.getQuests().put(quest.getId(), quest);
        }
    }
    
    // Proto

    @Override
    public void encodeActivityMsg(ActivityMsg msg) {
        var proto = msg.getMutableTask();
        
        for (int id : this.getCompletedGroups()) {
            proto.addGroupIds(id);
        }
        
        var tasks = proto.getMutableActivityTasks();
        
        for (var quest : this.getQuests().values()) {
            tasks.addList(quest.toProto());
        }
    }

}
