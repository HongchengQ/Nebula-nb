package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.ActivityLevelsSettle.ActivityLevelsSettleReq;
import emu.nebula.proto.ActivityLevelsSettle.ActivityLevelsSettleResp;
import emu.nebula.net.HandlerId;
import emu.nebula.game.activity.type.LevelsActivity;
import emu.nebula.game.instance.InstanceSettleData;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.activity_levels_settle_req)
public class HandlerActivityLevelsSettleReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = ActivityLevelsSettleReq.parseFrom(message);
        
        // Get activity
        var activity = session.getPlayer().getActivityManager().getActivity(LevelsActivity.class, req.getActivityId());
        
        if (activity == null || activity.getLevel() == null) {
            return session.encodeMsg(NetMsgId.activity_levels_settle_failed_ack);
        }
        
        // Settle
        var change = activity.settle(req.getStar());
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.activity_levels_settle_failed_ack);
        }
        
        var settleData = (InstanceSettleData) change.getExtraData();
        
        // Handle client events for achievements
        session.getPlayer().getAchievementManager().handleClientEvents(req.getEvents());
        
        // Build response
        var rsp = ActivityLevelsSettleResp.newInstance()
                .setExp(settleData.getExp())
                .setChangeInfo(change.toProto());
        
        // Add reward items
        if (settleData.getRewards() != null) {
            settleData.getRewards().toItemTemplateStream().forEach(rsp::addFixed);
        }
        if (settleData.getFirstRewards() != null) {
            settleData.getFirstRewards().toItemTemplateStream().forEach(rsp::addFirst);
        }
        
        // Encode and send
        return session.encodeMsg(NetMsgId.activity_levels_settle_succeed_ack, rsp);
    }

}
