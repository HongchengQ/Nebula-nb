package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;

@Getter
@ResourceType(name = "StarTowerEvent.json")
public class StarTowerEventDef extends BaseDef {
    private int Id;
    private int[] RelatedNPCs;
    
    private transient IntList optionIds;
    
    @Override
    public int getId() {
        return Id;
    }
    
    /**
     * Returns a deep copy of our option ids
     */
    public IntList getClonedOptionIds() {
        var list = new IntArrayList();
        list.addAll(this.getOptionIds());
        return list;
    }
    
    @Override
    public void onLoad() {
        this.optionIds = new IntArrayList();
    }
}
