package pl.wildfire.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class LocationUtil
{
    private static final Set<Integer> HOLLOW_MATERIALS = new HashSet<Integer>();
    private static final HashSet<Byte> TRANSPARENT_MATERIALS = new HashSet<Byte>();

    static {
        HOLLOW_MATERIALS.add(Material.AIR.getId());
        HOLLOW_MATERIALS.add(Material.SAPLING.getId());
        HOLLOW_MATERIALS.add(Material.POWERED_RAIL.getId());
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL.getId());
        HOLLOW_MATERIALS.add(Material.LONG_GRASS.getId());
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH.getId());
        HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER.getId());
        HOLLOW_MATERIALS.add(Material.RED_ROSE.getId());
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM.getId());
        HOLLOW_MATERIALS.add(Material.TORCH.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE.getId());
        HOLLOW_MATERIALS.add(Material.SEEDS.getId());
        HOLLOW_MATERIALS.add(Material.SIGN_POST.getId());
        HOLLOW_MATERIALS.add(Material.WOODEN_DOOR.getId());
        HOLLOW_MATERIALS.add(Material.LADDER.getId());
        HOLLOW_MATERIALS.add(Material.RAILS.getId());
        HOLLOW_MATERIALS.add(Material.WALL_SIGN.getId());
        HOLLOW_MATERIALS.add(Material.LEVER.getId());
        HOLLOW_MATERIALS.add(Material.STONE_PLATE.getId());
        HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
        HOLLOW_MATERIALS.add(Material.WOOD_PLATE.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
        HOLLOW_MATERIALS.add(Material.STONE_BUTTON.getId());
        HOLLOW_MATERIALS.add(Material.SNOW.getId());
        HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM.getId());
        HOLLOW_MATERIALS.add(Material.MELON_STEM.getId());
        HOLLOW_MATERIALS.add(Material.VINE.getId());
        HOLLOW_MATERIALS.add(Material.FENCE_GATE.getId());
        HOLLOW_MATERIALS.add(Material.WATER_LILY.getId());
        HOLLOW_MATERIALS.add(Material.NETHER_WARTS.getId());
        HOLLOW_MATERIALS.add(Material.CARPET.getId());

        for (Integer integer : HOLLOW_MATERIALS) {
            TRANSPARENT_MATERIALS.add(integer.byteValue());
        }
        TRANSPARENT_MATERIALS.add((byte) Material.WATER.getId());
        TRANSPARENT_MATERIALS.add((byte) Material.STATIONARY_WATER.getId());
    }

    public static Location getTarget(final LivingEntity entity) throws Exception
    {
        final Block block = entity.getTargetBlock(TRANSPARENT_MATERIALS, 300);
        if (block == null)
        {
            throw new Exception("Not targeting a block");
        }
        return block.getLocation();
    }
}

