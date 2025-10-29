package emu.nebula.game.instance;

import emu.nebula.game.inventory.ItemParamMap;
import emu.nebula.game.player.Player;

public interface InstanceData {
    
    public int getId();
    
    public int getNeedWorldClass();
    
    public int getEnergyConsume();
    
    public ItemParamMap getFirstRewards();
    
    public ItemParamMap getRewards();
    
    /**
     * Checks if the player has enough energy to complete this instance
     * @return true if the player has enough energy
     */
    public default boolean hasEnergy(Player player) {
        return this.hasEnergy(player, 1);
    }
    
    /**
     * Checks if the player has enough energy to complete this instance
     * @return true if the player has enough energy
     */
    public default boolean hasEnergy(Player player, int count) {
        return (this.getEnergyConsume() * count) <= player.getEnergy();
    }
    
}
