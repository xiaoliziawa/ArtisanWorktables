# ArtisanWorktables 配方编写指南（CraftTweaker & KubeJS）

本文档介绍如何通过 **CraftTweaker（ZenScript）** 和 **KubeJS（JavaScript）** 为 ArtisanWorktables 的工作台添加自定义配方。

> 模组同时支持两套脚本系统，二选一即可。新项目推荐使用 **KubeJS**；CraftTweaker 仍然完全可用。

---

## 一、基础概念

### 1.1 桌型（Table Type）

每种职业对应一种桌型，配方与桌型一一绑定。共 15 种：

`tailor`（裁缝）、`carpenter`（木匠）、`mason`（石匠）、`blacksmith`（铁匠）、`jeweler`（珠宝匠）、`basic`（基础）、`engineer`（工程师）、`mage`（法师）、`scribe`（抄写员）、`chemist`（化学家）、`farmer`（农夫）、`chef`（厨师）、`designer`（设计师）、`tanner`（制革匠）、`potter`（陶工）

### 1.2 等级（Tier）

每种桌型有三个等级，高等级可使用低等级配方：

| 等级 | 名称 | id |
|------|------|----|
| 工作台 | `worktable` | 0 |
| 工作站 | `workstation` | 1 |
| 工坊 | `workshop` | 2 |

配方通过 `minimumTier` / `maximumTier`（0~2）限定可在哪些等级使用。默认 `minimumTier=0`、`maximumTier=2`（全等级可用）。

### 1.3 配方类型

- **有序配方（shaped）**：按 `pattern`（图案）+ `key`（符号映射）摆放，类似工作台有序合成。
- **无序配方（shapeless）**：只看材料种类与数量，不看摆放位置。

### 1.4 配方可配置项一览

| 字段 | 含义 | 默认值 |
|------|------|--------|
| `result` | 主输出物品 | 必填 |
| `pattern` + `key` | 有序配方图案（仅 shaped） | shaped 必填 |
| `ingredients` | 无序配方材料列表（仅 shapeless） | shapeless 必填 |
| `tools` | 所需工具（最多 3 个），每个含耐久消耗 | 无 |
| `secondaryIngredients` | 副材料（最多 9 个，放在副输入格） | 无 |
| `consumeSecondaryIngredients` | 是否消耗副材料 | `true` |
| `fluidIngredient` | 流体消耗 | 无 |
| `extraOutput` | 额外产出（最多 3 个，可带概率） | 无 |
| `minimumTier` / `maximumTier` | 等级限制（0~2） | `0` / `2` |
| `experienceRequired` | 所需经验点数 | `0` |
| `levelRequired` | 所需经验等级 | `0` |
| `consumeExperience` | 是否消耗经验/等级 | `true` |
| `mirrored` | 有序配方是否允许镜像匹配（仅 shaped） | `true` |
| `group` | 配方分组（JEI 合并显示） | 空 |

---

## 二、CraftTweaker（ZenScript）

脚本放在 `scripts/` 目录，文件后缀 `.zs`。

### 2.1 入口与链式写法

```zenscript
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;
import mods.artisanworktables.Tier;

// 通过 Recipe.type(桌型) 开始，链式调用各项，最后 register()
Recipe.type(Type.BLACKSMITH)
    .shaped([
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>]
    ])
    .output(<item:minecraft:iron_block>)
    .register();
```

### 2.2 可用方法

