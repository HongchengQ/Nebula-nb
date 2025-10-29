package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.RegionBossLevelApply.RegionBossLevelApplyReq;
import emu.nebula.net.HandlerId;
import emu.nebula.data.GameData;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.region_boss_level_apply_req)
public class HandlerRegionBossLevelApplyReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = RegionBossLevelApplyReq.parseFrom(message);
        
        var data = GameData.getRegionBossLevelDataTable().get(req.getId());
        if (data == null) {
            return this.encodeMsg(NetMsgId.region_boss_level_apply_failed_ack);
        }
        
        // Check player energy
        if (data.getEnergyConsume() > session.getPlayer().getEnergy()) {
            return this.encodeMsg(NetMsgId.region_boss_level_apply_failed_ack);
        }
        
        // Set player
        session.getPlayer().getInstanceManager().setCurInstanceId(req.getId());
        
        // Template
        return this.encodeMsg(NetMsgId.region_boss_level_apply_succeed_ack);
    }

}
