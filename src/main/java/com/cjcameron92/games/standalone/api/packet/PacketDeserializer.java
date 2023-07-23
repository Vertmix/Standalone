package com.cjcameron92.games.standalone.api.packet;

import com.cjcameron92.games.standalone.api.ClientBootstrap;
import com.cjcameron92.games.standalone.api.DataDeserializer;
import com.cjcameron92.games.standalone.api.TargetType;
import com.google.gson.JsonObject;

public class PacketDeserializer implements DataDeserializer<PacketWrapper> {

    @Override
    public PacketWrapper deserialize(String string) {
        try {
            final JsonObject object = ClientBootstrap.getGSON().fromJson(string, JsonObject.class);
            final String packetTypeName = object.get("packetType").getAsString();
            final Class<? extends Packet> packetType = (Class<? extends Packet>) Class.forName(packetTypeName);
            final Packet packet = ClientBootstrap.getGSON().fromJson(object.get("packet"), packetType);
            final TargetType targetType = TargetType.valueOf(object.get("targetType").getAsString());
            return new PacketWrapper(packet, targetType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
