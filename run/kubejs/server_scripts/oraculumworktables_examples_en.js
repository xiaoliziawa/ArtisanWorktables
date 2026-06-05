// priority: 0
// ============================================================================
// OraculumWorktables - KubeJS (JavaScript) recipe examples
// Covers all 15 table types, one example each, demonstrating various options.
// Location: kubejs/server_scripts/ ; run /reload after editing.
//
// Call form:
//   event.recipes.oraculumworktables.<tableType>_shaped(result, pattern, key)
//   event.recipes.oraculumworktables.<tableType>_shapeless(result, ingredients)
// You can then chain .tools(...) / .fluidIngredient(...) / .extraOutput(...) etc.
// ============================================================================

ServerEvents.recipes(event => {
  const art = event.recipes.oraculumworktables

  // -------------------------------------------------------------------------
  // 1. TAILOR -- shaped, uses a tool (shears, costs 1 durability)
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
  // 2. CARPENTER -- shaped, requires an axe, demonstrates mirrored matching
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
  // 3. MASON -- shaped, requires a pickaxe, restricted to workstation and above
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
  // 4. BLACKSMITH -- tool + fluid + experience level + extra output (combined example)
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
  // 5. JEWELER -- secondary ingredient (not consumed, acts as a catalyst) + chance extra output
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
  // 6. BASIC -- shapeless, the simplest form
  // -------------------------------------------------------------------------
  art.basic_shapeless(
    Item.of('minecraft:oak_planks', 4),
    ['minecraft:oak_log']
  )

  // -------------------------------------------------------------------------
  // 7. ENGINEER -- secondary ingredients (consumed) + tool
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
  // 8. MAGE -- consumes an experience level (as a threshold)
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
  // 9. SCRIBE -- shapeless, pure ingredient combination
  // -------------------------------------------------------------------------
  art.scribe_shapeless(
    'minecraft:book',
    ['minecraft:paper', 'minecraft:paper', 'minecraft:paper', 'minecraft:leather']
  )

  // -------------------------------------------------------------------------
  // 10. CHEMIST -- shapeless, consumes fluid
  // -------------------------------------------------------------------------
  art.chemist_shapeless(
    'minecraft:tnt',
    ['minecraft:gunpowder', 'minecraft:redstone']
  ).fluidIngredient({ fluid: 'minecraft:water', amount: 500 })

  // -------------------------------------------------------------------------
  // 11. FARMER -- shapeless, chance extra output (seeds)
  // -------------------------------------------------------------------------
  art.farmer_shapeless(
    'minecraft:bread',
    ['minecraft:wheat', 'minecraft:wheat', 'minecraft:wheat']
  ).extraOutput([{ item: 'minecraft:wheat_seeds', count: 1, chance: 0.5 }])

  // -------------------------------------------------------------------------
  // 12. CHEF -- shapeless, requires experience points (consumed)
  // -------------------------------------------------------------------------
  art.chef_shapeless(
    Item.of('minecraft:cooked_beef', 2),
    ['minecraft:cooked_beef', 'minecraft:bread', 'minecraft:carrot']
  ).experienceRequired(10)

  // -------------------------------------------------------------------------
  // 13. DESIGNER -- shaped, dyeing related
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
  // 14. TANNER -- tool + fluid, restricted to a single tier (worktable only)
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
  // 15. POTTER -- fluid + secondary ingredient + extra output (combined example)
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
  // Tag examples
  // key / ingredients / secondaryIngredients use '#namespace:path';
  // tools pass through native JSON, so use { tag: 'namespace:path' } (no #).
  // Output (result / extraOutput) and fluid (fluidIngredient) do NOT support tags.
  // On 1.20.1 Forge, item tags use the forge: namespace (not the c: of 1.21+).
  // ===========================================================================

  // All-tag inputs: key + tool + secondary ingredients all use tags
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

  // Shapeless + tag: any log -> sticks
  art.basic_shapeless(
    Item.of('minecraft:stick', 4),
    ['#minecraft:logs']
  )

  // ===========================================================================
  // matchNbt tool example
  // Requires an iron pickaxe named "Hammer" (matchNbt: true, matches by NBT, ignores durability automatically).
  // A plain unnamed iron pickaxe does NOT satisfy this recipe.
  // ===========================================================================
  art.mason_shapeless(
    'minecraft:cobblestone',
    ['minecraft:stone']
  ).tools([
    { item: 'minecraft:iron_pickaxe', damage: 10, matchNbt: true, nbt: '{display:{Name:\'{"text":"Hammer"}\'}}' }
  ])

  // ===========================================================================
  // craftSound example
  // The value is a registered sound ID string (vanilla or any mod's sound).
  // A single left-click craft plays once; a shift-click bulk craft plays only
  // once total instead of per produced item. The sound is local to the crafter.
  // ===========================================================================

  // Built-in forge hammer sound (random pitch per craft for a forging feel)
  art.blacksmith_shaped(
    'minecraft:iron_block',
    [
      'III',
      'III',
      'III'
    ],
    { I: 'minecraft:iron_ingot' }
  ).craftSound('oraculumworktables:craft.forge_hammer')

  // Level-up sound (shapeless)
  art.jeweler_shapeless(
    'minecraft:diamond',
    ['minecraft:diamond_block']
  ).craftSound('minecraft:entity.player.levelup')

  // Using this mod's built-in meme sound (you may also omit craftSound entirely:
  // when omitted, the config options enableMemeCraftSound / memeCraftSoundChance
  // control whether it randomly plays).
  art.basic_shapeless(
    'minecraft:cookie',
    ['minecraft:wheat', 'minecraft:cocoa_beans']
  ).craftSound('oraculumworktables:craft_meme')
})
