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
    
    // Bonus potential levels
    private double bonusStrengthenChance = 0;
    private double bonusPotentialChance = 0;
    private int bonusPotentialLevel = 0;
    
    public StarTowerModifiers(StarTowerGame game) {
        this.game = game;
        
        // Strengthen machines
        this.enableEndStrengthen = game.getDifficulty() >= 2 && this.hasGrowthNode(10601);
        this.enableShopStrengthen = game.getDifficulty() >= 4 && this.hasGrowthNode(20301);
        
        this.freeStrengthen = this.hasGrowthNode(10801);
        
        if (this.hasGrowthNode(30402)) {
            this.strengthenDiscount += 60;
        }
        if (this.hasGrowthNode(30102)) {
            this.strengthenDiscount += 30;
        }
        
        // Bonus potential max level (Ocean of Souls)
        if (this.hasGrowthNode(30301)) {
            this.bonusMaxPotentialLevel = 6;
        } else if (this.hasGrowthNode(20601)) {
            this.bonusMaxPotentialLevel = 4;
        }
        
        // Shop (Monolith Premium)
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
        
        // Shop discount (Member Discount)
        this.shopDiscountTier1 = game.getDifficulty() >= 3 && this.hasGrowthNode(20202);
        this.shopDiscountTier2 = game.getDifficulty() >= 4 && this.hasGrowthNode(20502);
        this.shopDiscountTier3 = game.getDifficulty() >= 5 && this.hasGrowthNode(20802);
        
        // Bonus potential levels (Potential Boost)
        if (game.getDifficulty() >= 7 && this.hasGrowthNode(30802)) {
            this.bonusStrengthenChance = 0.3;
        } else if (game.getDifficulty() >= 6 && this.hasGrowthNode(30502)) {
            this.bonusStrengthenChance = 0.2;
        } else if (game.getDifficulty() >= 6 && this.hasGrowthNode(30202)) {
            this.bonusStrengthenChance = 0.1;
        }
        
        // Bonus potential levels (Butterflies Inside)
        if (game.getDifficulty() >= 7 && this.hasGrowthNode(30901)) {
            this.bonusPotentialChance = 0.3;
            this.bonusMaxPotentialLevel = 2;
        } else if (game.getDifficulty() >= 7 && this.hasGrowthNode(30801)) {
            this.bonusPotentialChance = 0.2;
            this.bonusMaxPotentialLevel = 1;
        } else if (game.getDifficulty() >= 6 && this.hasGrowthNode(30201)) {
            this.bonusPotentialChance = 0.1;
            this.bonusMaxPotentialLevel = 1;
        } else if (game.getDifficulty() >= 5 && this.hasGrowthNode(20801)) {
            this.bonusPotentialChance = 0.05;
            this.bonusMaxPotentialLevel = 1;
        }
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
