package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.GameData;
import emu.nebula.data.ResourceType;
import emu.nebula.data.ResourceType.LoadPriority;
import emu.nebula.game.inventory.ItemParamMap;
import lombok.Getter;

@Getter
@ResourceType(name = "ActivityGoods.json", loadPriority = LoadPriority.LOW)
public class ActivityGoodsDef extends BaseDef {
    private int Id;
    private int ShopId;
    
    private int ItemId;
    private int ItemQuantity;
    private int MaximumLimit;
    private int Price;
    
    private transient ItemParamMap items;
    
    @Override
    public int getId() {
        return Id;
    }

    @Override
    public void onLoad() {
        var shop = GameData.getActivityShopDataTable().get(this.getShopId());
        if (shop != null) {
            shop.getGoods().put(this.getId(), this);
        }
        
        this.items = new ItemParamMap();
        this.items.add(this.ItemId, this.ItemQuantity);
    }
}
