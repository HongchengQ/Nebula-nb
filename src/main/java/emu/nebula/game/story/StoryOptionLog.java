package emu.nebula.game.story;

import java.util.ArrayList;
import java.util.List;

import dev.morphia.annotations.Entity;

import emu.nebula.proto.Public.Story;
import emu.nebula.proto.StorySett.StoryOptions;

import lombok.Getter;

import us.hebi.quickbuf.RepeatedMessage;

@Getter
@Entity(useDiscriminator = false)
public class StoryOptionLog {
    private List<StoryChoiceInfo> major;
    private List<StoryChoiceInfo> personality;
    
    public StoryOptionLog() {
        
    }
    
    public int getMajorOptionSize() {
        if (this.major == null) {
            return 0;
        }
        
        return this.major.size();
    }
    
    public boolean hasMajorOption(int group, int choice) {
        if (this.major == null) {
            return false;
        }
        
        return this.major.stream()
                .filter(c -> c.getGroup() == group && c.getValue() == choice)
                .findFirst()
                .isPresent();
    }
    
    public boolean addMajorOption(int group, int choice) {
        if (this.major == null) {
            this.major = new ArrayList<>();
        }
        
        return this.major.add(new StoryChoiceInfo(group, choice));
    }
    
    public boolean settleMajor(RepeatedMessage<StoryOptions> options) {
        boolean success = false;
        
        for (var option : options) {
            // Sanity check
            if (this.getMajorOptionSize() >= 5) {
                break;
            }
            
            // Skip if we already have this choice
            if (this.hasMajorOption(option.getGroup(), option.getChoice())) {
                continue;
            }
            
            // Add
            this.addMajorOption(option.getGroup(), option.getChoice());
            
            // Set success flag
            success = true;
        }
        
        return success;
    }
    
    public int getPersonalityOptionSize() {
        if (this.personality == null) {
            return 0;
        }
        
        return this.personality.size();
    }
    
    public boolean hasPersonalityOption(int group, int choice) {
        if (this.personality == null) {
            return false;
        }
        
        return this.personality.stream()
                .filter(c -> c.getGroup() == group && c.getValue() == choice)
                .findFirst()
                .isPresent();
    }
    
    public boolean addPersonalityOption(int group, int choice) {
        if (this.personality == null) {
            this.personality = new ArrayList<>();
        }
        
        return this.personality.add(new StoryChoiceInfo(group, choice));
    }

    public boolean settlePersonality(RepeatedMessage<StoryOptions> options) {
        boolean success = false;
        
        for (var option : options) {
            // Sanity check
            if (this.getPersonalityOptionSize() >= 5) {
                break;
            }
            
            // Skip if we already have this choice
            if (this.hasPersonalityOption(option.getGroup(), option.getChoice())) {
                continue;
            }
            
            // Add
            this.addPersonalityOption(option.getGroup(), option.getChoice());
            
            // Set success flag
            success = true;
        }
        
        return success;
    }
    
    // Proto

    public void encodeStoryProto(Story proto) {
        if (this.major != null) {
            for (var choice : this.major) {
                proto.addMajor(choice.toProto());
            }
        }
        
        if (this.personality != null) {
            for (var choice : this.personality) {
                proto.addMajor(choice.toProto());
            }
        }
    }
    
}
