## 0.7.3
- improved little spiders to produce special kinds of cobwebs like ender, redstone, glowstone, limsew dropping piles - those combined create final product
- added tool to mine obsidian (and sky stone from AE2) - *Pointed Pick*, also hits hard creepers and endermen, chance on spawning enderman when killing zombie or creeper
- added switchgrass charcoal 
- added support for special cobwebs into cobweb harvester
- removed cobweb search cost from cobweb harvester, increased speed and internal power buffer
- added custom little spiders' eggs, replacement for spawner eggs (when entity ID changed those spawner eggs got broken)
- added spider "happiness" affecting rate of cobweb production (better results in dark, not crowded areas)
- spiders are spawning eggs - similar to chickens and ducks
- increased dust gain from mallet recipe (jaffarrol ore -> 2x dust)
- slightly boosted jaffarrol tools to match tools from other mods - jaffarrol tools were more expensive, yet worse

## 0.7.2b
- rewritten power sources extraction (they are working with internal power storage now), fixes not working energy pipes from other mods
- added energy bar and generated power amount to power source GUIs
- partially fixed compatibility with mods using older version of Redstone Flux API

## 0.7.2a
- updated to latest RedstoneFlux API

## 0.7.2
- added unfailing water bucket (endless portable water supply)
- moved to Redstone Flux power
- fixed entity registration (lead to conflicting IDs)
- BuildCraft power integration made more robust (unexpected behaviour of BC API) <- only applies to PRE1 version
- refactored out power source behavour from generator and wind generator to machine tile class
- fixed power string in wind generator GUI
- added power amount label to generator GUI
- added power unit to label of power machines GUI

## 0.7.1
- ported to 1.7.10
- fixed thermal expansion integration
- removed cloaks (MC EULA forbids it) and implemented sash for creators/donators
- added coconut lamp
- added Waila support for our crops (maturity meter)
- lowered tree seeds cost in recipes
- added recipes for seeds from crop products

## 0.7.0
- ported to 1.7.2 (Forge 1121, might not work with other versions)
- updated plants: added support for right click to collect
- added more crop stages, now every crop has 5 stages (from 3)
- sligtly modified some crop textures
- fixed internal names of food items (breaks current worlds)
- changed switchgrass - harvestable by right click, no drop quantity randomness
- fruit collector: slightly changed costs - actions eat more energy, but searching is free
- updated all blocks and items to use language file (translations are now possible, yay!)
- updated achievements: use of lang file
- recipes with sticks accept sticks from oredict, not just vanilla ones
- raised seed drop from plants
- machines no longer use energy when not being used (counteracted perdition from BuildCraft power)
- made collector's range configurable (and boosted it a bit)
- fixed not-refreshing client-side inventory when spamming sink (holding right mouse button)

## 0.6.3e
- fixed crash on using broken tool (weird thing is it worked fine, possibly Forge changed something and it got broken)

## 0.6.3d
- implemented use of ThermalExpansion 3 API (IMC)
- fixed double fishStickCooked creation
- added config option to disable ThermalExpansion integration

## 0.6.3
- juice maker (juicer, with NEI support)
- more juice kinds (carrot, melon, strawberry and tomato)
- custom villager trading (buys fruits and crops, jaffarrol ingots, sells tools and armours)
- rewritten juice and juice glass item (deprecated ones will be removed in next release)
- rewritten sink to use the new fluids registry
- fixed not refreshing of player's inventory when using sink with full hotbar
- fixed improper handling of custom sized (modded) crafting matrices
- banned use of arcane workbench from Thaumcraft by crafting handlers (in current form it crashes game) - can be disabled in config
- internal changes (mainly moved some machines code to core, multi-item trait)

## 0.6.2
- duck eggs are now fully supported by all vanilla and jaffas recipes
- updated to Forge #953
- updated BuildCraft API
- fixed texture of item in air renderer
- fixed duck not following when holding seeds
- changed version check to use jaffasfiles.tk (moxo.cz hosting was frequently down)
- made class scanning more resilient

