package emu.nebula.game.tower;

import lombok.Getter;

/**
 * Data class to hold various modifiers for star tower.
 */
@Getter
public class StarTowerModifiers {
    private StarTowerGame game;
    
    // Strengthen machines
    private boolean enableEndStrengthen;
    private boolean enableShopStrengthen;
    
    private boolean freeStrengthen;
    private int strengthenDiscount;
    
    // Bonus max potential level
    private int bonusMaxPotentialLevel;
    
    // Shop
    private int shopGoodsCount;
    
    private int shopRerollCount;
    private int shopRerollPrice;
    
    private boolean shopDiscountTier1;
    private boolean shopDiscountTier2;
    private boolean shopDiscountTier3;
    
    public StarTowerModifiers(StarTowerGame game) {
        this.game = game;
        
        // Strengthen machines
        this.enableEndStrengthen = this.hasGrowthNode(10601) && game.getDifficulty() >= 2;
        this.enableShopStrengthen = this.hasGrowthNode(20301) && game.getDifficulty() >= 4;
        
        this.freeStrengthen = this.hasGrowthNode(10801);
        
        if (this.hasGrowthNode(30402)) {
            this.strengthenDiscount += 60;
        }
        if (this.hasGrowthNode(30102)) {
            this.strengthenDiscount += 30;
        }
        
        // Bonus max level
        if (this.hasGrowthNode(30301)) {
            this.bonusMaxPotentialLevel = 6;
        } else if (this.hasGrowthNode(20601)) {
            this.bonusMaxPotentialLevel = 4;
        }
        
        // Shop
        if (this.hasGrowthNode(20702)) {
            this.shopGoodsCount = 8;
        } else if (this.hasGrowthNode(20402)) {
            this.shopGoodsCount = 6;
        } else if (this.hasGrowthNode(10402)) {
            this.shopGoodsCount = 4;
        } else {
            this.shopGoodsCount = 2;
        }
        
        if (this.hasGrowthNode(20902)) {
            this.shopRerollCount++;
        }
        if (this.hasGrowthNode(30601)) {
            this.shopRerollCount++;
        }
        
        if (this.shopRerollCount > 0) {
            this.shopRerollPrice = 100;
        }
        
        this.shopDiscountTier1 = this.hasGrowthNode(20202) && game.getDifficulty() >= 3;
        this.shopDiscountTier2 = this.hasGrowthNode(20502) && game.getDifficulty() >= 4;
        this.shopDiscountTier3 = this.hasGrowthNode(20802) && game.getDifficulty() >= 5;
    }
    
    public boolean hasGrowthNode(int nodeId) {
        return this.getGame().getManager().hasGrowthNode(nodeId);
    }
    
    public int getStartingCoin() {
        int gold = 0;
        
        if (this.hasGrowthNode(10103)) {
            gold += 50;
        } if (this.hasGrowthNode(10403)) {
            gold += 100;
        } if (this.hasGrowthNode(10702)) {
            gold += 200;
        }
        
        return gold;
    }

    public void setFreeStrengthen(boolean b) {
        this.freeStrengthen = b;
    }

    public void consumeShopReroll() {
        this.shopRerollCount = Math.max(this.shopRerollCount - 1, 0);
    }
}
