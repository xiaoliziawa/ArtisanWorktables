package com.lirxowo.oraculumworktables.common.plugin.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.oraculum_worktables.Tier")
public enum ZenEnumTier {

  @ZenCodeType.Field
  WORKTABLE(EnumTier.WORKTABLE),

  @ZenCodeType.Field
  WORKSTATION(EnumTier.WORKSTATION),

  @ZenCodeType.Field
  WORKSHOP(EnumTier.WORKSHOP);

  private final EnumTier tier;

  ZenEnumTier(EnumTier tier) {

    this.tier = tier;
  }

  public EnumTier getTier() {

    return this.tier;
  }
}
