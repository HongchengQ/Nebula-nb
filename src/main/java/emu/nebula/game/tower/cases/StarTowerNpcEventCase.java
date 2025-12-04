package emu.nebula.game.tower.cases;

import emu.nebula.proto.PublicStarTower.StarTowerRoomCase;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractReq;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractResp;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;

@Getter
public class StarTowerNpcEventCase extends StarTowerBaseCase {
    private int npcId;
    private int eventId;
    private IntList options;
    
    public StarTowerNpcEventCase(int npcId, int eventId) {
        this.npcId = npcId;
        this.eventId = eventId;
        this.options = new IntArrayList();
    }

    @Override
    public CaseType getType() {
        return CaseType.NpcEvent;
    }
    
    @Override
    public StarTowerInteractResp interact(StarTowerInteractReq req, StarTowerInteractResp rsp) {
        return rsp;
    }
    
    // Proto
    
    @Override
    public void encodeProto(StarTowerRoomCase proto) {
        proto.getMutableSelectOptionsEventCase()
            .setEvtId(this.getEventId())
            .setNPCId(this.getNpcId())
            .addAllOptions(this.getOptions().toIntArray());
    }
}
