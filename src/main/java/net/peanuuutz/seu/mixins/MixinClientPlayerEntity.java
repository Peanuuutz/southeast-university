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

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.peanuuutz.seu.entity.VehicleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends Entity {
    @Shadow private boolean riding;

    @Shadow public Input input;

    private MixinClientPlayerEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickRiding", at = @At("RETURN"))
    private void updateVehicle(CallbackInfo ci) {
        Entity entityVehicle = getVehicle();
        if (entityVehicle instanceof VehicleEntity) {
            VehicleEntity vehicle = (VehicleEntity) entityVehicle;
            vehicle.setPressingLeft(input.pressingLeft);
            vehicle.setPressingRight(input.pressingRight);
            vehicle.setPressingForward(input.pressingForward);
            vehicle.setPressingBack(input.pressingBack);
            riding |= input.pressingLeft || input.pressingRight || input.pressingForward || input.pressingBack;
        }
    }
}
