package archives.tater.xom;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ConeSummoning {
    private static final Vec3i[] REQUIRED_POSITIONS = {
            new Vec3i(-3, 0, -1),
            new Vec3i(-1, 0, -3),
            new Vec3i(-3, 0, 1),
            new Vec3i(-1, 0, 3),
            new Vec3i(3,  0, -1),
            new Vec3i(1,  0, -3),
            new Vec3i(3,  0, 1),
            new Vec3i(1,  0, 3)
    };

    private static @Nullable BlockPos summonPosition = null;
    private static int summonTicks = 0;
    private static @Nullable RegistryKey<World> summonWorld = null;

    // Call on the server
    public static void checkSummon(ServerWorld world, BlockPos candlePos) {
        for (var startOffset : REQUIRED_POSITIONS) {
            var centerPos = candlePos.add(startOffset).add(0, -1, 0);
            if (isValidStructure(world, centerPos)) {
                startSummon(world, centerPos);
                return;
            }
        }
    }

    public static boolean isValidStructure(World world, BlockPos centerPos) {
        for (var checkOffset : REQUIRED_POSITIONS) {
            var checkPos = centerPos.add(checkOffset);
            var coneState = world.getBlockState(checkPos);
            if (!coneState.isOf(Xom.CONE_BLOCK) || coneState.get(ConeBlock.STACKED) != 3) return false;
            var candleState = world.getBlockState(checkPos.up());
            if (!candleState.isOf(Blocks.ORANGE_CANDLE) || !candleState.get(CandleBlock.LIT)) return false;
        }
        return true;
    }

    public static void startSummon(ServerWorld world, BlockPos centerPos) {
        world.setWeather(0, 200, true, true);
        summonPosition = centerPos;
        summonTicks = 150;
        summonWorld = world.getRegistryKey();
    }

    public static void runSummon(World world, BlockPos centerPos) {
        world.setBlockState(centerPos, Xom.CONE_BLOCK.getDefaultState());
        var lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
        if (lightningEntity == null)
            return;
        lightningEntity.refreshPositionAfterTeleport(centerPos.toBottomCenterPos());
        lightningEntity.setCosmetic(true);
        world.spawnEntity(lightningEntity);
        for (var offset : REQUIRED_POSITIONS) {
            var candlePos = centerPos.add(offset).add(0, 1, 0);
            var candleState = world.getBlockState(candlePos);
            if (candleState.isOf(Blocks.ORANGE_CANDLE) && candleState.get(CandleBlock.LIT))
                CandleBlock.extinguish(null, candleState, world, candlePos);
        }
    }

    public static void registerCallbacks() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            var world = server.getWorld(summonWorld);
            if (world == null) return;
            if (summonTicks == 0) {
                if (summonPosition != null && summonWorld != null) {
                    runSummon(world, summonPosition);
                    summonPosition = null;
                    summonWorld = null;
                }
            } else if (summonTicks > 0) {
                summonTicks--;
                if (summonPosition != null && summonTicks % 20 == 0 && world.random.nextFloat() < 0.2f) {
                    world.playSound(null, summonPosition,
                                    SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                                    SoundCategory.WEATHER,
                                    500.0F,
                                    0.8F + world.random.nextFloat() * 0.2F
                            );
                }
            }
        });
    }
}
