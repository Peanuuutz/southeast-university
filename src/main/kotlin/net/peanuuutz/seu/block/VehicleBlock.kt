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

package net.peanuuutz.seu.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.peanuuutz.seu.block.entity.VehicleBlockEntity
import net.peanuuutz.seu.util.vehicleType

object VehicleBlock : Block(
    FabricBlockSettings
        .of(Material.METAL)
        .nonOpaque()
), BlockEntityProvider {
    @Deprecated("Do not call directly")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        val vehicleBlockEntity = world.getBlockEntity(pos)
        if (vehicleBlockEntity !is VehicleBlockEntity) {
            return VoxelShapes.empty()
        }
        val dimensions = vehicleBlockEntity.vehicleType.dimensions
        val halfWidth = dimensions.width * vehicleBlockEntity.number / 2.0
        return VoxelShapes.cuboid(
            0.5 - halfWidth,
            0.0,
            0.5 - halfWidth,
            0.5 + halfWidth,
            dimensions.height.toDouble(),
            0.5 + halfWidth
        )
    }

    @Deprecated("Do not call directly")
    @Suppress("DEPRECATION")
    override fun canReplace(state: BlockState, context: ItemPlacementContext): Boolean {
        val vehicleBlockEntity = context.world.getBlockEntity(context.blockPos)
        if (vehicleBlockEntity is VehicleBlockEntity) {
            val stack = context.stack
            val vehicleType = stack.vehicleType
            return vehicleBlockEntity.vehicleType == vehicleType && vehicleBlockEntity.canAdd
        }
        return super.canReplace(state, context)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val vehicleBlockEntity = ctx.world.getBlockEntity(ctx.blockPos)
        if (vehicleBlockEntity is VehicleBlockEntity && vehicleBlockEntity.canAdd) {
            vehicleBlockEntity.number++
        }
        return super.getPlacementState(ctx)
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        val vehicleBlockEntity = world.getBlockEntity(pos)
        if (vehicleBlockEntity is VehicleBlockEntity) {
            vehicleBlockEntity.vehicleType = itemStack.vehicleType ?: return
        }
    }

    override fun createBlockEntity(world: BlockView): BlockEntity = VehicleBlockEntity()
}