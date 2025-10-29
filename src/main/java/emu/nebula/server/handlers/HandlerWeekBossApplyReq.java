package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.WeekBossApply.WeekBossApplyReq;
import emu.nebula.net.HandlerId;
import emu.nebula.data.GameData;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.week_boss_apply_req)
public class HandlerWeekBossApplyReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = WeekBossApplyReq.parseFrom(message);
        
        var data = GameData.getWeekBossLevelDataTable().get(req.getId());
        if (data == null) {
            return this.encodeMsg(NetMsgId.week_boss_apply_failed_ack);
        }
        
        // Set player
        session.getPlayer().getInstanceManager().setCurInstanceId(req.getId());
        
        // Template
        return this.encodeMsg(NetMsgId.week_boss_apply_succeed_ack);
    }

}
