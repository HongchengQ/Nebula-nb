package emu.nebula.game.tower.cases;

import emu.nebula.proto.PublicStarTower.PotentialInfo;
import emu.nebula.proto.PublicStarTower.StarTowerRoomCase;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractReq;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractResp;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;

@Getter
public class StarTowerPotentialCase extends StarTowerBaseCase {
    private int teamLevel;
    private IntList potentialIds;
    
    public StarTowerPotentialCase(int teamLevel, IntList potentialIds) {
        this.teamLevel = teamLevel;
        this.potentialIds = potentialIds;
    }

    @Override
    public CaseType getType() {
        return CaseType.PotentialSelect;
    }
    
    public int selectId(int index) {
        if (this.getPotentialIds() == null) {
            return 0;
        }
        
        if (index < 0 || index >= this.getPotentialIds().size()) {
            return 0;
        }
        
        return this.getPotentialIds().getInt(index);
    }
    
    @Override
    public StarTowerInteractResp interact(StarTowerInteractReq req, StarTowerInteractResp rsp) {
        var index = req.getMutableSelectReq().getIndex();
        
        int id = this.selectId(index);
        if (id <= 0) {
            return rsp;
        }
        
        // Add item
        var change = this.getGame().addItem(id, 1);
        
        // Set change
        rsp.setChange(change.toProto());
        
        // Handle pending potential selectors
        var nextCases = this.getGame().handlePendingPotentialSelectors();
        
        for (var towerCase : nextCases) {
            this.getGame().addCase(rsp.getMutableCases(), towerCase);
        }
        
        return rsp;
    }
    
    // Proto
    
    @Override
    public void encodeProto(StarTowerRoomCase proto) {
        var select = proto.getMutableSelectPotentialCase()
            .setTeamLevel(this.getTeamLevel());
        
        for (int id : this.getPotentialIds()) {
            var info = PotentialInfo.newInstance()
                    .setTid(id)
                    .setLevel(1);
            
            select.addInfos(info);
        }
    }
}
