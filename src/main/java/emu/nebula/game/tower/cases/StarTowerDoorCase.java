package emu.nebula.game.tower.cases;

import emu.nebula.data.resources.StarTowerStageDef;
import emu.nebula.proto.PublicStarTower.StarTowerRoomCase;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractReq;
import emu.nebula.proto.StarTowerInteract.StarTowerInteractResp;

import lombok.Getter;

@Getter
public class StarTowerDoorCase extends StarTowerBaseCase {
    private int floorNum;
    private int roomType;
    
    public StarTowerDoorCase(int floor, StarTowerStageDef data) {
        this.floorNum = floor;
        
        if (data != null) {
            this.roomType = data.getRoomType().getValue();
        }
    }

    @Override
    public CaseType getType() {
        return CaseType.OpenDoor;
    }
    
    @Override
    public StarTowerInteractResp interact(StarTowerInteractReq req, StarTowerInteractResp rsp) {
        // Get request
        var proto = req.getEnterReq();
        
        // Check if we need to settle on the last floor
        if (this.getGame().isOnFinalFloor()) {
            return this.getGame().settle(rsp, true);
        }
        
        // Enter next room
        this.getGame().enterNextRoom();
        this.getGame().getRoom().setMapInfo(proto);
        
        // Set room proto
        rsp.getMutableEnterResp()
            .setRoom(this.getRoom().toProto());
        
        // Done
        return rsp;
    }
    
    // Proto
    
    @Override
    public void encodeProto(StarTowerRoomCase proto) {
        proto.getMutableDoorCase()
            .setFloor(this.getFloorNum())
            .setType(this.getRoomType());
    }
}
