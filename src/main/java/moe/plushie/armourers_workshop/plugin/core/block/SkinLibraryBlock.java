package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.init.ModBlocks;
import moe.plushie.armourers_workshop.plugin.init.ModMenuTypes;
import moe.plushie.armourers_workshop.plugin.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SkinLibraryBlock extends HorizontalDirectionalBlock {

    public SkinLibraryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, World world, BlockPos blockPos, Player player, InteractionHand hand) {
        if (this == ModBlocks.SKIN_LIBRARY_CREATIVE) {
            return MenuManager.openMenu(ModMenuTypes.SKIN_LIBRARY_CREATIVE, player, WorldAccessor.of(world, blockPos));
        }
        if (this == ModBlocks.SKIN_LIBRARY) {
            return MenuManager.openMenu(ModMenuTypes.SKIN_LIBRARY, player, WorldAccessor.of(world, blockPos));
        }
        return InteractionResult.CONSUME;
    }

//    @Override
//    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean p_196243_5_) {
//        if (state.is(newState.getBlock())) {
//            return;
//        }
//        SkinLibraryBlockEntity blockEntity = ObjectUtils.safeCast(level.getBlockEntity(pos), SkinLibraryBlockEntity.class);
//        if (blockEntity != null) {
//            DataSerializers.dropContents(level, pos, blockEntity.getInventory());
//        }
//        super.onRemove(state, level, pos, newState, p_196243_5_);
//    }
}