// ============================================================================
// OraculumWorktables - CraftTweaker (ZenScript) recipe examples
// Covers all 15 table types, one example each, demonstrating various options.
// Location: run/scripts/ (or the instance's scripts/ directory); run /reload after editing.
// ============================================================================

import mods.oraculumworktables.Recipe;
import mods.oraculumworktables.Type;
import mods.oraculumworktables.Tier;

// ---------------------------------------------------------------------------
// 1. TAILOR -- shaped, uses a tool (shears, costs 1 durability)
// ---------------------------------------------------------------------------
Recipe.type(Type.TAILOR)
    .shaped([
        [<item:minecraft:leather>, <item:minecraft:leather>, <item:minecraft:leather>],
        [<item:minecraft:leather>, <item:minecraft:air>,     <item:minecraft:leather>]
    ])
    .tool(<item:minecraft:shears>, 1)
    .output(<item:minecraft:leather_chestplate>)
    .register("tailor_chestplate");

// ---------------------------------------------------------------------------
// 2. CARPENTER -- shaped, requires an axe, demonstrates mirrored matching
// ---------------------------------------------------------------------------
Recipe.type(Type.CARPENTER)
    .shaped([
        [<item:minecraft:oak_planks>, <item:minecraft:oak_planks>],
        [<item:minecraft:oak_planks>, <item:minecraft:oak_planks>]
    ])
    .tool(<item:minecraft:iron_axe>, 1)
    .mirrored(true)
    .output(<item:minecraft:crafting_table>)
    .register("carpenter_crafting_table");

// ---------------------------------------------------------------------------
// 3. MASON -- shaped, requires a pickaxe, restricted to workstation and above
// ---------------------------------------------------------------------------
Recipe.type(Type.MASON)
    .shaped([
        [<item:minecraft:stone>, <item:minecraft:stone>],
        [<item:minecraft:stone>, <item:minecraft:stone>]
    ])
    .tool(<item:minecraft:stone_pickaxe>, 1)
    .restrict(Tier.WORKSTATION)
    .output(<item:minecraft:stone_bricks> * 4)
    .register("mason_stone_bricks");

// ---------------------------------------------------------------------------
// 4. BLACKSMITH -- shaped, tool + fluid + experience level + extra output (combined example)
// ---------------------------------------------------------------------------
Recipe.type(Type.BLACKSMITH)
    .shaped([
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:stick>,      <item:minecraft:iron_ingot>],
        [<item:minecraft:air>,        <item:minecraft:stick>,      <item:minecraft:air>]
    ])
    .tool(<item:minecraft:diamond_pickaxe>, 10)
    .fluid(<fluid:minecraft:water> * 1000)
    .extra(<item:minecraft:iron_nugget>, 0.5)
    .restrict(Tier.WORKSTATION, Tier.WORKSHOP)
    .level(3)
    .output(<item:minecraft:iron_block>)
    .register("blacksmith_iron_block");

// ---------------------------------------------------------------------------
// 5. JEWELER -- shaped, secondary ingredient (not consumed, acts as a catalyst) + chance extra output
// ---------------------------------------------------------------------------
Recipe.type(Type.JEWELER)
    .shaped([
        [<item:minecraft:diamond>, <item:minecraft:gold_ingot>, <item:minecraft:diamond>]
    ])
    .secondary([<item:minecraft:emerald>], false)
    .extra(<item:minecraft:diamond>, 0.25)
    .output(<item:minecraft:enchanted_golden_apple>)
    .register("jeweler_gilded_gem");

// ---------------------------------------------------------------------------
// 6. BASIC -- shapeless, the simplest form
// ---------------------------------------------------------------------------
Recipe.type(Type.BASIC)
    .shapeless([<item:minecraft:oak_log>])
    .output(<item:minecraft:oak_planks> * 4)
    .register("basic_planks");

