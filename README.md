# Wilderlands
Wilderness survival game made for introduction to computer science at Amherst college.

To run, clone this repository and navigate to Wilderlands/src in the terminal and run:

```
javac *.java
java game
```

Controls:
Move the character with standard: 'w' to go up, 'a' to go left, 's' to go down, 'd' to go right\
```
Use 'e' to open the crafting menu\
Use 'q' to attack/break\
Use 'r' to open the inventory\
Use 1,2,3,4,5,6 to navigate the hotbar\
Move items around the inventory by clicking and placing. The bar to the left of the inventory is for armor.\
Eat food by clicking on the mouse while having food in your hotbar\
Place walls by navigating to them in the hotbar and clicking on the screen. Use the arrow keys to change orientation.\
Press 'esc' to go in and out of the menu. Use 's' to save while in the menu and 'l' to load.
```

Craft items by clicking on the item in the crafting menu. Crafting recipes are as follows:
```
Stone Sword: 1 wood, 2 stone
Gold Sword: 1 wood, 2 gold
Iron Sword: 1 wood, 2 iron
Emerald Sword: 1 wood, 2 emerald
Iron Helmet: 3 iron
Iron Chest Plate: 5 iron
Stone Axe: 2 wood, 3 stone
Copper Axe: 2 wood, 3 copper
Iron Axe: 2 wood, 3 iron
Ruby Axe: 2 wood, 3 ruby
Stone Wall: 3 stone
Wood Fence: 3 wood
Stone Pickaxe: 2 wood, 3 stone
Copper Pickaxe: 2 wood, 3 copper
Iron Pickaxe: 2 wood, 3 iron
Ruby Pickaxe: 2 wood, 3 ruby
Polar Bear Fur Cover: 1 polar bear hide, 1 emerald
Wolf Fur Cover: 1 wolf hide, 1 ruby
Fried Egg: 1 egg
Crown: 2 emerald, 5 gold, 1 ruby
Cake: 2 eggs, 2 milk
Lion Armor: 2 lion hide, 2 wool
Ruby Sword: 2 ruby, 2 bone, 1 emerald, 2 gold
Polar Bear Hat: 2 polar bear hide, 1 bone, 1 wool
```

Other notes:
Axes help you break trees, pickaxes help you break rocks, swords help you kill animals/enemies.
Walls will block both you and any animals/enemies. If you go up to the corner you can kind of phase through.
Upon death, you will lose all items and restart as spawn. It would be nice to have items drop upon death but we don't have it yet.
Keep track of location using the coordinates in the top left.
