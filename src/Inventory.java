import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Inventory {
    static int barSlot = 1;
    static int invDim = 300;
    static int barWidth = 31*6;
    static int barHeight = 6*6;
    static int armorWidth = 31;
    static int armorHeight = 31*3;
    static Stack[][] inventory = new Stack[8][6];
    static BufferedImage inv;
    static BufferedImage bar;
    static BufferedImage armor;
    public Inventory() {
        CraftMenu.loadImages();
        try {
            inv = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Inventory.png")));
            bar = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Hotbar.png")));
            armor = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Character/Armor.png")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean contains(String type, int number) {
        boolean toReturn = false;
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[0].length; j++) {
                if (inventory[i][j] != null) {
                    if (inventory[i][j].type.equals(type) && inventory[i][j].size >= number) {
                        toReturn = true;
                    }
                }
            }
        }
        return toReturn;
    }
    public void removeItem(String type, int number) {
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[0].length; j++) {
                if (inventory[i][j] != null) {
                    if (inventory[i][j].type.equals(type)) {
                        if (inventory[i][j].size > number){
                            inventory[i][j].size -= number;
                        }
                        else {
                            inventory[i][j] = null;
                        }
                    }
                }
            }
        }
    }
    public void move(Stack s, int[] index) {
        if (inventory[index[0]][index[1]] != null) {
            Stack temp = inventory[index[0]][index[1]];
            temp.index = s.index;
            if (temp.index[0] == 0) {
                temp.location = "bar";
            }
            else if (temp.index[0] == 7) {
                temp.location = "armor";
            }
            else {
                temp.location = "inventory";
            }
            inventory[s.index[0]][s.index[1]] = temp;
        }
        else {
            inventory[s.index[0]][s.index[1]] = null;
        }
        if (index[0] == 0) {
            s.location = "bar";
        }
        else if (index[0] == 7) {
            s.location = "armor";
        }
        else {
            s.location = "inventory";
        }
        s.index = index;
        inventory[index[0]][index[1]] = s;
    }
    public void addItem(Item item) {
        boolean contained = false;
        boolean adding = false;
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[0].length; j++) {
                if (inventory[i][j] != null && inventory[i][j].type.equals(item.type) && !adding) {
                    inventory[i][j].size++;
                    contained = true;
                    adding = true;
                }
            }
        }
        if (!contained) {
            for (int i = 0; i < inventory.length; i++) {
                for (int j = 0; j < inventory[0].length; j++) {
                    //Adding items to the inventory
                    if (inventory[i][j] == null && !adding) {
                        if (i == 0) {
                            inventory[i][j] = new Stack(item, new int[]{0,j}, "bar");
                        }
                        else if (i == 7) {
                            inventory[i][j] = new Stack(item, new int[]{7,j}, "armor");
                        }
                        else {
                            inventory[i][j] = new Stack(item, new int[]{i,j}, "inventory");
                        }
                        adding = true;
                        inventory[i][j].size++;
                    }
                }
            }
        }
    }
    public void addItem(Item item, int[] index, int size) {
        if (index[0] == 0) {
            inventory[0][index[1]] = new Stack(item, new int[]{0,index[0]}, "bar");
        }
        else if (index[0] == 7) {
            inventory[index[0]][index[1]] = new Stack(item, new int[]{index[0],index[1]}, "armor");
        }
        else {
            inventory[index[0]][index[1]] = new Stack(item, new int[]{index[0],index[1]}, "inventory");
        }
        inventory[index[0]][index[1]].size = size;
    }
    public void draw(Graphics g) {
        //draw bar under player
        g.drawImage(bar, Game.width/2 - barWidth, Game.height - 50 - barHeight, barWidth*2, barHeight*2, null, null);
        //drawing whatever is in the bar
        if (inventory[0][barSlot-1] != null) {
            int dy = 0;
            int dx = 0;
            BufferedImage toDraw = inventory[0][barSlot-1].img;
            if (inventory[0][barSlot-1].use.equals("resource")) {
                dy = 2;
                dx = -20;
            }
            else if (inventory[0][barSlot-1].use.equals("axe") || inventory[0][barSlot-1].use.equals("sword") || inventory[0][barSlot-1].use.equals("pickaxe")) {
                dy = -16;
                if (Player.lastDir.equals("left")) {
                    dx = -15;
                    toDraw = inventory[0][barSlot-1].alt;
                }
                else {
                    dx = -22;
                }
            }
            else if (inventory[0][barSlot-1].use.equals("food")) {
                dy = -2;
                if (Player.lastDir.equals("left")) {
                    dx = 2;
                }
                else {
                    dx = -32;
                }
            }
            g.drawImage(toDraw, (int) Player.middle.x+dx, (int) Player.middle.y+dy, inventory[0][barSlot-1].width*2, inventory[0][barSlot-1].height*2, null, null);
        }
        //draw inventory if open
        if (Player.openInventory) {
            g.drawImage(inv, Game.width / 2 - invDim, Game.height / 2 - invDim, invDim * 2, invDim * 2, null, null);
            g.drawImage(armor, Game.width / 2 - invDim - armorWidth*3, Game.height / 2 - armorHeight, armorWidth * 2, armorHeight * 2, null, null);
        }
        //draw bar slot thing
        g.setColor(Color.white);
        g.drawRect(Game.width/2 - barWidth + (barSlot-1)*(int)((double)5/31*barWidth)*2, Game.height - 50 - barHeight, (int)((double)6/31*barWidth)*2, barHeight*2);
        //draw any items stored
        for (int i = 0; i < inventory.length; i++) {
            for (int j = 0; j < inventory[0].length; j++) {
                if (inventory[i][j] != null) {
                    if (Player.openInventory) {
                        inventory[i][j].draw(g);
                    }
                    if (inventory[0][j] != null) {
                        inventory[0][j].draw(g);
                    }
                }
            }
        }
        CraftMenu.draw(g);
    }
}
class Stack {
    public BufferedImage img;
    public BufferedImage alt;
    public int size;
    public String location;
    public int width;
    public int height;
    public int[] index;
    public String type;
    public String use;
    public int dmg;
    public int prot;
    public int health;
    public Stack (Item item, int[] index, String location) {
        this.location = location;
        this.img = item.img;
        this.width = item.width;
        this.height = item.height;
        this.type = item.type;
        this.index = index;
        this.use = item.use;
        if (this.use.equals("axe") || this.use.equals("sword") || this.use.equals("pickaxe")) {
            this.alt = ((Tool)item).alt;
            this.dmg = ((Tool)item).dmg;
        }
        else if (this.use.equals("helmet") || this.use.equals("chest") || this.use.equals("neck")) {
            this.alt = ((Armor)item).alt;
            this.prot = ((Armor)item).prot;
        }
        else if (this.use.equals("food")) {
            this.health = ((Food)item).health;
        }
    }
    public void draw(Graphics g) {
        if (location.equals("bar")) {
            g.drawImage(img, Game.width/2 - Inventory.barWidth + (int)((double)3/31 * Inventory.barWidth*2) - this.width + 2*(int)((double)5/31 * Inventory.barWidth) * index[1], Game.height - 52 -  Inventory.barHeight/6 - this.height, this.width * 2, this.height * 2, null, null);
            g.drawString(Integer.toString(this.size), Game.width/2 - Inventory.barWidth + (int)((double)3/31 * Inventory.barWidth*2) - 3 + 2*(int)((double)5/31 * Inventory.barWidth) * index[1], (int)(Game.height - Inventory.barHeight + 6));
        }
        if (location.equals("inventory")) {
            if (Player.openInventory) {
                g.drawImage(img, Game.width / 2 - Inventory.invDim + 2 * Inventory.invDim * 3 / 31 + index[1] * 2 * Inventory.invDim * 5 / 31 - this.width, Game.height / 2 - Inventory.invDim + 2 * Inventory.invDim * 3 / 31 + (index[0]-1) * 2 * Inventory.invDim * 5 / 31 - this.height, this.width * 2, this.height * 2, null, null);
                g.drawString(Integer.toString(this.size),Game.width / 2 - Inventory.invDim + 2 * Inventory.invDim * 3 / 31 + index[1] * 2 * Inventory.invDim * 5 / 31 - 3, Game.height / 2 - Inventory.invDim + 2 * Inventory.invDim * 3 / 31 + (index[0]-1) * 2 * Inventory.invDim * 5 / 31 + 26);
            }
        }
        if (location.equals("armor")) {
            int ah = (int)((double)1/3*Inventory.armorHeight);
            if (Player.openInventory) {
                g.drawImage(img, Game.width / 2 - Inventory.invDim - Inventory.armorWidth*3 + Inventory.armorWidth - this.width, Game.height/2 - Inventory.armorHeight + ah + ah * 2 * index[1] - this.height, this.width*2, this.height*2, null, null);
            }
        }
        if (location.equals("moving")) {
            g.drawImage(img, (int)Game.mousePos.x, (int)Game.mousePos.y, this.width*2, this.height*2, null, null);
        }
    }
}
class CraftMenu {
    static BufferedImage[][] images = new BufferedImage[6][6];
    static String[] references = new String[]{
            "Items/StoneSword/StoneSword.png", "Items/GoldSword/GoldSword.png", "Items/IronSword/IronSword.png", "Items/EmeraldSword/EmeraldSword.png", "Items/IronHelmet/IronHelmet.png", "Items/IronChest/IronChest.png",
            "Items/StoneAxe/StoneAxe.png", "Items/CopperAxe/CopperAxe.png", "Items/IronAxe/IronAxe.png", "Items/RubyAxe/RubyAxe.png", "Items/StoneWall.png", "Items/WoodFence.png",
            "Items/StonePickaxe/StonePickaxe.png", "Items/CopperPickaxe/CopperPickaxe.png", "Items/IronPickaxe/IronPickaxe.png", "Items/RubyPickaxe/RubyPickaxe.png", "Items/PolarCover/PolarCover.png", "Items/WolfCover/WolfCover.png",
            "Items/FriedEgg.png", "Items/Crown/Crown.png", "Items/Cake.png", "Items/LionArmor/LionArmor.png", "Items/RubySword/RubySword.png","Items/PolarHelmet/PolarHelmet.png"
    };
    static int[][] sizes = new int[][]{
            {20,20},{20,20},{20,20},{20,20},{20,15},{20,12},
            {20,20},{20,20},{20,20},{20,20},{20,20},{20,20},
            {20,20},{20,20},{20,20},{20,20},{20,8},{20,8},
            {20,20},{20,15},{20,20},{20,12},{20,20},{20,12}
    };
    public static void loadImages() {
        int p = 0;
        try {
            for (int i = 0; i < images.length; i++) {
                for (int j = 0; j < images.length; j++) {
                    if (p < references.length && references[p] != null) {
                        images[i][j] = ImageIO.read(Objects.requireNonNull(CraftMenu.class.getResourceAsStream(references[p])));
                        p++;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void draw(Graphics g) {
        if (Player.crafting) {
            g.drawImage(Inventory.inv, Game.width / 2 - Inventory.invDim, Game.height / 2 - Inventory.invDim, Inventory.invDim * 2, Inventory.invDim * 2, null, null);
            //300 x 300, 31 x 31 pixels
            int p = 0;
            for (int i = 0; i < images.length; i++) {
                for (int j = 0; j < images.length; j++) {
                    if (images[i][j] != null && p < sizes.length && sizes[p] != null) {
                        g.drawImage(images[i][j], Game.width / 2 - Inventory.invDim + 2 * Inventory.invDim * 3 / 31 + j * 2 * Inventory.invDim * 5 / 31 - sizes[p][0], Game.height / 2 - Inventory.invDim + 2 * Inventory.invDim * 3 / 31 + i * 2 * Inventory.invDim * 5 / 31 - sizes[p][1], sizes[p][0] * 2, sizes[p][1] * 2, null, null);
                        p++;
                    }
                }
            }
        }
    }
}