| 方法 | 说明 |
|------|------|
| `Recipe.type(Type.XXX)` | 指定桌型，返回配方构建器 |
| `.shaped(IIngredient[][])` | 有序配方，二维数组即图案（自动生成 pattern/key） |
| `.shapeless(IIngredient[])` | 无序配方 |
| `.output(IItemStack)` | 主输出 |
| `.tool(IIngredient, int damage)` | 添加一个工具及其耐久消耗（最多调用 3 次） |
| `.fluid(IFluidStack)` | 流体消耗 |
| `.secondary(IIngredient[])` | 副材料（默认消耗） |
| `.secondary(IIngredient[], bool consume)` | 副材料，并指定是否消耗 |
| `.extra(IItemStack, float chance)` | 额外产出及概率（0.0~1.0，最多 3 个） |
| `.mirrored(bool)` | 是否允许镜像（仅 shaped） |
| `.restrict(Tier min)` | 最低等级限制（最高默认 WORKSHOP） |
| `.restrict(Tier min, Tier max)` | 等级范围限制 |
| `.experience(int)` / `.experience(int, bool consume)` | 所需经验点数 |
| `.level(int)` / `.level(int, bool consume)` | 所需经验等级 |
| `.register()` | 注册（自动生成配方名） |
| `.register(String name)` | 注册并指定配方名 |

> 注意：`.experience(...)` 与 `.level(...)` 互斥，调用其一会清零另一项。

### 2.3 完整示例

```zenscript
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;
import mods.artisanworktables.Tier;

// 铁匠有序配方：需要钻石镐（消耗 10 耐久），消耗 1000mB 水，
// 限定工作站及以上等级，需 5 级经验，并有 50% 概率额外产出钻石。
Recipe.type(Type.BLACKSMITH)
    .shaped([
        [<item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>, <item:minecraft:iron_ingot>],
        [<item:minecraft:iron_ingot>, <item:minecraft:stick>,      <item:minecraft:iron_ingot>],
        [<item:minecraft:air>,        <item:minecraft:stick>,      <item:minecraft:air>]
    ])
    .tool(<item:minecraft:diamond_pickaxe>, 10)
    .fluid(<fluid:minecraft:water> * 1000)
    .secondary([<item:minecraft:coal>], false)
    .extra(<item:minecraft:diamond>, 0.5)
    .restrict(Tier.WORKSTATION, Tier.WORKSHOP)
    .level(5)
    .mirrored(true)
    .register("my_iron_machine");

// 厨师无序配方
Recipe.type(Type.CHEF)
    .shapeless([<item:minecraft:wheat>, <item:minecraft:wheat>, <item:minecraft:wheat>])
    .output(<item:minecraft:bread>)
    .register();
```

---

## 三、KubeJS（JavaScript）

脚本放在 `kubejs/server_scripts/` 目录，文件后缀 `.js`。配方在 `ServerEvents.recipes` 事件中添加。

### 3.1 访问方式

每个桌型有两个配方函数：

```
event.recipes.artisanworktables.<桌型>_shaped(result, pattern, key)
event.recipes.artisanworktables.<桌型>_shapeless(result, ingredients)
```

例如 `event.recipes.artisanworktables.blacksmith_shaped(...)`、`event.recipes.artisanworktables.chef_shapeless(...)`。

### 3.2 构造参数

- **有序**：`(result, pattern, key)`
  - `result`：物品（字符串 id 或 `Item.of(...)`）
  - `pattern`：字符串数组，如 `['XXX', 'X X', 'XXX']`
  - `key`：符号→材料 的对象，如 `{ X: 'minecraft:iron_ingot' }`
- **无序**：`(result, ingredients)`
  - `ingredients`：材料数组，如 `['minecraft:wheat', 'minecraft:wheat']`

### 3.3 链式可选项（builder 方法）

构造后可链式调用以下方法（方法名与字段同名）：

| 方法 | 说明 |
|------|------|
| `.mirrored(bool)` | 是否镜像（仅 shaped） |
| `.group(string)` | 配方分组 |
| `.secondaryIngredients([...])` | 副材料数组 |
| `.consumeSecondaryIngredients(bool)` | 是否消耗副材料 |
| `.tools([...])` | 工具数组（见下方格式） |
| `.extraOutput([...])` | 额外产出数组（见下方格式） |
| `.fluidIngredient({...})` | 流体消耗（见下方格式） |
| `.minimumTier(int)` / `.maximumTier(int)` | 等级范围（0~2） |
| `.experienceRequired(int)` | 所需经验点数 |
| `.levelRequired(int)` | 所需经验等级 |
| `.consumeExperience(bool)` | 是否消耗经验/等级 |

