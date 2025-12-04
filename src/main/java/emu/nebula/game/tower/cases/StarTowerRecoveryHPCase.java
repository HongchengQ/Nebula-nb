package emu.nebula.game.tower.cases;

import emu.nebula.proto.PublicStarTower.StarTowerRoomCase;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractReq;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractResp;
import lombok.Getter;

@Getter
public class StarTowerRecoveryHPCase extends StarTowerBaseCase {

    @Override
    public CaseType getType() {
        return CaseType.RecoveryHP;
    }
    
    @Override
    public StarTowerInteractResp interact(StarTowerInteractReq req, StarTowerInteractResp rsp) {
        // Set nil resp
        rsp.getMutableNilResp();
        
        // Add sync hp case
        this.getGame().addCase(rsp.getMutableCases(), new StarTowerSyncHPCase());
        
        // Return
        return rsp;
    }
    
    // Proto
    
    @Override
    public void encodeProto(StarTowerRoomCase proto) {
        // Set field in the proto
        proto.getMutableRecoveryHPCase();
    }
}
