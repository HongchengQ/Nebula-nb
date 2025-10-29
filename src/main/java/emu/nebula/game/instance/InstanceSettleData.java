package emu.nebula.game.instance;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InstanceSettleData {
    private boolean isWin;
    private boolean isFirst;
    private int exp;
    
    public InstanceSettleData() {
        
    }
}
