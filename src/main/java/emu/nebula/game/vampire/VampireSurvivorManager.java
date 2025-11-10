package emu.nebula.game.vampire;

import emu.nebula.data.GameData;
import emu.nebula.game.player.Player;
import emu.nebula.game.player.PlayerManager;

import lombok.Getter;

@Getter
public class VampireSurvivorManager extends PlayerManager {
    // Game
    private transient VampireSurvivorGame game;
    
    public VampireSurvivorManager(Player player) {
        super(player);
    }
    
    public VampireSurvivorGame apply(int levelId) {
        // Get data
        var data = GameData.getVampireSurvivorDataTable().get(levelId);
        if (data == null) {
            return null;
        }
        
        // Create game
        this.game = new VampireSurvivorGame(this, data);
        
        // Success
        return this.game;
    }

    public void settle(boolean isWin, int score) {
        // Clear game
        this.game = null;
    }

}
