package com.lirxowo.artisanworktables.common.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class MageParticle
    extends PortalParticle {

  private final double x;
  private final double y;
  private final double z;

  protected MageParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {

    super(world, x, y, z, motionX, motionY, motionZ);

    this.xo = x + motionX;
    this.yo = y + motionY;
    this.zo = z + motionZ;

    this.x = this.xo;
    this.y = this.yo;
    this.z = this.zo;

    this.quadSize = 0.1F * (this.random.nextFloat() * 0.5f + 0.4f);

    float f = this.random.nextFloat() * 0.25f + 0.75f;
    this.rCol = 145 / 255f * f;
    this.gCol = 82 / 255f * f;
    this.bCol = 198 / 255f * f;

    this.lifetime = (int) (Math.random() * 10.0D) + 30;

    this.hasPhysics = false;
  }

  @Nonnull
  @Override
  public ParticleRenderType getRenderType() {

    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public float getQuadSize(float scaleFactor) {

    return this.quadSize;
  }

  @Override
  public void tick() {

    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;

    if (this.age++ >= this.lifetime) {
      this.remove();

    } else {
      float f = (float) this.age / (float) this.lifetime;
      f = 1.0F - f;
      float f1 = 1.0F - f;
      f1 = f1 * f1;
      f1 = f1 * f1;
      this.setPos(this.x + this.xd * (double) f, this.y + this.yd * (double) f - (double) (f1 * 1.2F), this.z + this.zd * (double) f);
      this.alpha = f;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class Factory
      implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public Factory(SpriteSet spriteSet) {

      this.spriteSet = spriteSet;
    }

    public Particle createParticle(@Nonnull SimpleParticleType type, @Nonnull ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {

      MageParticle particle = new MageParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
      particle.pickSprite(this.spriteSet);
      return particle;
    }
  }
}
