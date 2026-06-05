// priority: 0
// ============================================================================
// OraculumWorktables - KubeJS (JavaScript) 配方示例
// 覆盖全部 15 种桌型，每种各一个示例，并演示不同的可选项。
// 放置位置：kubejs/server_scripts/，修改后使用 /reload 重载。
//
// 调用形式：
//   event.recipes.oraculumworktables.<桌型>_shaped(result, pattern, key)
//   event.recipes.oraculumworktables.<桌型>_shapeless(result, ingredients)
// 之后可链式调用 .tools(...) / .fluidIngredient(...) / .extraOutput(...) 等。
// ============================================================================

ServerEvents.recipes(event => {
  const art = event.recipes.oraculumworktables

  // -------------------------------------------------------------------------
  // 1. TAILOR（裁缝）—— 有序，使用工具（剪刀，消耗 1 点耐久）
  // -------------------------------------------------------------------------
  art.tailor_shaped(
    'minecraft:leather_chestplate',
    [
      'LLL',
      'L L'
    ],
    { L: 'minecraft:leather' }
  ).tools([{ item: 'minecraft:shears', damage: 1 }])

  // -------------------------------------------------------------------------
  // 2. CARPENTER（木匠）—— 有序，需要斧头，演示镜像匹配
  // -------------------------------------------------------------------------
  art.carpenter_shaped(
    'minecraft:crafting_table',
    [
      'PP',
      'PP'
    ],
    { P: 'minecraft:oak_planks' }
  )
    .tools([{ item: 'minecraft:iron_axe', damage: 1 }])
    .mirrored(true)

  // -------------------------------------------------------------------------
  // 3. MASON（石匠）—— 有序，需要镐，限定工作站及以上等级
  // -------------------------------------------------------------------------
  art.mason_shaped(
    Item.of('minecraft:stone_bricks', 4),
    [
      'SS',
      'SS'
    ],
    { S: 'minecraft:stone' }
  )
    .tools([{ item: 'minecraft:stone_pickaxe', damage: 1 }])
    .minimumTier(1)

  // -------------------------------------------------------------------------
  // 4. BLACKSMITH（铁匠）—— 工具 + 流体 + 经验等级 + 额外产出（综合示例）
  // -------------------------------------------------------------------------
  art.blacksmith_shaped(
    'minecraft:iron_block',
    [
      'III',
      'ISI',
      ' S '
    ],
    {
      I: 'minecraft:iron_ingot',
      S: 'minecraft:stick'
    }
  )
    .tools([{ item: 'minecraft:diamond_pickaxe', damage: 10 }])
    .fluidIngredient({ fluid: 'minecraft:water', amount: 1000 })
    .extraOutput([{ item: 'minecraft:iron_nugget', count: 1, chance: 0.5 }])
    .minimumTier(1)
    .maximumTier(2)
    .levelRequired(3)

  // -------------------------------------------------------------------------
  // 5. JEWELER（珠宝匠）—— 副材料（不消耗，作为催化）+ 概率额外产出
  // -------------------------------------------------------------------------
  art.jeweler_shaped(
    'minecraft:enchanted_golden_apple',
    ['DGD'],
    {
      D: 'minecraft:diamond',
      G: 'minecraft:gold_ingot'
    }
  )
    .secondaryIngredients(['minecraft:emerald'])
    .consumeSecondaryIngredients(false)
    .extraOutput([{ item: 'minecraft:diamond', count: 1, chance: 0.25 }])

  // -------------------------------------------------------------------------
  // 6. BASIC（基础）—— 无序，最简单的写法
  // -------------------------------------------------------------------------
  art.basic_shapeless(
    Item.of('minecraft:oak_planks', 4),
    ['minecraft:oak_log']
  )

  // -------------------------------------------------------------------------
  // 7. ENGINEER（工程师）—— 副材料（消耗）+ 工具
  // -------------------------------------------------------------------------
  art.engineer_shaped(
    'minecraft:piston',
    [
      'IRI',
      'IRI'
    ],
    {
      I: 'minecraft:iron_ingot',
      R: 'minecraft:redstone'
    }
  )
    .tools([{ item: 'minecraft:iron_pickaxe', damage: 2 }])
    .secondaryIngredients(['minecraft:copper_ingot', 'minecraft:copper_ingot'])

  // -------------------------------------------------------------------------
  // 8. MAGE（法师）—— 消耗经验等级（作为门槛）
  // -------------------------------------------------------------------------
  art.mage_shaped(
    Item.of('minecraft:enchanted_book', '{StoredEnchantments:[{id:"minecraft:sharpness",lvl:5s}]}'),
    [
      'LBL',
      'LEL'
    ],
    {
      L: 'minecraft:lapis_lazuli',
      B: 'minecraft:book',
      E: 'minecraft:experience_bottle'
    }
  ).levelRequired(5)

  // -------------------------------------------------------------------------
  // 9. SCRIBE（抄写员）—— 无序，纯材料组合
  // -------------------------------------------------------------------------
  art.scribe_shapeless(
    'minecraft:book',
    ['minecraft:paper', 'minecraft:paper', 'minecraft:paper', 'minecraft:leather']
  )

  // -------------------------------------------------------------------------
  // 10. CHEMIST（化学家）—— 无序，消耗流体
  // -------------------------------------------------------------------------
  art.chemist_shapeless(
    'minecraft:tnt',
    ['minecraft:gunpowder', 'minecraft:redstone']
  ).fluidIngredient({ fluid: 'minecraft:water', amount: 500 })

  // -------------------------------------------------------------------------
  // 11. FARMER（农夫）—— 无序，概率额外产出（种子）
  // -------------------------------------------------------------------------
  art.farmer_shapeless(
    'minecraft:bread',
    ['minecraft:wheat', 'minecraft:wheat', 'minecraft:wheat']
  ).extraOutput([{ item: 'minecraft:wheat_seeds', count: 1, chance: 0.5 }])

  // -------------------------------------------------------------------------
  // 12. CHEF（厨师）—— 无序，需要经验点数（消耗）
  // -------------------------------------------------------------------------
  art.chef_shapeless(
    Item.of('minecraft:cooked_beef', 2),
    ['minecraft:cooked_beef', 'minecraft:bread', 'minecraft:carrot']
  ).experienceRequired(10)

  // -------------------------------------------------------------------------
  // 13. DESIGNER（设计师）—— 有序，染色相关
  // -------------------------------------------------------------------------
  art.designer_shaped(
    Item.of('minecraft:blue_wool', 2),
    ['WDW'],
    {
      W: 'minecraft:white_wool',
      D: 'minecraft:blue_dye'
    }
  )

  // -------------------------------------------------------------------------
  // 14. TANNER（制革匠）—— 工具 + 流体，限定单一等级（仅工作台）
  // -------------------------------------------------------------------------
  art.tanner_shaped(
    'minecraft:leather',
    [
      'HH',
      'HH'
    ],
    { H: 'minecraft:rabbit_hide' }
  )
    .tools([{ item: 'minecraft:flint', damage: 1 }])
    .fluidIngredient({ fluid: 'minecraft:water', amount: 250 })
    .minimumTier(0)
    .maximumTier(0)

  // -------------------------------------------------------------------------
  // 15. POTTER（陶工）—— 流体 + 副材料 + 额外产出（综合示例）
  // -------------------------------------------------------------------------
  art.potter_shaped(
    'minecraft:terracotta',
    [
      'CC',
      'CC'
    ],
    { C: 'minecraft:clay_ball' }
  )
    .fluidIngredient({ fluid: 'minecraft:water', amount: 100 })
    .secondaryIngredients(['minecraft:sand'])
    .extraOutput([{ item: 'minecraft:brick', count: 1, chance: 0.3 }])

  // ===========================================================================
  // 标签（Tag）示例
  // key / ingredients / secondaryIngredients 用 '#命名空间:路径'；
  // tools 走原生 JSON，用 { tag: '命名空间:路径' }（不带 #）。
  // 输出（result / extraOutput）与流体（fluidIngredient）不支持 tag。
  // 1.20.1 Forge 物品标签命名空间用 forge:（非 1.21+ 的 c:）。
  // ===========================================================================

  // 全 tag 输入：key + 工具 + 副材料均使用标签
  art.blacksmith_shaped(
    'minecraft:anvil',
    [
      'II',
      'II'
    ],
    { I: '#forge:ingots/iron' }
  )
    .tools([{ tag: 'minecraft:pickaxes', damage: 1 }])
    .secondaryIngredients(['#forge:gems/diamond'])
    .consumeSecondaryIngredients(false)

  // 无序 + tag：任意原木 → 木棍
  art.basic_shapeless(
    Item.of('minecraft:stick', 4),
    ['#minecraft:logs']
  )

  // ===========================================================================
  // matchNbt 工具示例
  // 要求一把名为 "Hammer" 的铁镐（matchNbt: true，按 NBT 匹配，自动忽略耐久）。
  // 普通未命名的铁镐不满足该配方。
  // ===========================================================================
  art.mason_shapeless(
    'minecraft:cobblestone',
    ['minecraft:stone']
  ).tools([
    { item: 'minecraft:iron_pickaxe', damage: 10, matchNbt: true, nbt: '{display:{Name:\'{"text":"Hammer"}\'}}' }
  ])

  // ===========================================================================
  // craftSound 合成音效示例
  // 取值为已注册的音效 ID 字符串（原版或任意模组的音效）。
  // 单次左键合成播放一次；Shift 批量合成整次只播放一次，不会按产出数量重复。
  // 音效仅对执行合成的玩家本地播放。
  // ===========================================================================

  // 内置锻锤音效（每次合成随机升/降调，锻造感）
  art.blacksmith_shaped(
    'minecraft:iron_block',
    [
      'III',
      'III',
      'III'
    ],
    { I: 'minecraft:iron_ingot' }
  ).craftSound('oraculumworktables:craft.saw')

  // 升级音效（无序）
  art.jeweler_shapeless(
    'minecraft:diamond',
    ['minecraft:diamond_block']
  ).craftSound('minecraft:entity.player.levelup')

  // 使用本模组内置的彩蛋音效（也可不指定 craftSound：
  // 未指定时由配置 enableMemeCraftSound / memeCraftSoundChance 控制是否随机播放）
  art.basic_shapeless(
    'minecraft:cookie',
    ['minecraft:wheat', 'minecraft:cocoa_beans']
  ).craftSound('oraculumworktables:craft_meme')
})
