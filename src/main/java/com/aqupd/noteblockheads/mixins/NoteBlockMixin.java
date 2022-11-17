package com.aqupd.noteblockheads.mixins;

import com.aqupd.noteblockheads.Heads;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
  @Shadow @Final public static IntProperty NOTE;

  @Inject(method = "playNote", at = @At("HEAD"))
  private void playMobSounds(Entity entity, World world, BlockPos pos, CallbackInfo ci) {
    float pitch = 0.5f + 0.0625f * world.getBlockState(pos).get(NOTE);
    float volume = 1.0f;
    BlockState bs = world.getBlockState(pos.up());
    SoundCategory sc = SoundCategory.RECORDS;
    if(bs.isOf(Blocks.SKELETON_SKULL)) { //All the existing heads
      world.playSound(null, pos, SoundEvents.ENTITY_SKELETON_AMBIENT, sc, volume, pitch);
    } else if(bs.isOf(Blocks.WITHER_SKELETON_SKULL)) {
      world.playSound(null, pos, SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT, sc, volume, pitch);
    } else if(bs.isOf(Blocks.ZOMBIE_HEAD)) {
      world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_AMBIENT, sc, volume, pitch);
    } else if(bs.isOf(Blocks.DRAGON_HEAD)) {
      world.playSound(null, pos, SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT, sc, volume, pitch);
    } else if(bs.isOf(Blocks.CREEPER_HEAD)) {
      world.playSound(null, pos, SoundEvents.ENTITY_CREEPER_PRIMED, sc, volume, pitch);
    } else if(bs.isOf(Blocks.PLAYER_HEAD) && world.getBlockEntity(pos.up()) != null) { //VanillaTweaks more mob heads datapack
      try {
        NbtCompound nbt = world.getBlockEntity(pos.up()).createNbt();
        String skullName = nbt.getCompound("SkullOwner").getString("Name");
        String lastWordName = skullName.substring(skullName.lastIndexOf(" ")+1);
        if(Heads.HEADS.containsKey(skullName)) {
          world.playSound(null, pos, Heads.HEADS.get(skullName), sc, volume, pitch);
        } else if (Heads.HEADS.containsKey(lastWordName)) {
          world.playSound(null, pos, Heads.HEADS.get(lastWordName), sc, volume, pitch);
        }
      } catch (NullPointerException ignored) {}
    }
  }
}