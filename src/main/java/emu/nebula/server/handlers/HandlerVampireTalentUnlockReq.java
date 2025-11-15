package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.Public.UI32;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.vampire_talent_unlock_req)
public class HandlerVampireTalentUnlockReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = UI32.parseFrom(message);
        
        // Unlock talent
        boolean success = session.getPlayer().getVampireSurvivorManager().unlockTalent(req.getValue());
        
        // Encode and send
        return session.encodeMsg(success ? NetMsgId.vampire_talent_unlock_succeed_ack : NetMsgId.vampire_talent_unlock_failed_ack);
    }

}
