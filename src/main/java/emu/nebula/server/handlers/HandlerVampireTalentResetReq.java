package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.vampire_talent_reset_req)
public class HandlerVampireTalentResetReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Reset talents
        session.getPlayer().getVampireSurvivorManager().resetTalents();
        
        // Encode and send
        return session.encodeMsg(NetMsgId.vampire_talent_reset_succeed_ack);
    }

}
