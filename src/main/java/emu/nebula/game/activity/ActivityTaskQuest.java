package emu.nebula.game.activity;

import dev.morphia.annotations.Entity;

import emu.nebula.data.resources.ActivityTaskDef;
import emu.nebula.proto.Public.Quest;
import emu.nebula.proto.Public.QuestProgress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Entity(useDiscriminator = false)
public class ActivityTaskQuest {
    private int id;
    private int cond;
    
    private int curProgress;
    private int maxProgress;
    
    @Setter
    private boolean claimed;
    
    @Deprecated
    public ActivityTaskQuest() {
        
    }
    
    public ActivityTaskQuest(ActivityTaskDef data) {
        this.id = data.getId();
        this.cond = data.getCompleteCond();
        this.maxProgress = data.getAimNumShow();
    }

    public void resetProgress() {
        this.curProgress = 0;
        this.claimed = false;
    }
    
    public boolean isComplete() {
        return this.curProgress >= this.maxProgress;
    }
    
    private int getStatus() {
        if (this.isClaimed()) {
            return 2;
        } else if (this.isComplete()) {
            return 1;
        }
        
        return 0;
    }

    public boolean trigger(int condition, int progress, int param1, int param2) {
        // Sanity check
        if (this.isComplete()) {
            return false;
        }
        
        // Skip if not the correct condition
        if (this.cond != condition) {
            return false;
        }
        
        // Check quest param TODO
        
        // Get new progress
        int newProgress = Math.min(this.curProgress + progress, this.maxProgress);
        
        // Set
        if (this.curProgress != newProgress) {
            this.curProgress = newProgress;
            return true;
        }
        
        return false;
    }
    
    // Proto

    public Quest toProto() {
        var progress = QuestProgress.newInstance()
                .setCur(this.getCurProgress())
                .setMax(this.getMaxProgress());
        
        var proto = Quest.newInstance()
                .setId(this.getId())
                .setStatus(this.getStatus())
                .addProgress(progress);
        
        return proto;
    }
}
