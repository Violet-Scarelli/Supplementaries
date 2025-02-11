package net.mehvahdjukaar.supplementaries.common.world.data.map.markers;

import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.world.data.map.ModMapMarkers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.NetherPortalBlock;

import javax.annotation.Nullable;

public class NetherPortalMarker extends MapBlockMarker<CustomMapDecoration> {

    public NetherPortalMarker() {
        super(ModMapMarkers.NETHER_PORTAL_DECORATION_TYPE);
    }

    public NetherPortalMarker(BlockPos pos) {
        super(ModMapMarkers.NETHER_PORTAL_DECORATION_TYPE, pos);
    }

    @Nullable
    public static NetherPortalMarker getFromWorld(BlockGetter world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() instanceof NetherPortalBlock ||
                Utils.getID(world.getFluidState(pos).getType()).toString().equals("betterportals:portal_fluid")) {
            return new NetherPortalMarker(pos);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public CustomMapDecoration doCreateDecoration(byte mapX, byte mapY, byte rot) {
        return new CustomMapDecoration(this.getType(), mapX, mapY, rot, null);
    }
}