// ---------------------------------------------------------------------------
// 7. ENGINEER -- shaped, secondary ingredients (consumed) + tool
// ---------------------------------------------------------------------------
Recipe.type(Type.ENGINEER)
    .shaped([
        [<item:minecraft:iron_ingot>, <item:minecraft:redstone>,  <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:redstone>,  <item:minecraft:iron_ingot>]
    ])
    .tool(<item:minecraft:iron_pickaxe>, 2)
    .secondary([<item:minecraft:copper_ingot>, <item:minecraft:copper_ingot>])
    .output(<item:minecraft:piston>)
    .register("engineer_piston");

// ---------------------------------------------------------------------------
// 8. MAGE -- shaped, consumes an experience level (does not consume XP points, used only as a threshold)
// ---------------------------------------------------------------------------
Recipe.type(Type.MAGE)
    .shaped([
        [<item:minecraft:lapis_lazuli>, <item:minecraft:book>,         <item:minecraft:lapis_lazuli>],
        [<item:minecraft:lapis_lazuli>, <item:minecraft:experience_bottle>, <item:minecraft:lapis_lazuli>]
    ])
    .level(5)
    .output(<item:minecraft:enchanted_book>.withTag({StoredEnchantments: [{id: "minecraft:sharpness" as string, lvl: 5 as short}]}))
    .register("mage_sharpness_book");

// ---------------------------------------------------------------------------
// 9. SCRIBE -- shapeless, demonstrates a pure ingredient combination
// ---------------------------------------------------------------------------
Recipe.type(Type.SCRIBE)
    .shapeless([<item:minecraft:paper>, <item:minecraft:paper>, <item:minecraft:paper>, <item:minecraft:leather>])
    .output(<item:minecraft:book>)
    .register("scribe_book");

// ---------------------------------------------------------------------------
// 10. CHEMIST -- shapeless, consumes fluid
// ---------------------------------------------------------------------------
Recipe.type(Type.CHEMIST)
    .shapeless([<item:minecraft:gunpowder>, <item:minecraft:redstone>])
    .fluid(<fluid:minecraft:water> * 500)
    .output(<item:minecraft:tnt>)
    .register("chemist_tnt");

// ---------------------------------------------------------------------------
// 11. FARMER -- shapeless, chance extra output (seeds)
// ---------------------------------------------------------------------------
Recipe.type(Type.FARMER)
    .shapeless([<item:minecraft:wheat>, <item:minecraft:wheat>, <item:minecraft:wheat>])
    .extra(<item:minecraft:wheat_seeds>, 0.5)
    .output(<item:minecraft:bread>)
    .register("farmer_bread");

// ---------------------------------------------------------------------------
// 12. CHEF -- shapeless, requires experience points (consumed)
// ---------------------------------------------------------------------------
Recipe.type(Type.CHEF)
    .shapeless([<item:minecraft:cooked_beef>, <item:minecraft:bread>, <item:minecraft:carrot>])
    .experience(10)
    .output(<item:minecraft:cooked_beef> * 2)
    .register("chef_meal");

// ---------------------------------------------------------------------------
// 13. DESIGNER -- shaped, dyeing related
// ---------------------------------------------------------------------------
Recipe.type(Type.DESIGNER)
    .shaped([
        [<item:minecraft:white_wool>, <item:minecraft:blue_dye>, <item:minecraft:white_wool>]
    ])
    .output(<item:minecraft:blue_wool> * 2)
    .register("designer_blue_wool");

// ---------------------------------------------------------------------------
// 14. TANNER -- shaped, tool + fluid, restricted to a single tier (worktable only)
// ---------------------------------------------------------------------------
Recipe.type(Type.TANNER)
    .shaped([
        [<item:minecraft:rabbit_hide>, <item:minecraft:rabbit_hide>],
        [<item:minecraft:rabbit_hide>, <item:minecraft:rabbit_hide>]
    ])
    .tool(<item:minecraft:flint>, 1)
    .fluid(<fluid:minecraft:water> * 250)
    .restrict(Tier.WORKTABLE, Tier.WORKTABLE)
    .output(<item:minecraft:leather>)
    .register("tanner_leather");

