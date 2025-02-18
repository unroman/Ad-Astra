package earth.terrarium.ad_astra.common.entity;

import earth.terrarium.ad_astra.AdAstra;
import earth.terrarium.ad_astra.common.registry.ModEntityTypes;
import earth.terrarium.ad_astra.common.registry.ModItems;
import earth.terrarium.ad_astra.mixin.PaintingInvoker;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpacePainting extends Painting {

    public SpacePainting(EntityType<? extends Painting> entityType, Level level) {
        super(entityType, level);
    }

    protected SpacePainting(Level level, BlockPos pos) {
        this(ModEntityTypes.SPACE_PAINTING.get(), level);
        this.pos = pos;
    }

    public static Optional<SpacePainting> placeSpacePainting(Level level, BlockPos pos, Direction facing) {
        SpacePainting paintingEntity = new SpacePainting(level, pos);

        List<Holder<PaintingVariant>> spacePaintings = getSpacePaintings();

        if (spacePaintings.isEmpty()) {
            return Optional.empty();
        }

        paintingEntity.setDirection(facing);
        spacePaintings.removeIf(variant -> {
            ((PaintingInvoker) paintingEntity).ad_astra$invokeSetVariant(variant);
            
            return !paintingEntity.survives();
        });

        if (spacePaintings.isEmpty()) {
            return Optional.empty();
        }

        int i = spacePaintings.stream().mapToInt(SpacePainting::getSize).max().orElse(0);
        spacePaintings.removeIf(variant -> SpacePainting.getSize(variant) < i);
        Optional<Holder<PaintingVariant>> optional = Util.getRandomSafe(spacePaintings, paintingEntity.random);

        if (optional.isEmpty()) {
            return Optional.empty();
        }

        ((PaintingInvoker) paintingEntity).ad_astra$invokeSetVariant(optional.get());
        paintingEntity.setDirection(facing);
        return Optional.of(paintingEntity);
    }

    protected static int getSize(Holder<PaintingVariant> variant) {
        return variant.value().getWidth() * variant.value().getHeight();
    }

    public static List<Holder<PaintingVariant>> getSpacePaintings() {
        List<Holder<PaintingVariant>> paintings = new ArrayList<>();
        BuiltInRegistries.PAINTING_VARIANT.forEach(painting -> {
            if (BuiltInRegistries.PAINTING_VARIANT.getKey(painting).getNamespace().equals(AdAstra.MOD_ID)) {
                paintings.add(Holder.direct(painting));
            }
        });

        return paintings;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        ResourceKey<PaintingVariant> registryKey = ResourceKey.create(Registries.PAINTING_VARIANT, new ResourceLocation(nbt.getString("Variant")));
        ((PaintingInvoker) this).ad_astra$invokeSetVariant(BuiltInRegistries.PAINTING_VARIANT.getHolder(registryKey).get());
        this.direction = Direction.from2DDataValue(nbt.getByte("Facing"));
        this.setDirection(this.direction);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Variant", BuiltInRegistries.PAINTING_VARIANT.getKey(this.getVariant().value()).toString());
        nbt.putByte("Facing", (byte) this.direction.get2DDataValue());
    }

    @Override
    public ItemEntity spawnAtLocation(ItemLike item) {
        return super.spawnAtLocation(ModItems.SPACE_PAINTING.get());
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.SPACE_PAINTING.get());
    }
}