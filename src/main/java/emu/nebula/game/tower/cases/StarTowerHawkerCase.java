package emu.nebula.game.tower.cases;

import java.util.HashMap;
import java.util.Map;

import emu.nebula.GameConstants;
import emu.nebula.game.tower.StarTowerShopGoods;
import emu.nebula.proto.PublicStarTower.HawkerCaseData;
import emu.nebula.proto.PublicStarTower.HawkerGoods;
import emu.nebula.proto.PublicStarTower.StarTowerRoomCase;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractReq;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractResp;
import lombok.Getter;

@Getter
public class StarTowerHawkerCase extends StarTowerBaseCase {
    private Map<Integer, StarTowerShopGoods> goods;
    
    public StarTowerHawkerCase() {
        this.goods = new HashMap<>();
    }

    @Override
    public CaseType getType() {
        return CaseType.Hawker;
    }
    
    @Override
    public void onRegister() {
        this.initGoods();
    }
    
    public void initGoods() {
        // Clear goods
        this.getGoods().clear();
        
        // Add goods
        for (int i = 0; i < getModifiers().getShopGoodsCount(); i++) {
            this.addGoods(new StarTowerShopGoods(1, 1, 200));
        }
        
        // TODO apply discounts based on star tower talents
    }
    
    public void addGoods(StarTowerShopGoods goods) {
        this.getGoods().put(getGoods().size() + 1, goods);
    }
    
    @Override
    public StarTowerInteractResp interact(StarTowerInteractReq req, StarTowerInteractResp rsp) {
        // Set nil resp
        rsp.getMutableNilResp();
        
        // Get hawker req
        var hawker = req.getHawkerReq();
        
        if (hawker.hasReRoll()) {
            // Refresh shop items
            this.refresh(rsp);
        } else if (hawker.hasSid()) {
            // Buy shop items
            this.buy(hawker.getSid(), rsp);
        }
        
        // Success
        return rsp;
    }
    
    private void refresh(StarTowerInteractResp rsp) {
        // Check if we can refresh
        if (this.getModifiers().getShopRerollCount() <= 0) {
            return;
        }
        
        // Make sure we have enough currency
        int coin = this.getGame().getResCount(GameConstants.STAR_TOWER_COIN_ITEM_ID);
        int price = this.getModifiers().getShopRerollPrice();
        
        if (coin < price) {
            return;
        }
        
        // Create new goods
        this.initGoods();
        
        // Set in proto
        rsp.getMutableSelectResp()
            .setHawkerCase(this.toHawkerCaseProto());
        
        // Remove coins
        var change = this.getGame().addItem(GameConstants.STAR_TOWER_COIN_ITEM_ID, -price);
        
        // Set change info
        rsp.setChange(change.toProto());
        
        // Consume reroll count
        this.getGame().getModifiers().consumeShopReroll();
    }
    
    private void buy(int sid, StarTowerInteractResp rsp) {
        // Get goods
        var goods = this.getGoods().get(sid);
        if (goods == null) {
            return;
        }
        
        // Make sure we have enough currency
        int coin = this.getGame().getResCount(GameConstants.STAR_TOWER_COIN_ITEM_ID);
        int price = goods.getPrice();
        
        if (coin < price || goods.isSold()) {
            return;
        }
        
        // Mark goods as sold
        goods.markAsSold();
        
        // Add case
        this.getGame().addCase(rsp.getMutableCases(), this.getGame().createPotentialSelector());
        
        // Remove coins
        var change = this.getGame().addItem(GameConstants.STAR_TOWER_COIN_ITEM_ID, -price);
        
        // Set change info
        rsp.setChange(change.toProto());
    }
    
    // Proto
    
    private HawkerCaseData toHawkerCaseProto() {
        var hawker = HawkerCaseData.newInstance();
        
        if (this.getModifiers().getShopRerollCount() > 0) {
            hawker.setCanReRoll(true);
            hawker.setReRollTimes(this.getModifiers().getShopRerollCount());
            hawker.setReRollPrice(this.getModifiers().getShopRerollPrice());
        }
        
        for (var entry : this.getGoods().entrySet()) {
            var sid = entry.getKey();
            var goods = entry.getValue();
            
            var info = HawkerGoods.newInstance()
                    .setIdx(goods.getGoodsId())
                    .setSid(sid)
                    .setType(goods.getType())
                    .setGoodsId(102) // ?
                    .setPrice(goods.getPrice())
                    .setTag(1);
            
            hawker.addList(info);
        }
        
        return hawker;
    }
    
    @Override
    public void encodeProto(StarTowerRoomCase proto) {
        proto.setHawkerCase(this.toHawkerCaseProto());
    }
}
