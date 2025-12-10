package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.ActivityLevelsSweep.ActivityLevelsSweepReq;
import emu.nebula.proto.ActivityLevelsSweep.ActivityLevelsSweepResp;
import emu.nebula.proto.ActivityLevelsSweep.ActivityLevelsSweepReward;
import emu.nebula.net.HandlerId;
import emu.nebula.data.GameData;
import emu.nebula.game.activity.type.LevelsActivity;
import emu.nebula.game.inventory.ItemParamMap;
import emu.nebula.net.GameSession;

import java.util.List;

@HandlerId(NetMsgId.activity_levels_sweep_req)
public class HandlerActivityLevelsSweepReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = ActivityLevelsSweepReq.parseFrom(message);
        
        // Get activity
        var activity = session.getPlayer().getActivityManager().getActivity(LevelsActivity.class, req.getActivityId());
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.activity_levels_sweep_failed_ack);
        }
        
        // Get level data
        var data = GameData.getActivityLevelsLevelDataTable().get(req.getLevelId());
        if (data == null) {
            return session.encodeMsg(NetMsgId.activity_levels_sweep_failed_ack);
        }
        
        // Settle
        var change = activity.sweep(data, req.getTimes());
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.activity_levels_sweep_failed_ack);
        }
        
        // Handle client events for achievements
        session.getPlayer().getAchievementManager().handleClientEvents(req.getEvents());
        
        // Build response
        var rsp = ActivityLevelsSweepResp.newInstance()
                .setChangeInfo(change.toProto());
        
        // Add reward list to response
        if (change.getExtraData() != null) {
            @SuppressWarnings("unchecked")
            var list = (List<ItemParamMap>) change.getExtraData();
            
            for (var rewards : list) {
                var reward = ActivityLevelsSweepReward.newInstance()
                        .setExp(data.getEnergyConsume());
                
                rewards.toItemTemplateStream().forEach(reward::addAwardItems);
                
                rsp.addRewards(reward);
            }
        }
        
        // Encode and send
        return session.encodeMsg(NetMsgId.activity_levels_sweep_succeed_ack, rsp);
    }

}
