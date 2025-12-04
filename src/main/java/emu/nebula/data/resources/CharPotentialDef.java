package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import lombok.Getter;

@Getter
@ResourceType(name = "CharPotential.json")
public class CharPotentialDef extends BaseDef {
    private int Id;
    
    private int[] MasterSpecificPotentialIds;
    private int[] AssistSpecificPotentialIds;
    private int[] CommonPotentialIds;
    private int[] MasterNormalPotentialIds;
    private int[] AssistNormalPotentialIds;
    
    @Override
    public int getId() {
        return Id;
    }
}
