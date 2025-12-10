package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

@Getter
@ResourceType(name = "ActivityShop.json")
public class ActivityShopDef extends BaseDef {
    private int Id;
    private int CurrencyItemId;
    private int ExchangeItemId;
    private double Rate;
    
    private Int2ObjectMap<ActivityGoodsDef> goods;
    
    @Override
    public int getId() {
        return Id;
    }

    @Override
    public void onLoad() {
        this.goods = new Int2ObjectOpenHashMap<>();
    }
}
