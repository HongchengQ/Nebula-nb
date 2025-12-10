package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;

import lombok.Getter;

@Getter
@ResourceType(name = "ActivityShopControl.json")
public class ActivityShopControlDef extends BaseDef {
    private int Id;
    private int[] ShopIds;
    
    @Override
    public int getId() {
        return Id;
    }
}
