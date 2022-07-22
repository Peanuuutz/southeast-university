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

import net.minecraft.block.Block;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.peanuuutz.seu.block.VehicleBlock;
import net.peanuuutz.seu.block.entity.VehicleBlockEntity;
import net.peanuuutz.seu.entity.SeuEntities;
import net.peanuuutz.seu.util.NbtUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mixin(BuiltinModelItemRenderer.class)
public class MixinBuiltinModelItemRenderer {
    private static final Map<String, VehicleBlockEntity> VEHICLE_BLOCK_ENTITIES = Util.make(() -> {
        Collection<EntityType<?>> vehicleTypes = SeuEntities.getEntityTypes().values();
        HashMap<String, VehicleBlockEntity> map = new HashMap<>(vehicleTypes.size());
        for (EntityType<?> vehicleType : vehicleTypes) {
            VehicleBlockEntity vehicleBlockEntity = new VehicleBlockEntity();
            vehicleBlockEntity.setVehicleType(vehicleType);
            map.put(EntityType.getId(vehicleType).toString(), vehicleBlockEntity);
        }
        return map;
    });

    @Inject(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"), cancellable = true)
    private void renderVehicleBlock(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        Block block = ((BlockItem) stack.getItem()).getBlock();
        if (block instanceof VehicleBlock) {
            String id = NbtUtils.getVehicleId(stack);
            VehicleBlockEntity vehicleBlockEntity = VEHICLE_BLOCK_ENTITIES.get(id);
            if (vehicleBlockEntity != null) {
                BlockEntityRenderDispatcher.INSTANCE.renderEntity(vehicleBlockEntity, matrices, vertexConsumers, light, overlay);
            }
            ci.cancel();
        }
    }
}
