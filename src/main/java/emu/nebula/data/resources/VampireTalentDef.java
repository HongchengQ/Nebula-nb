package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import lombok.Getter;

@Getter
@ResourceType(name = "VampireTalent.json")
public class VampireTalentDef extends BaseDef {
    private int Id;
    private int[] Prev;
    private int Point;
    
    @Override
    public int getId() {
        return Id;
    }
}
