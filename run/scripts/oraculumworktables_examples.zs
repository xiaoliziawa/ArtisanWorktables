// ============================================================================
// OraculumWorktables - CraftTweaker (ZenScript) 配方示例
// 覆盖全部 15 种桌型，每种各一个示例，并演示不同的可选项。
// 放置位置：run/scripts/  （或实例的 scripts/ 目录），修改后使用 /reload 重载。
// ============================================================================

import mods.oraculumworktables.Recipe;
import mods.oraculumworktables.Type;
import mods.oraculumworktables.Tier;

// ---------------------------------------------------------------------------
// 1. TAILOR（裁缝）—— 有序，使用工具（剪刀，消耗 1 点耐久）
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
// 2. CARPENTER（木匠）—— 有序，需要斧头，演示镜像匹配
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
// 3. MASON（石匠）—— 有序，需要镐，限定工作站及以上等级
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
// 4. BLACKSMITH（铁匠）—— 有序，工具 + 流体 + 经验等级 + 额外产出（综合示例）
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
// 5. JEWELER（珠宝匠）—— 有序，副材料（不消耗，作为催化）+ 概率额外产出
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
// 6. BASIC（基础）—— 无序，最简单的写法
// ---------------------------------------------------------------------------
Recipe.type(Type.BASIC)
    .shapeless([<item:minecraft:oak_log>])
    .output(<item:minecraft:oak_planks> * 4)
    .register("basic_planks");

// ---------------------------------------------------------------------------
// 7. ENGINEER（工程师）—— 有序，副材料（消耗）+ 工具
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
// 8. MAGE（法师）—— 有序，消耗经验等级（不消耗经验，仅作为门槛）
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
// 9. SCRIBE（抄写员）—— 无序，演示纯材料组合
// ---------------------------------------------------------------------------
Recipe.type(Type.SCRIBE)
    .shapeless([<item:minecraft:paper>, <item:minecraft:paper>, <item:minecraft:paper>, <item:minecraft:leather>])
    .output(<item:minecraft:book>)
    .register("scribe_book");

// ---------------------------------------------------------------------------
// 10. CHEMIST（化学家）—— 无序，消耗流体
// ---------------------------------------------------------------------------
Recipe.type(Type.CHEMIST)
    .shapeless([<item:minecraft:gunpowder>, <item:minecraft:redstone>])
    .fluid(<fluid:minecraft:water> * 500)
    .output(<item:minecraft:tnt>)
    .register("chemist_tnt");

// ---------------------------------------------------------------------------
// 11. FARMER（农夫）—— 无序，概率额外产出（种子）
// ---------------------------------------------------------------------------
Recipe.type(Type.FARMER)
    .shapeless([<item:minecraft:wheat>, <item:minecraft:wheat>, <item:minecraft:wheat>])
    .extra(<item:minecraft:wheat_seeds>, 0.5)
    .output(<item:minecraft:bread>)
    .register("farmer_bread");

// ---------------------------------------------------------------------------
// 12. CHEF（厨师）—— 无序，需要经验点数（消耗）
// ---------------------------------------------------------------------------
Recipe.type(Type.CHEF)
    .shapeless([<item:minecraft:cooked_beef>, <item:minecraft:bread>, <item:minecraft:carrot>])
    .experience(10)
    .output(<item:minecraft:cooked_beef> * 2)
    .register("chef_meal");

// ---------------------------------------------------------------------------
// 13. DESIGNER（设计师）—— 有序，染色相关
// ---------------------------------------------------------------------------
Recipe.type(Type.DESIGNER)
    .shaped([
        [<item:minecraft:white_wool>, <item:minecraft:blue_dye>, <item:minecraft:white_wool>]
    ])
    .output(<item:minecraft:blue_wool> * 2)
    .register("designer_blue_wool");

// ---------------------------------------------------------------------------
// 14. TANNER（制革匠）—— 有序，工具 + 流体，限定单一等级（仅工作台）
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
// 15. POTTER（陶工）—— 有序，流体 + 副材料 + 额外产出（综合示例）
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
// 标签（Tag）示例
// 输入类（主材料、副材料、工具）可用 <tag:items:...>；
// 输出（output / extra）与流体（fluid）不支持 tag，必须是具体 id。
// 1.20.1 Forge 物品标签命名空间用 forge:（非 1.21+ 的 c:）。
// ===========================================================================

// 全 tag 输入：主材料 + 工具 + 副材料均使用标签
Recipe.type(Type.BLACKSMITH)
    .shaped([
        [<tag:items:forge:ingots/iron>, <tag:items:forge:ingots/iron>],
        [<tag:items:forge:ingots/iron>, <tag:items:forge:ingots/iron>]
    ])
    .tool(<tag:items:minecraft:pickaxes>, 1)
    .secondary([<tag:items:forge:gems/diamond>], false)
    .output(<item:minecraft:anvil>)
    .register("blacksmith_anvil_tag");

// 无序 + tag：任意原木 → 木棍
Recipe.type(Type.BASIC)
    .shapeless([<tag:items:minecraft:logs>])
    .output(<item:minecraft:stick> * 4)
    .register("basic_sticks_tag");

// ===========================================================================
// matchNbt 工具示例
// 要求一把名为 "Hammer" 的铁镐（第三个参数 true = 按 NBT 匹配，自动忽略耐久）。
// 普通未命名的铁镐不满足该配方。
// ===========================================================================
val hammer = <item:minecraft:iron_pickaxe>.withTag({display: {Name: "{\"text\":\"Hammer\"}"}});

Recipe.type(Type.MASON)
    .shapeless([<item:minecraft:stone>])
    .tool(hammer, 10, true)
    .output(<item:minecraft:cobblestone>)
    .register("mason_hammer_cobble");

// ===========================================================================
// craftSound 合成音效示例
// 取值为已注册的音效 ID 字符串（原版或任意模组的音效）。
// 单次左键合成播放一次；Shift 批量合成整次只播放一次，不会按产出数量重复。
// 音效仅对执行合成的玩家本地播放。
// ===========================================================================

// 内置锻锤音效（每次合成随机升/降调，锻造感）
Recipe.type(Type.BLACKSMITH)
    .shaped([
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>]
    ])
    .craftSound("oraculumworktables:craft.forge_hammer")
    .output(<item:minecraft:iron_block>)
    .register("blacksmith_iron_block_sound");

// 升级音效（无序）
Recipe.type(Type.JEWELER)
    .shapeless([<item:minecraft:diamond_block>])
    .craftSound("minecraft:entity.player.levelup")
    .output(<item:minecraft:diamond>)
    .register("jeweler_diamond_sound");

// 使用本模组内置的彩蛋音效（也可不指定 craftSound：
// 未指定时由配置 enableMemeCraftSound / memeCraftSoundChance 控制是否随机播放）
Recipe.type(Type.BASIC)
    .shapeless([<item:minecraft:wheat>, <item:minecraft:cocoa_beans>])
    .craftSound("oraculumworktables:craft_meme")
    .output(<item:minecraft:cookie>)
    .register("basic_cookie_meme");
