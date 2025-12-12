package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.StorySett.StorySettleReq;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.story_settle_req)
public class HandlerStorySettleReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = StorySettleReq.parseFrom(message);
        
        // Settle
        var change = session.getPlayer().getStoryManager().settle(req.getList(), req.getEvidences());
        
        // Handle client events for achievements
        session.getPlayer().getAchievementManager().handleClientEvents(req.getEvents());
        
        // Send response
        return session.encodeMsg(NetMsgId.story_settle_succeed_ack, change.toProto());
    }

}