// ---------------------------------------------------------------------------
// 15. POTTER -- shaped, fluid + secondary ingredient + extra output (combined example)
// ---------------------------------------------------------------------------
Recipe.type(Type.POTTER)
    .shaped([
        [<item:minecraft:clay_ball>, <item:minecraft:clay_ball>],
        [<item:minecraft:clay_ball>, <item:minecraft:clay_ball>]
    ])
    .fluid(<fluid:minecraft:water> * 100)
    .secondary([<item:minecraft:sand>])
    .extra(<item:minecraft:brick>, 0.3)
    .output(<item:minecraft:terracotta>)
    .register("potter_terracotta");

// ===========================================================================
// Tag examples
// Input fields (primary ingredients, secondary ingredients, tools) can use <tag:items:...>;
// output (output / extra) and fluid (fluid) do NOT support tags and must be concrete ids.
// On 1.20.1 Forge, item tags use the forge: namespace (not the c: of 1.21+).
// ===========================================================================

// All-tag inputs: primary ingredients + tool + secondary ingredients all use tags
Recipe.type(Type.BLACKSMITH)
    .shaped([
        [<tag:items:forge:ingots/iron>, <tag:items:forge:ingots/iron>],
        [<tag:items:forge:ingots/iron>, <tag:items:forge:ingots/iron>]
    ])
    .tool(<tag:items:minecraft:pickaxes>, 1)
    .secondary([<tag:items:forge:gems/diamond>], false)
    .output(<item:minecraft:anvil>)
    .register("blacksmith_anvil_tag");

// Shapeless + tag: any log -> sticks
Recipe.type(Type.BASIC)
    .shapeless([<tag:items:minecraft:logs>])
    .output(<item:minecraft:stick> * 4)
    .register("basic_sticks_tag");

// ===========================================================================
// matchNbt tool example
// Requires an iron pickaxe named "Hammer" (third argument true = match by NBT, ignores durability automatically).
// A plain unnamed iron pickaxe does NOT satisfy this recipe.
// ===========================================================================
val hammer = <item:minecraft:iron_pickaxe>.withTag({display: {Name: "{\"text\":\"Hammer\"}"}});

Recipe.type(Type.MASON)
    .shapeless([<item:minecraft:stone>])
    .tool(hammer, 10, true)
    .output(<item:minecraft:cobblestone>)
    .register("mason_hammer_cobble");

// ===========================================================================
// craftSound example
// The value is a registered sound ID string (vanilla or any mod's sound).
// A single left-click craft plays once; a shift-click bulk craft plays only
// once total instead of per produced item. The sound is local to the crafter.
// ===========================================================================

// Built-in forge hammer sound (random pitch per craft for a forging feel)
Recipe.type(Type.BLACKSMITH)
    .shaped([
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>]
    ])
    .craftSound("oraculumworktables:craft.forge_hammer")
    .output(<item:minecraft:iron_block>)
    .register("blacksmith_iron_block_sound");

// Level-up sound (shapeless)
Recipe.type(Type.JEWELER)
    .shapeless([<item:minecraft:diamond_block>])
    .craftSound("minecraft:entity.player.levelup")
    .output(<item:minecraft:diamond>)
    .register("jeweler_diamond_sound");

// Using this mod's built-in meme sound (you may also omit craftSound entirely:
// when omitted, the config options enableMemeCraftSound / memeCraftSoundChance
// control whether it randomly plays).
Recipe.type(Type.BASIC)
    .shapeless([<item:minecraft:wheat>, <item:minecraft:cocoa_beans>])
    .craftSound("oraculumworktables:craft_meme")
    .output(<item:minecraft:cookie>)
    .register("basic_cookie_meme");
