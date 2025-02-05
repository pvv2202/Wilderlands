import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player {
    static double vel = 5;
    static int width = (int) (11 * 2.2); //11
    static int height = (int) (19 * 2.2); //19
    static double maxHealth = 10;
    static double health = 10;
    static int walk = 0;
    static boolean up = false;
    static boolean right = false;
    static boolean down = false;
    static boolean left = false;
    static boolean attack = false;
    static String placing = "right";
    static String blocked = "";
    static boolean openInventory = false;
    static boolean crafting = false;
    static boolean movingItem = false;
    static Inventory inventory = new Inventory();
    static Pair middle = new Pair((double) Game.width / 2, (double) Game.height / 2);
    int[] coords = new int[]{0, 0};
    BufferedImage[] img = new BufferedImage[10];
    BufferedImage lastImg;
    static String lastDir = "right";
    //storing the surrounding biomes
    static Biome currentBiome;
    static Tile currentTile;
    public Player() {
        try {
            img[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character1.png")));
            img[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character2.png")));
            img[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character3.png")));
            img[3] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character4.png")));
            img[4] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character5.png")));
            img[5] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character6.png")));
            img[6] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character7.png")));
            img[7] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Character8.png")));
            img[8] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Heart.png")));
            img[9] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/HalfHeart.png")));
            lastImg = img[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        if (lastDir.equals("left") && !attack) {
            if (walk >= 1 && walk < 3) {
                drawPlayer(g, img[4]);
            } else if (walk >= 3) {
                drawPlayer(g, img[5]);
                if (walk == 4) {
                    walk = 0;
                }
            } else {
                drawPlayer(g, img[3]);
            }
        } else if (lastDir.equals("right") && !attack) {
            if (walk >= 1 && walk < 3) {
                drawPlayer(g, img[1]);
            } else if (walk == 3 || walk == 4) {
                drawPlayer(g, img[2]);
                if (walk == 4) {
                    walk = 0;
                }
            } else {
                drawPlayer(g, img[0]);
            }
        }
        if (attack) {
            if (lastDir.equals("right")) {
                drawPlayer(g, img[6]);
            } else {
                drawPlayer(g, img[7]);
            }
        }
        //drawing armor
        //helmet
        if (Inventory.inventory[7][0] != null) {
            if (lastDir.equals("right")) {
                if (walk >= 3) {
                    g.drawImage(Inventory.inventory[7][0].img, Game.width / 2 - width, Game.height / 2 - height - Inventory.inventory[7][0].height + 16, Inventory.inventory[7][0].width * 2 + 1, Inventory.inventory[7][0].height * 2, null, null);
                }
                else {
                    g.drawImage(Inventory.inventory[7][0].img, Game.width / 2 - width, Game.height / 2 - height - Inventory.inventory[7][0].height + 12, Inventory.inventory[7][0].width * 2 - 1, Inventory.inventory[7][0].height * 2, null, null);
                }
            }
            else if (lastDir.equals("left")) {
                if (walk >= 3) {
                    g.drawImage(Inventory.inventory[7][0].alt, Game.width / 2 - width + 4, Game.height / 2 - height - Inventory.inventory[7][0].height + 16, Inventory.inventory[7][0].width * 2 + 1, Inventory.inventory[7][0].height * 2, null, null);
                }
                else {
                    g.drawImage(Inventory.inventory[7][0].alt, Game.width / 2 - width + 4, Game.height / 2 - height - Inventory.inventory[7][0].height + 12, Inventory.inventory[7][0].width * 2 - 1, Inventory.inventory[7][0].height * 2, null, null);
                }
            }
        }
        //neck armor
        if (Inventory.inventory[7][1] != null) {
            if (lastDir.equals("right")) {
                g.drawImage(Inventory.inventory[7][1].img, Game.width / 2 - width, Game.height / 2 + 4 - Inventory.inventory[7][1].height, Inventory.inventory[7][1].width * 2 + 4, Inventory.inventory[7][1].height * 2, null, null);
            }
            else if (lastDir.equals("left")) {
                g.drawImage(Inventory.inventory[7][1].alt, Game.width / 2 - width, Game.height / 2 + 4 - Inventory.inventory[7][1].height, Inventory.inventory[7][1].width * 2 + 4, Inventory.inventory[7][1].height * 2, null, null);
            }
        }
        //chest armor
        if (Inventory.inventory[7][2] != null) {
            if (lastDir.equals("right")) {
                g.drawImage(Inventory.inventory[7][2].img, Game.width / 2 - width, Game.height / 2 + 9 - Inventory.inventory[7][2].height, Inventory.inventory[7][2].width * 2 + 4, Inventory.inventory[7][2].height * 2, null, null);
            }
            else if (lastDir.equals("left")) {
                g.drawImage(Inventory.inventory[7][2].alt, Game.width / 2 - width, Game.height / 2 + 9 - Inventory.inventory[7][2].height, Inventory.inventory[7][2].width * 2 + 4, Inventory.inventory[7][2].height * 2, null, null);
            }
        }
        //Highlighting placement
        g.setColor(new Color(230,230,230,50));
        int w = 4;
        if (Inventory.inventory[0][Inventory.barSlot-1] != null && Inventory.inventory[0][Inventory.barSlot-1].use.equals("building")) {
            switch (placing) {
                case "right":
                    g.fillRect((int) currentTile.position.x + Tile.width - w, (int) currentTile.position.y - Tile.height, w * 2, Tile.height * 2);
                    break;
                case "left":
                    g.fillRect((int) currentTile.position.x - Tile.width + w, (int) currentTile.position.y - Tile.height, w * 2, Tile.height * 2);
                    break;
                case "down":
                    g.fillRect((int) currentTile.position.x - Tile.width, (int) currentTile.position.y + Tile.height - w, Tile.width * 2, w * 2);
                    break;
                case "up":
                    g.fillRect((int) currentTile.position.x - Tile.width, (int) currentTile.position.y - Tile.height + w, Tile.width * 2, w * 2);
                    break;
            }
        }
        //drawing hearts
        for (int i = 0; i < health/2; i++) {
            if (i + 1 <= health/2) {
                g.drawImage(img[8], Game.width/2 - 13*(int)maxHealth + 13*4*i, 60 - 11*2, 13*4, 11*4, null, null);
            }
            else {
                g.drawImage(img[9], Game.width/2 - 13*(int)maxHealth + 13*4*i, 60 - 11*2, 13*4, 11*4, null, null);
            }
        }
        inventory.draw(g);
    }
    public void drawPlayer(Graphics g, BufferedImage img) {
        g.drawImage(img, Game.width / 2 - width, Game.height / 2 - height, width * 2, height * 2, null, null);
    }
    public void update(World w) {
        if (up && !blocked.equals("up")) {
            World.position.y += vel;
        }
        if (right && !blocked.equals("right")) {
            lastImg = img[0];
            lastDir = "right";
            World.position.x -= vel;
        }
        if (down && !blocked.equals("down")) {
            World.position.y -= vel;
        }
        if (left && !blocked.equals("left")) {
            lastImg = img[3];
            lastDir = "left";
            World.position.x += vel;
        }
        //Store current biome the player is in
        for (Biome b : World.biomes) {
            if (w.detectCollision(middle, width, height, b.position, Biome.width, Biome.height)) {
                currentBiome = b;
                coords = b.coordinates;
            }
        }
        for (int i = 0; i < Biome.width / Tile.width; i++) {
            for (int j = 0; j < Biome.height / Tile.height; j++) {
                Tile t = currentBiome.tiles[i][j];
                if (w.detectCollision(middle, width, height, t.position, Tile.width, Tile.height)) {
                    currentTile = t;
                }
            }
        }
        //Player dying
        if (Player.health <= 0) {
            World.position = new Pair(0,0);
            Player.health = 10;
            for (int i = 0; i < Inventory.inventory.length; i++) {
                for (int j = 0; j < Inventory.inventory[0].length; j++) {
                    Inventory.inventory[i][j] = null;
                }
            }
        }
        /**
         * PLAYER INVENTORY
         */
        //variables for inventory
        double halfWidth = (double)4/31 * Inventory.invDim;
        double jumpWidth = (double)10/31 * Inventory.invDim;
        //variables for bar
        int bw = (int)((double)4/31*Inventory.barWidth);
        int bh = (int)((double)4/6*Inventory.barHeight);
        //variables for armor
        int ah = (int)((double)1/3*Inventory.armorHeight);
        //moving items in the inventory
        for (int i = 0; i < Inventory.inventory.length; i++) {
            for (int j = 0; j < Inventory.inventory[0].length; j++) {
                if (Inventory.inventory[i][j] != null) {
                    if (!movingItem) {
                        if (i == 0) {
                            //detecting if the player picks up an item from the bar
                            if (w.detectCollision(Game.mousePos, 0.001, 0.001, new Pair((int)(Game.width/2) - Inventory.barWidth + bw + (int)((double)2/31*Inventory.barWidth) + j * (int)((double)5/31*Inventory.barWidth)*2, (int)(Game.height-50-Inventory.barHeight) + (int)((double)1/3*Inventory.barHeight) + bh), bw, bh)) {
                                if (Game.mouseClicked && !Game.mouseClickProcessed) {
                                    Inventory.inventory[0][j].location = "moving";
                                    movingItem = true;
                                    Game.mouseClickProcessed = true;
                                }
                            }
                        }
                        else if (i == 7 && j < 3) {
                            //detecting if the player picks up an item from armor
                            if (w.detectCollision(Game.mousePos, 0.001, 0.001, new Pair((double) Game.width / 2 - Inventory.invDim - Inventory.armorWidth * 2, (double)Game.height / 2 - Inventory.armorHeight + ah*2 + ah*2*(j - 1)), Inventory.armorWidth, ah)) {
                                if (Game.mouseClicked && !Game.mouseClickProcessed) {
                                    Player.maxHealth -= Inventory.inventory[i][j].prot;
                                    if (Player.health > Player.maxHealth) {Player.health = Player.maxHealth;}
                                    Inventory.inventory[i][j].location = "moving";
                                    movingItem = true;
                                    Game.mouseClickProcessed = true;
                                }
                            }
                        }
                        else {
                            //detecting if the player picks up an item from the inventory
                            if (w.detectCollision(Game.mousePos, 0.001, 0.001, new Pair((double) Game.width / 2 - Inventory.invDim + 3 * halfWidth / 2 + jumpWidth * j, (double) Game.height / 2 - Inventory.invDim + 3 * halfWidth / 2 + jumpWidth * (i - 1)), halfWidth, halfWidth)) {
                                if (Game.mouseClicked && !Game.mouseClickProcessed) {
                                    Inventory.inventory[i][j].location = "moving";
                                    movingItem = true;
                                    Game.mouseClickProcessed = true;
                                }
                            }
                        }
                    }
                    //placing it in the correct index
                    if (Inventory.inventory[i][j].location.equals("moving")) {
                        for (int k = 0; k < Inventory.inventory.length; k++) {
                            for (int l = 0; l < Inventory.inventory[0].length; l++) {
                                //bar
                                if (k == 0 && w.detectCollision(Game.mousePos, 0.001, 0.001, new Pair((int)(Game.width/2 - Inventory.barWidth + bw + (int)((double)2/31*Inventory.barWidth) + l * (int)((double)5/31*Inventory.barWidth)*2), (int)(Game.height-50-Inventory.barHeight) + (int)((double)1/3*Inventory.barHeight) + bh), bw, bh)) {
                                    if (Game.mouseClicked && !Game.mouseClickProcessed) {
                                        Inventory.inventory[i][j].location = "bar";
                                        inventory.move(Inventory.inventory[i][j], new int[]{0, l});
                                        movingItem = false;
                                        Game.mouseClickProcessed = true;
                                    }
                                }
                                //armor
                                else if (k == 7 && l < 3 && w.detectCollision(Game.mousePos, 0.001, 0.001, new Pair((double) Game.width / 2 - Inventory.invDim - Inventory.armorWidth * 2, (double)Game.height / 2 - Inventory.armorHeight + ah*2 + ah*2*(l - 1)), Inventory.armorWidth, ah)) {
                                    if (Game.mouseClicked && !Game.mouseClickProcessed && ((Inventory.inventory[i][j].use.equals("helmet") && l == 0) || (Inventory.inventory[i][j].use.equals("chest") && l == 2) || (Inventory.inventory[i][j].use.equals("neck") && l == 1))) {
                                        Player.maxHealth += Inventory.inventory[i][j].prot;
                                        Player.health += Inventory.inventory[i][j].prot;
                                        Inventory.inventory[i][j].location = "armor";
                                        inventory.move(Inventory.inventory[i][j], new int[]{7, l});
                                        movingItem = false;
                                        Game.mouseClickProcessed = true;
                                    }
                                }
                                //inventory
                                else if (w.detectCollision(Game.mousePos, 0.001, 0.001, new Pair((double) Game.width / 2 - Inventory.invDim + 3 * halfWidth / 2 + jumpWidth * l, (double) Game.height / 2 - Inventory.invDim + 3 * halfWidth/2 + jumpWidth * (k-1)), halfWidth, halfWidth) && openInventory) {
                                    if (Game.mouseClicked && !Game.mouseClickProcessed) {
                                        Inventory.inventory[i][j].location = "inventory";
                                        inventory.move(Inventory.inventory[i][j], new int[]{k, l});
                                        movingItem = false;
                                        Game.mouseClickProcessed = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /**
         * PICKING UP ITEMS
         */
        for (int i = 0; i < World.items.size(); i++) {
            Item t = World.items.get(i);
            if (w.detectCollision(middle, width, height, t.position, t.width, t.height)) {
                inventory.addItem(t);
                World.items.remove(t);
            }
        }
        /**
         * EATING
         */
        if (Inventory.inventory[0][Inventory.barSlot-1] != null && Inventory.inventory[0][Inventory.barSlot-1].use.equals("food") && !Player.openInventory && !Player.crafting && health < maxHealth) {
            if (Game.mouseClicked && !Game.mouseClickProcessed) {
                if (health + Inventory.inventory[0][Inventory.barSlot-1].health <= maxHealth) {
                    health += Inventory.inventory[0][Inventory.barSlot-1].health;
                }
                else {
                    health = maxHealth;
                }
                if (Inventory.inventory[0][Inventory.barSlot-1].size == 1) {
                    Inventory.inventory[0][Inventory.barSlot-1] = null;
                }
                else {
                    Inventory.inventory[0][Inventory.barSlot-1].size --;
                }
                Game.mouseClickProcessed = true;
            }
        }
        /**
         * BUILDING COLLISION
         */
        boolean collision = false;
        for (Building b : World.buildings) {
            if (w.detectCollision(middle, width, height, b.position, b.width, b.height)) {
                if (b.orientation.equals("right") || b.orientation.equals("left")) {
                    if (middle.x < b.position.x) {
                        blocked = "right";
                    }
                    else if (middle.x > b.position.x) {
                        blocked = "left";
                    }
                }
                else if (b.orientation.equals("up") || b.orientation.equals("down")){
                    if (middle.y < b.position.y) {
                        blocked = "down";
                    }
                    else if (middle.y > b.position.y) {
                        blocked = "up";
                    }
                }
                collision = true;
            }
        }
        if (!collision) {
            blocked = "";
        }
        /**
         * CRAFTING
         */
        if (crafting) {
            for (int i = 0; i < CraftMenu.images.length; i++) {
                for (int j = 0; j < CraftMenu.images.length; j++) {
                    if (w.detectCollision(Game.mousePos, 0.001, 0.001, new Pair((double) Game.width / 2 - Inventory.invDim + 3 * halfWidth / 2 + jumpWidth * j, (double) Game.height / 2 - Inventory.invDim + 3 * halfWidth / 2 + jumpWidth * i), halfWidth, halfWidth) && Game.mouseClicked && !Game.mouseClickProcessed) {
                        switch (i) {
                            //add items based on i, j position
                            case 0:
                                switch (j) {
                                    case 0:
                                        if (inventory.contains("wood", 1) && inventory.contains("stone", 2)) {
                                            inventory.addItem(new StoneSword(20, 20));
                                            inventory.removeItem("wood", 1);
                                            inventory.removeItem("stone", 2);
                                        } break;
                                    case 1:
                                        if (inventory.contains("wood", 1) && inventory.contains("gold", 2)) {
                                            inventory.addItem(new GoldSword(20, 20));
                                            inventory.removeItem("wood", 1);
                                            inventory.removeItem("gold", 2);
                                        } break;
                                    case 2:
                                        if (inventory.contains("wood", 1) && inventory.contains("iron", 2)) {
                                            inventory.addItem(new IronSword(20, 20));
                                            inventory.removeItem("wood", 1);
                                            inventory.removeItem("iron", 2);
                                        } break;
                                    case 3:
                                        if (inventory.contains("wood", 1) && inventory.contains("emerald", 2)) {
                                            inventory.addItem(new EmeraldSword(20, 20));
                                            inventory.removeItem("wood", 1);
                                            inventory.removeItem("emerald", 2);
                                        } break;
                                    case 4:
                                        if (inventory.contains("iron", 3)) {
                                            inventory.addItem(new IronHelmet(20,20));
                                            inventory.removeItem("iron", 3);
                                        } break;
                                    case 5:
                                        if (inventory.contains("iron", 5)) {
                                            inventory.addItem(new IronChest(20, 20));
                                            inventory.removeItem("iron", 5);
                                        } break;
                                }
                                break;
                            case 1:
                                switch (j) {
                                    case 0:
                                        if (inventory.contains("wood", 2) && inventory.contains("stone", 3)) {
                                            inventory.addItem(new StoneAxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("stone", 3);
                                        } break;
                                    case 1:
                                        if (inventory.contains("wood", 2) && inventory.contains("copper", 3)) {
                                            inventory.addItem(new CopperAxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("copper", 3);
                                        } break;
                                    case 2:
                                        if (inventory.contains("wood", 2) && inventory.contains("iron", 3)) {
                                            inventory.addItem(new IronAxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("iron", 3);
                                        } break;
                                    case 3:
                                        if (inventory.contains("wood", 2) && inventory.contains("ruby", 3)) {
                                            inventory.addItem(new RubyAxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("ruby", 3);
                                        } break;
                                    case 4:
                                        if (inventory.contains("stone", 3)) {
                                            inventory.addItem(new StoneWall(20,20));
                                            inventory.removeItem("stone", 3);
                                        } break;
                                    case 5:
                                        if (inventory.contains("wood", 3)) {
                                            inventory.addItem(new WoodFence(20,20));
                                            inventory.removeItem("wood", 3);
                                        } break;
                                }
                                break;
                            case 2:
                                switch(j) {
                                    case 0:
                                        if (inventory.contains("wood", 2) && inventory.contains("stone", 3)) {
                                            inventory.addItem(new StonePickaxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("stone", 3);
                                        } break;
                                    case 1:
                                        if (inventory.contains("wood", 2) && inventory.contains("copper", 3)) {
                                            inventory.addItem(new CopperPickaxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("copper", 3);
                                        } break;
                                    case 2:
                                        if (inventory.contains("wood", 2) && inventory.contains("iron", 3)) {
                                            inventory.addItem(new IronPickaxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("iron", 3);
                                        } break;
                                    case 3:
                                        if (inventory.contains("wood", 2) && inventory.contains("ruby", 3)) {
                                            inventory.addItem(new RubyPickaxe(20, 20));
                                            inventory.removeItem("wood", 2);
                                            inventory.removeItem("ruby", 3);
                                        } break;
                                    case 4:
                                        if (inventory.contains("polarhide", 1) && inventory.contains("emerald", 1)) {
                                            inventory.addItem(new PolarCover(20,20));
                                            inventory.removeItem("polarhide", 1);
                                            inventory.removeItem("emerald", 1);
                                        } break;
                                    case 5:
                                        if (inventory.contains("wolfhide", 1) && inventory.contains("ruby", 1)) {
                                            inventory.addItem(new WolfCover(20,20));
                                            inventory.removeItem("wolfhide", 1);
                                            inventory.removeItem("ruby", 1);
                                        } break;
                                }
                            case 3:
                                switch(j) {
                                    case 0:
                                        if (inventory.contains("egg", 1)) {
                                            inventory.addItem(new FriedEgg(new Pair(20,20)));
                                            inventory.removeItem("egg", 1);
                                        } break;
                                    case 1:
                                        if (inventory.contains("emerald", 2) && inventory.contains("gold", 5) && inventory.contains("ruby", 1)) {
                                            inventory.addItem(new Crown(20,20));
                                            inventory.removeItem("emerald", 2);
                                            inventory.removeItem("gold", 5);
                                            inventory.removeItem("ruby", 1);
                                        } break;
                                    case 2:
                                        if (inventory.contains("egg", 2) && inventory.contains("milk", 2)) {
                                            inventory.addItem(new Cake(new Pair(20,20)));
                                            inventory.removeItem("egg", 2);
                                            inventory.removeItem("milk", 2);
                                        } break;
                                    case 3:
                                        if (inventory.contains("lionhide", 2) && inventory.contains("wool", 2)) {
                                            inventory.addItem(new LionArmor(20,20));
                                            inventory.removeItem("lionhide", 2);
                                            inventory.removeItem("wool", 2);
                                        } break;
                                    case 4:
                                        if (inventory.contains("ruby", 2) && inventory.contains("bone", 2) && inventory.contains("emerald", 1) && inventory.contains("gold", 2)) {
                                            inventory.addItem(new RubySword(20,20));
                                            inventory.removeItem("ruby", 2);
                                            inventory.removeItem("bone", 2);
                                            inventory.removeItem("emerald", 1);
                                            inventory.removeItem("gold", 2);
                                        } break;
                                    case 5:
                                        if (inventory.contains("polarhide", 2) && inventory.contains("bone", 1) && inventory.contains("wool", 1)) {
                                            inventory.addItem(new PolarHelmet(20,20));
                                            inventory.removeItem("polarhide", 2);
                                            inventory.removeItem("bone", 1);
                                            inventory.removeItem("wool", 1);
                                        } break;
                                }
                        }
                        Game.mouseClickProcessed = true;
                    }
                }
            }
        }
        /**
         * PLACING THINGS
         */
        if (Inventory.inventory[0][Inventory.barSlot-1] != null && Inventory.inventory[0][Inventory.barSlot-1].use.equals("building")) {
            if (Game.mouseClicked && !Game.mouseClickProcessed) {
                switch (Inventory.inventory[0][Inventory.barSlot-1].type) {
                    case("stonewall"): World.buildings.add(new Stone_Wall(currentTile.position, Player.placing)); break;
                    case("woodfence"): World.buildings.add(new Wood_Fence(currentTile.position, Player.placing)); break;
                }
                inventory.removeItem(Inventory.inventory[0][Inventory.barSlot-1].type, 1);
                Game.mouseClickProcessed = true;
            }
        }
        /**
         * BREAKING/ATTACKING
         */
        if (attack) {
            for (int i = 0; i < Biome.width / Tile.width; i++) {
                for (int j = 0; j < Biome.height / Tile.height; j++) {
                    if (currentBiome.tiles[i][j].entity != null && (w.detectCollision(middle, width, height, currentBiome.tiles[i][j].entity.position, currentBiome.tiles[i][j].entity.width, currentBiome.tiles[i][j].entity.height) && !Entity.attacked)) {
                        if (Inventory.inventory[0][Inventory.barSlot-1] != null && Inventory.inventory[0][Inventory.barSlot-1].use.equals("axe") && currentBiome.tiles[i][j].entity.type.equals("tree")) {
                            currentBiome.tiles[i][j].entity.health -= Inventory.inventory[0][Inventory.barSlot-1].dmg;
                        }
                        else if (Inventory.inventory[0][Inventory.barSlot-1] != null && Inventory.inventory[0][Inventory.barSlot-1].use.equals("pickaxe") && currentBiome.tiles[i][j].entity.type.equals("rock")) {
                            currentBiome.tiles[i][j].entity.health -= Inventory.inventory[0][Inventory.barSlot-1].dmg;
                        }
                        else {
                            currentBiome.tiles[i][j].entity.health -= 1;
                        }
                        Entity.attacked = true;
                        if (currentBiome.tiles[i][j].entity.health <= 0) {
                            currentBiome.tiles[i][j].entity.drop();
                            currentBiome.tiles[i][j].entity = null;
                        }
                    }
                }
            }
            for (int i = 0; i < World.animals.size(); i++) {
                if (w.detectCollision(middle, width, height, World.animals.get(i).position, World.animals.get(i).width, World.animals.get(i).height) && !Entity.attacked) {
                    if ((Inventory.inventory[0][Inventory.barSlot-1] != null && Inventory.inventory[0][Inventory.barSlot-1].use.equals("sword"))) {
                        World.animals.get(i).health -= Inventory.inventory[0][Inventory.barSlot-1].dmg;
                    }
                    else {
                        World.animals.get(i).health -= 1;
                    }
                    Entity.attacked = true;
                }
                if (World.animals.get(i).health <= 0) {
                    World.animals.get(i).drop();
                    World.animals.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < World.buildings.size(); i++) {
                if (w.detectCollision(middle, width, height, World.buildings.get(i).position, World.buildings.get(i).width, World.buildings.get(i).height) && !Entity.attacked) {
                    World.buildings.get(i).health -= 1;
                    Entity.attacked = true;
                }
                if (World.buildings.get(i).health <= 0) {
                    World.buildings.get(i).drop();
                    World.buildings.remove(i);
                    i--;
                }
            }
        }
        /**
         * BIOME GENERATION
         */
        //Loop around the current coordinates to check if the surroundings have been rendered
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int[] n = new int[]{coords[0] + i, coords[1] + j};
                if (World.rendered.stream().noneMatch(a -> a[0] == n[0] && a[1] == n[1])) {
                    World.biomes.add(w.randomBiome((double) Game.width / 2 + (double) Biome.width * 2 * n[0], (double) Game.height / 2 + (double) Biome.height * 2 * n[1], n[0], n[1]));
                    World.rendered.add(n);
                }
            }
        }
    }
}