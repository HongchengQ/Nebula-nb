package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;

import lombok.Getter;

@Getter
@ResourceType(name = "SignIn.json")
public class SignInDef extends BaseDef {
    private int Group;
    private int Day;
    private int ItemId;
    private int ItemQty;
    
    @Override
    public int getId() {
        return (this.Group << 16) + this.Day;
    }
}
