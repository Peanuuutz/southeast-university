/*
    Copyright 2022 Peanuuutz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package net.peanuuutz.seu.mixins;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.peanuuutz.seu.entity.BikeEntity;
import net.peanuuutz.seu.entity.SeuEntities;
import net.peanuuutz.seu.entity.VehicleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Shadow private ClientWorld world;

    @Inject(method = "onEntitySpawn", at = @At("RETURN"))
    private void createVehicle(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        VehicleEntity vehicle;
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        EntityType<?> entityType = packet.getEntityTypeId();
        if (entityType == SeuEntities.BIKE_ENTITY) {
            vehicle = new BikeEntity(world);
        } else {
            vehicle = null;
        }
        if (vehicle != null) {
            vehicle.setPosition(x, y, z);
            vehicle.updateTrackedPosition(x, y, z);
            vehicle.refreshPositionAfterTeleport(x, y, z);
            vehicle.pitch = packet.getPitch() * 360 / 256f;
            vehicle.yaw = packet.getYaw() * 360 / 256f;
            int id = packet.getId();
            vehicle.setEntityId(id);
            vehicle.setUuid(packet.getUuid());
            world.addEntity(id, vehicle);
        }
    }
}
