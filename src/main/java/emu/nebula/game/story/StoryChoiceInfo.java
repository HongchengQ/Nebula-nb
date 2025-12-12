package emu.nebula.game.story;

import dev.morphia.annotations.Entity;
import emu.nebula.proto.Public.StoryChoice;
import lombok.Getter;

@Getter
@Entity(useDiscriminator = false)
public class StoryChoiceInfo {
    private int group;
    private int value;
    
    @Deprecated
    public StoryChoiceInfo() {
        // Morphia only
    }
    
    public StoryChoiceInfo(int group, int value) {
        this.group = group;
        this.value = value;
    }
    
    // Proto
    
    public StoryChoice toProto() {
        var proto = StoryChoice.newInstance()
                .setGroup(this.getGroup())
                .setValue(this.getValue());
        
        return proto;
    }
}