## 0.6.1
- added fish fillet, fish sticks (fingers)
- added lollipops
- added soups - fish, tomato and pea
- added collecting farmer's bag - when harvesting crops with our hoe the bag will try to suck up all the loot
- added planting farmer's bags - fill with seeds and you can sow your field with one click
- implemented new MFR API
- rewritten switchgrass planting with our hoe (now supports much larger patterns, also it won't allow planting when switchgrass square is touching another switgrass block - to protect player from mistakes)
- modified crafting-handlers as suggested (place leftovers to player's inventory, I hope it won't be crashing other auto-crafting mods)
- fixed crash when turbine breaks
- fixed dying of crops when no farmland block is under it
- under the hood: alot of refactoring and rewring, mainly containers, items and blocks now uses icondescriptor trait, some cleaning of Items class, machines and BC API moved to core

## 0.6.0
- ported to 1.6.4
- buffed recipe: switchgrass → paper

## 0.5.2
- power from wind
- ripening box
- dairy products recipes
  bucket with milk → milk and cream
  cream + mallet → butter
  milk + mallet → “raw cheese”
- NEI is no longer required for showing tooltips
- fixed rendering issue of glassy construction block
- implemented generic support for NEI of processing machines (grinder, toaster)
- changed GUI of web harvester
- rewritten entity ID assigning from config
- new sounds for home stone and duck
- added pipe wrench to tools tab
- added redstone block to redstone tab
- added support for wolf foods (mutton, duck)
- raw duck is now eatable
- added configuration option to disable auto un-equipping
- upgraded sword to allow easy (and spider-safe) web harvesting
- added armours to combat creative tab
- more recipes:
  paper from switchgrass
  books from plank, silk and paper 
  flint and steel with jaffarrol
- nerfed pickaxe - recipe is now costlier
- fixed ghost tooltip on generator GUI
- fixed item titles to be used as internal item names
- added jaffarrol nugget (modified monster drop table and recipes)
- fixed internal names of sink and construction block
- recycling recipes for jaffarrol tools
- added limsew drop from slimes

## 0.5.1
- fixed server crash when placing fungi box
- added spider farm (little spider creature, web harvester)
- rewritten lightning hook - it looks it's now compatible with MCPC+
- armor repairing/enchanting via recipes (similar to our tools)
- upgraded hoe to till 3×3 when crouching
- jarmor un-equips itself when almost broken
- added jaffarrol tools and weapon to proper creative tabs
- hidden blocks from creative - keg, plant post, fermenter, dummy construction block
- fixed cloak hook to be more aggressive (no more cape overrides by poorly coded mods)
- fixed home stone to reset fall counter
- fixed metadata on a sapling when picked in creative
- kitchen board now accepts wolf and duck meat
- modified kitchen board, fridge and collector to implement new inventory interface

## 0.5.0
- added machines using/producing BuildCraft power: generator, grinder (replaces item meat grinder), toaster, kitchen unit
- added decorative lamps
- added redstone blocks: analog repeater, sample-and-hold, randomizer
- reworked cake packages, now supports all kinds of jaffas!
- upgraded our hoe - mass-harvest and mass-plant of switchgrass (1x1 and 3x3 mode), also supports harvesting of any IPlantable blocks (flowers, wheat)
- added pipe wrench
- added omni wrench compatibility
- added our items to dungeon loot tables
- rewritten jaffa bomb (spawns random jaffas and the recipe got a bit more difficult)
- added option to change an offset of an achievement IDs
- changed home stone recipes (first one is more expensive, better ones are created by upgrading worse ones)
- added xp from jaffarrol ores (only naturally occurring ones)
- fixed mining with jaffarrol tools in broken state
- fixed snow harvesting with jaffarrol shovel and wooden material detection of jaffarrol axe
- fixed shift-clicking in most containers
- fixed bug in custom starts of ID intervals
- switchgrass now won't spawn in frozen biomes and "dies" when frozen block is around
- fixed switchgrass not breaking in certain cases
- fixed liquid saving in fermenter
- changed drying rack to be harvestable not only by bare hand
- fixed collector - now accepts coconuts
- fixed axe damage against entities
- minor change of beer chain - before grinding one must combine hop and wheat

## 0.4.22
- beer brewing
- cobble breaker
- crisps
- "vanilla" recipes

## 0.4.21
- changed naming of TEs (read more at wiki - ignoring this change can lead to a damaged world save!)
- new mushroom - fly agaric
- multi-lamp
- new foods - shroomburger and fruit salad
- sink supports containers from other mods
- meat drying
- fixed crash when is no space in a crafting matrix for a byproduct

## 0.4.20
- mushrooms
- banana tree
- compost and composting tank
- NEI support for kitchen board
- switchgrass block (and slime spawning)
- rewritten repair recipe (now supports NEI)
- custom starts of ID intervals
- fixed pickaxe to be able to mine same blocks as diamond pickaxe
- more work on a guide book (mostly composting tank item values)

## 0.4.19
- implemented achievement system (not much content yet)
- rewritten to not use external libraries (lombok, jsoup)
- added meat cleaver support for more animals (wolf, sheep, duck)
- rewritten duck spawning to use biome dictionary (it should be compatible with ExtraBiomesXL and Bimes o' Plenty)

## 0.4.18
- update to 1.5.2
- compatibility with TE (mainly ore → 2x dusts in pulverizer)
- enchant and repair recipes
- new version message should appear only once per version
- ore recipes disabled by default

## 0.4.17
- customizable duck spawn rate
- ore dictionary compatility for Better Farming (orange & lemon)
- leaves tinting depending on a biome
- fixed path detection in core
- leaves, saplings, switchgrass and more are now flammable
- removed seed bag
- bonemeal-able switchgrass
- fixed crash on egg throw
- reworked bonemealing of plants and trees to require multiple usage
- Mine Factory Reloaded support (plants, switchgrass, saplings/seeds of trees)

## 0.4.16
- separated core
- rewritten tree saplings to support custom titles
- fixed tree bonemealing to not change type of tree

## 0.4.15
- fixed pies (rendering and saving)
- fixed names of tree seeds

## 0.4.14
- added support for modded wood and planks
- added config option to disable achievements
- fixed recipes with "any damage"
- fixed server crash

## 0.4.13
- update to 1.5.1
- trees and plants support fertilizers from other forge mods

## 0.4.12
- added lightning conductor
- added mini-locomotive
- reworked seeds getting
- fixed durability of tables
- throwable duck eggs

## 0.4.11
- jaffarrol tools
- ores
- switchgrass
- hot cocoa, coco bar
- tables
- updated sink

## 0.4.10
- new ice-creams
- duck (entity, armor, duck à l'orange, compatibility with ExtraBiomesXL)
- lamb with peas, baked beans with tomato sauce, muffins, sandwich
- smarter ID assigning after update
- rewritten teleportation of home stone (hopefuly fixed falling bug)

## 0.4.9
- added home stones (beta, sometimes teleports player under bed)
- wolf armor is repairable in anvil
- added more food from animals/monsters - mutton, wolf meat, spider leg
- added jarmor
- added juices
- added caramelized peanuts, stuffed pepper, cooked mushrooms

## 0.4.8
- added pies
- some fruits are eatable (e.g. orange, pepper)
- removed "Jaffa Cakes" trademark, title of cakes can be changed in config file
- added milk
- hamburger, cheeseburge, toasts, chips
- changed brownies recipe to use kitchen knife instead of iron sword
- pastry without sugar (for rolls, buns etc.)
- added decoration blocks: column, jaffa statue
- fruits from trees are now harvastable by any item (not only bare hand)
- nerfed fruit spawn of trees, boosted a bit amount of seeds from plants

## 0.4.7
- added kitchen board
- added pizza
- added wolf armour set
- fixed cones and wafers (raw variants)

## 0.4.6
- updated to MC 1.4.6 (MCForge 471)
- added basin (sink)
- implemented meat cleaver

## 0.4.5
- new stuff divided into modules (each can be disabled in config)
- xmas module - christmas candies and presents
- ores - better way of crafting machines, adds two storage/decorative blocks

## 0.4.4
- updated to MC 1.4.5

## 0.4.4
- updated to MC 1.4.2
- rework of fruit trees (fruit is now picked by hand or special tools)
- plants are compatible with forestry (combine and farm machines)

## 0.4.3
- finished bushes
- fruit collector
- rewritten BC pipes support, now it should work with all mods (like RP2)
- machines accept build craft energy
- changed fridge consumption - on stack of coal now lasts aprox. 4 hours
- rewritten damaging items handling in craft
- added kettle, cup, knife (+ recipes using new "tools")

## 0.4.2
- fridge now don't consume buckets
- fridge supports buildcraft 3's pipes (fuel input is at bottom)

## 0.4.1
- fixed fridge's inventory to support shift-clicking
- fridge now tries harder to find ice-cream to freeze
- updated fruit trees textures

## 0.4.0
- added fruit trees
- added donuts
- new flavours of jaffa cakes
- hoodie

## 0.3.6
- fixed not-collectable items from jaffa bomb

## 0.3.5
- added ice-creams
- almost finished fridge
- moved start of default ID interval to avoid conflicts with BuilCraft 3 (3856-3913)

## 0.3.4
- added packs
- jaffa cakes bomb
- added buff to food

## 0.3.3
- more food (hotdog, chocolate bar)

## 0.3.2
- fixed new food items to be eatable
- fixed helper command
- new jar textures

## 0.3.1
- more mallets
- new pastry texture
- added brownies (1 hunger point), cream rolls (2 hunger points)

## 0.3
- complete rework of items handling
- cleaned code (removed unsued files, ...)
- tool for repeated usage (after while it breaks)

## 0.2
- initial version
- jaffa cakes crafting chain (sponge cake gives half hunger point, small one gives 1 hunger point, lager ones give 2 hunger points)
