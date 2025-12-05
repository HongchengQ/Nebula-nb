package emu.nebula.game.tower.cases;

import java.util.List;

import emu.nebula.game.tower.StarTowerPotentialInfo;
import emu.nebula.proto.PublicStarTower.StarTowerRoomCase;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractReq;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractResp;

import lombok.Getter;

@Getter
public class StarTowerPotentialCase extends StarTowerBaseCase {
    private int teamLevel;
    private List<StarTowerPotentialInfo> potentials;
    
    public StarTowerPotentialCase(int teamLevel, List<StarTowerPotentialInfo> potentials) {
        this.teamLevel = teamLevel;
        this.potentials = potentials;
    }

    @Override
    public CaseType getType() {
        return CaseType.PotentialSelect;
    }
    
    public StarTowerPotentialInfo selectId(int index) {
        if (index < 0 || index >= this.getPotentials().size()) {
            return null;
        }
        
        return this.getPotentials().get(index);
    }
    
    @Override
    public StarTowerInteractResp interact(StarTowerInteractReq req, StarTowerInteractResp rsp) {
        // Get selected potential
        var index = req.getMutableSelectReq().getIndex();
        
        var potential = this.selectId(index);
        if (potential == null) {
            return rsp;
        }
        
        // Add potential
        var change = this.getGame().addItem(potential.getId(), potential.getLevel());
        
        // Set change
        rsp.setChange(change.toProto());
        
        // Handle pending potential selectors
        var nextCases = this.getGame().handlePendingPotentialSelectors();
        
        for (var towerCase : nextCases) {
            this.getGame().addCase(rsp.getMutableCases(), towerCase);
        }
        
        // Complete
        return rsp;
    }
    
    // Proto
    
    @Override
    public void encodeProto(StarTowerRoomCase proto) {
        var select = proto.getMutableSelectPotentialCase()
            .setTeamLevel(this.getTeamLevel());
        
        for (var potential : this.getPotentials()) {
            select.addInfos(potential.toProto());
        }
    }
}