### 3.4 特殊字段的 JSON 格式

`tools` / `extraOutput` / `fluidIngredient` 使用模组原生 JSON 结构，直接以 JS 对象书写：

```js
// 工具：item/tag 为材料，damage 为耐久消耗
.tools([
  { item: 'minecraft:diamond_pickaxe', damage: 10 },
  { tag: 'forge:tools/hammers',        damage: 5  }
])

// 额外产出：item + count + chance（概率 0.0~1.0）
.extraOutput([
  { item: 'minecraft:diamond', count: 1, chance: 0.5 }
])

// 流体：fluid + amount（毫桶 mB）
.fluidIngredient({ fluid: 'minecraft:water', amount: 1000 })
```

### 3.5 完整示例

```js
ServerEvents.recipes(event => {

  // 铁匠有序配方
  event.recipes.artisanworktables.blacksmith_shaped(
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
    .secondaryIngredients(['minecraft:coal'])
    .consumeSecondaryIngredients(false)
    .extraOutput([{ item: 'minecraft:diamond', count: 1, chance: 0.5 }])
    .minimumTier(1)
    .maximumTier(2)
    .levelRequired(5)
    .mirrored(true)

  // 厨师无序配方
  event.recipes.artisanworktables.chef_shapeless(
    'minecraft:bread',
    ['minecraft:wheat', 'minecraft:wheat', 'minecraft:wheat']
  )
})
```

### 3.6 移除配方

KubeJS 可按配方类型移除：

```js
ServerEvents.recipes(event => {
  // 移除某桌型的全部有序配方
  event.remove({ type: 'artisanworktables:blacksmith_shaped' })
  // 按输出移除
  event.remove({ output: 'minecraft:iron_block' })
})
```

---

## 四、字段对照表（CraftTweaker ↔ KubeJS ↔ JSON）

| 含义 | CraftTweaker | KubeJS | JSON 字段 |
|------|--------------|--------|-----------|
| 主输出 | `.output(stack)` | 构造参数 `result` | `result` |
| 有序图案 | `.shaped([[...]])` | 构造参数 `pattern` + `key` | `pattern` + `key` |
| 无序材料 | `.shapeless([...])` | 构造参数 `ingredients` | `ingredients` |
| 工具 | `.tool(ing, dmg)` | `.tools([{item,damage}])` | `tools` |
| 副材料 | `.secondary([...], consume)` | `.secondaryIngredients([...])` + `.consumeSecondaryIngredients(b)` | `secondaryIngredients` / `consumeSecondaryIngredients` |
| 流体 | `.fluid(fluidStack)` | `.fluidIngredient({fluid,amount})` | `fluidIngredient` |
| 额外产出 | `.extra(stack, chance)` | `.extraOutput([{item,count,chance}])` | `extraOutput` |
| 等级范围 | `.restrict(min, max)` | `.minimumTier(n)` + `.maximumTier(n)` | `minimumTier` / `maximumTier` |
| 经验点数 | `.experience(n)` | `.experienceRequired(n)` + `.consumeExperience(b)` | `experienceRequired` / `consumeExperience` |
| 经验等级 | `.level(n)` | `.levelRequired(n)` | `levelRequired` |
| 镜像 | `.mirrored(b)` | `.mirrored(b)` | `mirrored` |
| 分组 | — | `.group(s)` | `group` |

---

## 五、常见问题

- **配方不生效？** 确认桌型名拼写正确（全小写），且物品/流体 id 存在。可在 JEI 中查看对应桌型分类核对。
- **工具不被识别？** `tools` 中的 `item`/`tag` 必须是合法材料，`damage` 是单次合成消耗的耐久；并确认该工具在对应工具处理器支持范围内。
- **等级限制无效？** `minimumTier`/`maximumTier` 取值 0（工作台）、1（工作站）、2（工坊）。
- **修改后需重载：** KubeJS 用 `/reload`；CraftTweaker 用 `/reload`（部分改动需重进存档）。
