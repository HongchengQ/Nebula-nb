package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.game.character.ElementType;
import lombok.Getter;

@Getter
@ResourceType(name = "Disc.json")
public class DiscDef extends BaseDef {
    private int Id;
    private boolean Visible;
    private boolean Available;
    private int EET;
    
    private int StrengthenGroupId;
    private int PromoteGroupId;
    private int TransformItemId;
    private int[] MaxStarTransformItem;
    private int[] ReadReward;
    
    private int SecondarySkillGroupId1;
    private int SecondarySkillGroupId2;
    private int SubNoteSkillGroupId;
    
    private transient ElementType elementType;
    
    @Override
    public int getId() {
        return Id;
    }

    @Override
    public void onLoad() {
        this.elementType = ElementType.getByValue(this.EET);
    }
}
