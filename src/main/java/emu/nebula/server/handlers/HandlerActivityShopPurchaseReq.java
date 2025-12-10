package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.ActivityShopPurchase.ActivityShopPurchaseReq;
import emu.nebula.proto.ActivityShopPurchase.ActivityShopPurchaseResp;
import emu.nebula.net.HandlerId;
import emu.nebula.game.activity.type.ShopActivity;
import emu.nebula.game.activity.type.ShopActivity.ActivityShopInfo;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.activity_shop_purchase_req)
public class HandlerActivityShopPurchaseReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = ActivityShopPurchaseReq.parseFrom(message);
        
        // Get activity
        var activity = session.getPlayer().getActivityManager().getActivity(ShopActivity.class, req.getActivityId());
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.activity_shop_purchase_failed_ack);
        }
        
        // Buy
        var change = activity.buy(req.getShopId(), req.getGoodsId(), req.getNumber());
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.activity_shop_purchase_failed_ack);
        }
        
        // Build response
        var rsp = ActivityShopPurchaseResp.newInstance()
                .setChange(change.toProto())
                .setPurchasedNumber(req.getNumber());
        
        // Set shop info
        var shop = (ActivityShopInfo) change.getExtraData();
        var shopInfo = shop.toProto()
                .setId(req.getShopId());
        
        rsp.setShop(shopInfo);
        
        // Encode and send
        return session.encodeMsg(NetMsgId.activity_shop_purchase_succeed_ack, rsp);
    }

}
