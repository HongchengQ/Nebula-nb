package emu.nebula.game.activity.type;

import java.util.HashMap;
import java.util.Map;

import dev.morphia.annotations.Entity;

import emu.nebula.data.GameData;
import emu.nebula.data.resources.ActivityDef;
import emu.nebula.data.resources.ActivityShopControlDef;
import emu.nebula.data.resources.ActivityShopDef;
import emu.nebula.game.activity.ActivityManager;
import emu.nebula.game.activity.GameActivity;
import emu.nebula.game.inventory.ItemParamMap;
import emu.nebula.game.player.PlayerChangeInfo;
import emu.nebula.proto.ActivityDetail.ActivityMsg;
import emu.nebula.proto.Public.BoughtGoods;
import emu.nebula.proto.Public.ResidentShop;

import lombok.Getter;

@Getter
@Entity
public class ShopActivity extends GameActivity {
    private Map<Integer, ActivityShopInfo> shops;
    
    @Deprecated // Morphia only
    public ShopActivity() {
        
    }
    
    public ShopActivity(ActivityManager manager, ActivityDef data) {
        super(manager, data);
        this.shops = new HashMap<>();
        
        // Load shops
        var control = GameData.getActivityShopControlDataTable().get(data.getId());
        if (control != null) {
            this.initShops(control);
        }
    }
    
    private void initShops(ActivityShopControlDef control) {
        for (int id : control.getShopIds()) {
            var data = GameData.getActivityShopDataTable().get(id);
            if (data == null) {
                continue;
            }
            
            // Create resident shop
            var shop = new ActivityShopInfo(data);
            
            // Add
            this.getShops().put(data.getId(), shop);
        }
    }

    public PlayerChangeInfo buy(int shopId, int goodsId, int count) {
        // Get shop
        var shop = this.getShops().get(shopId);
        if (shop == null) return null;
        
        // Get shop data
        var data = GameData.getActivityShopDataTable().get(shopId);
        if (data == null) return null;
        
        // Get goods
        var goods = data.getGoods().get(goodsId);
        if (goods == null) return null;
        
        // Check limit
        if (goods.getMaximumLimit() > 0) {
            var limit = goods.getMaximumLimit() - shop.getBoughtCount(goodsId);
            
            if (count > limit) {
                return null;
            }
        }
        
        // Purchase
        var change = getPlayer().getInventory().buyItem(data.getCurrencyItemId(), goods.getPrice(), goods.getItems(), count);
        
        // Purchase failed
        if (change == null) {
            return null;
        }
        
        // Set log
        shop.getBoughtGoods().add(goodsId, count);
        
        // Save
        this.save();
        
        // Set extra data
        change.setExtraData(shop);
        
        // Success
        return change;
    }

    // Proto

    @Override
    public void encodeActivityMsg(ActivityMsg msg) {
        var proto = msg.getMutableShop();
        
        for (var entry : this.getShops().entrySet()) {
            var id = entry.getKey();
            var shop = entry.getValue();
            
            var info = shop.toProto()
                    .setId(id);
            
            proto.addShops(info);
        }
    }

    @Getter
    @Entity(useDiscriminator = false)
    public static class ActivityShopInfo {
        private ItemParamMap boughtGoods;
        
        @Deprecated // Morphia only
        public ActivityShopInfo() {
            
        }
        
        public ActivityShopInfo(ActivityShopDef data) {
            this.boughtGoods = new ItemParamMap();
        }
        
        public int getBoughtCount(int goodsId) {
            return this.getBoughtGoods().get(goodsId);
        }
        
        // Proto

        public ResidentShop toProto() {
            var proto = ResidentShop.newInstance();
            
            for (var item : this.getBoughtGoods().int2IntEntrySet()) {
                var info = BoughtGoods.newInstance()
                        .setId(item.getIntKey())
                        .setNumber(item.getIntValue());
                
                proto.addInfos(info);
            }
            
            return proto;
        }
    }
}
