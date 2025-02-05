import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
abstract public class Item {
    public String type;
    public String use;
    public Pair position;
    public Pair initPos;
    public Pair worldInitPos;
    public Pair delta;
    public BufferedImage img;
    public int width;
    public int height;
    public Item (Pair position) {
        this.initPos = new Pair(position.x, position.y);
        this.position = new Pair(position.x, position.y);
        this.worldInitPos = new Pair(World.position.x, World.position.y);
        this.delta = new Pair(0,0);
        this.delta.x = (int)(Math.random()*40);
        this.width = 20;
        this.height = 9;
        this.delta.y = (int)(Math.random()*Tile.height/2);
    }
    public Item(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public void draw(Graphics g) {
        g.drawImage(img, (int)(this.position.x - width), (int)(this.position.y - height), width*2, height*2, null, null);
    }
    public void update(World w) {
        this.position.x = initPos.x + this.delta.x + (World.position.x - worldInitPos.x);
        this.position.y = initPos.y + this.delta.y + (World.position.y - worldInitPos.y);
    }
}
class Tool extends Item {
    public int dmg;
    public BufferedImage alt;
    public Tool(int width, int height) {
        super(width, height);
    }
}
class Resource extends Item {
    public Resource(Pair position) {
        super(position);
        this.use = "resource";
    }
}
class Food extends Item {
    public int health;
    public Food(Pair position) {
        super(position);
        this.use = "food";
        this.health = 1;
        this.width = 15;
        this.height = 15;
    }
}
class Buildable extends Item {
    public Buildable(int width, int height) {
        super(width, height);
        this.use = "building";
    }
}
class Armor extends Item {
    public int prot;
    public BufferedImage alt;
    public Armor(int width, int height) {
        super(width, height);
    }
}
class Helmet extends Armor {
    public Helmet(int width, int height) {
        super(width, height);
        this.prot = 2;
        this.width = 11 * 2;
        this.height = (int) (19 * 2.2)*8/19;
        this.use = "helmet";
    }
}
class Neck extends Armor {
    public Neck(int width, int height) {
        super(width, height);
        this.width = 11 * 2;
        this.height = (int)(19 * 2.2)*4/19;
        this.use = "neck";
    }
}
class Chest extends Armor {
    public Chest(int width, int height) {
        super(width, height);
        this.prot = 2;
        this.width = 11 * 2;
        this.height = (int)(19 * 2.2)*6/19;
        this.use = "chest";
    }
}
/**
 * CODE FOR EACH ITEM BELOW
 */
class IronHelmet extends Helmet {
    public IronHelmet(int width, int height) {
        super(width, height);
        this.type = "ironhelmet";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronHelmet/IronHelmet.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronHelmet/IronHelmetAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Crown extends Helmet {
    public Crown(int width, int height) {
        super(width, height);
        this.prot = 6;
        this.type = "crown";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Crown/Crown.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Crown/CrownAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class PolarHelmet extends Helmet {
    public PolarHelmet(int width, int height) {
        super(width, height);
        this.prot = 4;
        this.type = "polarhelmet";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/PolarHelmet/PolarHelmet.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/PolarHelmet/PolarHelmetAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class IronChest extends Chest {
    public IronChest(int width, int height) {
        super(width, height);
        this.type = "ironchest";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronChest/IronChest.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronChest/IronChestAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class LionArmor extends Chest {
    public LionArmor(int width, int height) {
        super(width, height);
        this.prot = 4;
        this.type = "lionarmor";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/LionArmor/LionArmor.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/LionArmor/LionArmorAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class PolarCover extends Neck {
    public PolarCover(int width, int height) {
        super(width, height);
        this.prot = 4;
        this.type = "polarcover";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/PolarCover/PolarCover.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/PolarCover/PolarCoverAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class WolfCover extends Neck {
    public WolfCover(int width, int height) {
        super(width, height);
        this.prot = 2;
        this.type = "wolfcover";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/WolfCover/WolfCover.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/WolfCover/WolfCoverAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class StoneWall extends Buildable {
    public StoneWall(int width, int height) {
        super(width, height);
        this.type = "stonewall";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/StoneWall.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class WoodFence extends Buildable {
    public WoodFence(int width, int height) {
        super(width, height);
        this.type = "woodfence";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/WoodFence.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Sword extends Tool {
    public Sword(int width, int height) {
        super(width, height);
        this.use = "sword";
    }
}
class StoneSword extends Sword {
    public StoneSword(int width, int height) {
        super(width, height);
        this.type = "stonesword";
        this.dmg = 2;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/StoneSword/StoneSword.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/StoneSword/StoneSwordAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class IronSword extends Sword {
    public IronSword(int width, int height) {
        super(width, height);
        this.type = "ironsword";
        this.dmg = 4;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronSword/IronSword.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronSword/IronSwordAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class GoldSword extends Sword {
    public GoldSword(int width, int height) {
        super(width, height);
        this.type = "goldsword";
        this.dmg = 3;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/GoldSword/GoldSword.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/GoldSword/GoldSwordAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class EmeraldSword extends Sword {
    public EmeraldSword(int width, int height) {
        super(width, height);
        this.type = "emeraldsword";
        this.dmg = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/EmeraldSword/EmeraldSword.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/EmeraldSword/EmeraldSwordAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class RubySword extends Sword {
    public RubySword(int width, int height) {
        super(width, height);
        this.type = "rubysword";
        this.dmg = 8;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/RubySword/RubySword.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/RubySword/RubySwordAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Axe extends Tool {
    public Axe(int width, int height) {
        super(width, height);
        this.use = "axe";
    }
}
class StoneAxe extends Axe {
    public StoneAxe (int width, int height) {
        super(width, height);
        this.type = "stoneaxe";
        this.dmg = 2;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/StoneAxe/StoneAxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/StoneAxe/StoneAxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class IronAxe extends Axe {
    public IronAxe (int width, int height) {
        super(width, height);
        this.type = "ironaxe";
        this.dmg = 4;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronAxe/IronAxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronAxe/IronAxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class RubyAxe extends Axe {
    public RubyAxe (int width, int height) {
        super(width, height);
        this.type = "rubyaxe";
        this.dmg = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/RubyAxe/RubyAxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/RubyAxe/RubyAxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class CopperAxe extends Axe {
    public CopperAxe (int width, int height) {
        super(width, height);
        this.type = "copperaxe";
        this.dmg = 3;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/CopperAxe/CopperAxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/CopperAxe/CopperAxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Pickaxe extends Tool {
    public Pickaxe(int width, int height) {
        super(width, height);
        this.use = "pickaxe";
    }
}
class StonePickaxe extends Pickaxe {
    public StonePickaxe (int width, int height) {
        super(width, height);
        this.type = "stonepickaxe";
        this.dmg = 2;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/StonePickaxe/StonePickaxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/StonePickaxe/StonePickaxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class IronPickaxe extends Pickaxe {
    public IronPickaxe (int width, int height) {
        super(width, height);
        this.type = "ironpickaxe";
        this.dmg = 4;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronPickaxe/IronPickaxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/IronPickaxe/IronPickaxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class CopperPickaxe extends Pickaxe {
    public CopperPickaxe (int width, int height) {
        super(width, height);
        this.type = "copperpickaxe";
        this.dmg = 3;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/CopperPickaxe/CopperPickaxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/CopperPickaxe/CopperPickaxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class RubyPickaxe extends Pickaxe {
    public RubyPickaxe (int width, int height) {
        super(width, height);
        this.type = "stonepickaxe";
        this.dmg = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/RubyPickaxe/RubyPickaxe.png")));
            this.alt = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/RubyPickaxe/RubyPickaxeAlt.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Wood extends Resource {
    public Wood (Pair position) {
        super(position);
        this.type = "wood";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Wood.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Stone extends Resource {
    public Stone (Pair position) {
        super(position);
        this.type = "stone";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Stone.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Copper extends Resource {
    public Copper (Pair position) {
        super(position);
        this.height = 12;
        this.width = 12;
        this.type = "copper";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Copper.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Emerald extends Resource {
    public Emerald (Pair position) {
        super(position);
        this.height = 15;
        this.width = 12;
        this.type = "emerald";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Emerald.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Gold extends Resource {
    public Gold (Pair position) {
        super(position);
        this.height = 12;
        this.width = 12;
        this.type = "gold";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Gold.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Iron extends Resource {
    public Iron (Pair position) {
        super(position);
        this.height = 12;
        this.width = 12;
        this.type = "iron";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Iron.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Ruby extends Resource {
    public Ruby (Pair position) {
        super(position);
        this.height = 15;
        this.width = 12;
        this.type = "ruby";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Ruby.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Egg extends Resource {
    public Egg (Pair position) {
        super(position);
        this.height = 15;
        this.width = 15;
        this.type = "egg";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Egg.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Milk extends Resource {
    public Milk (Pair position) {
        super(position);
        this.height = 15;
        this.width = 15;
        this.type = "milk";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Milk.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class PolarHide extends Resource {
    public PolarHide (Pair position) {
        super(position);
        this.height = 20;
        this.width = 20;
        this.type = "polarhide";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/PolarHide.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class WolfHide extends Resource {
    public WolfHide (Pair position) {
        super(position);
        this.height = 20;
        this.width = 20;
        this.type = "wolfhide";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/WolfHide.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class LionHide extends Resource {
    public LionHide (Pair position) {
        super(position);
        this.height = 20;
        this.width = 20;
        this.type = "lionhide";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/LionHide.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Wool extends Resource {
    public Wool (Pair position) {
        super(position);
        this.height = 20;
        this.width = 20;
        this.type = "wool";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Wool.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Bone extends Resource {
    public Bone (Pair position) {
        super(position);
        this.height = 20;
        this.width = 20;
        this.type = "bone";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Bone.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Mushroom_Item extends Food {
    public Mushroom_Item (Pair position) {
        super(position);
        this.type = "mushroom";
        this.height = 20;
        this.width = 20;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/mushroom.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Beef extends Food {
    public Beef (Pair position) {
        super(position);
        this.type = "beef";
        this.health = 2;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Beef.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class FriedEgg extends Food {
    public FriedEgg (Pair position) {
        super(position);
        this.type = "friedegg";
        this.health = 2;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/FriedEgg.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Banana extends Food {
    public Banana (Pair position) {
        super(position);
        this.type = "banana";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Banana.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Cake extends Food {
    public Cake (Pair position) {
        super(position);
        this.type = "cake";
        this.health = 4;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Cake.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class ChickenWing extends Food {
    public ChickenWing (Pair position) {
        super(position);
        this.type = "chicken";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Chicken.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Lamb extends Food {
    public Lamb (Pair position) {
        super(position);
        this.type = "lamb";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Items/Lamb.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}