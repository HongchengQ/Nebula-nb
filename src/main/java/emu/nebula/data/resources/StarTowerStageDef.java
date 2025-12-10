package emu.nebula.data.resources;

import com.google.gson.annotations.SerializedName;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.game.tower.room.RoomType;
import lombok.Getter;

@Getter
@ResourceType(name = "StarTowerStage.json")
public class StarTowerStageDef extends BaseDef {
    private int Id;
    private int Stage;
    private int Floor;
    private int InteriorCurrencyQuantity;
    @SerializedName("RoomType")
    private int RoomTypeValue;
    
    private transient RoomType roomType;
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        this.roomType = RoomType.getByValue(this.RoomTypeValue);
    }
}
