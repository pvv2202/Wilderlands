import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
abstract public class Entity {
    public Pair position;
    public Pair tilePos;
    public Pair delta;
    public int width;
    public int height;
    public int health;
    public String type;
    public static boolean attacked = false;
    public BufferedImage img;
    public Entity(Pair tilePos) {
        this.tilePos = tilePos;
        this.position = new Pair(tilePos.x,tilePos.y);
        this.width = Tile.width;
        this.height = Tile.height;
        this.delta = new Pair(0,0);
    }
    abstract public void drop();
    public void update(double time, World w) {
        this.position = this.tilePos.add(this.delta);
    }
    public void draw(Graphics g) {
        g.drawImage(this.img, (int)this.position.x-this.width, (int)this.position.y-this.height, this.width*2, this.height*2, null, null);
    }
}
class Building extends Entity {
    public String type;
    public String orientation;
    public Building(Pair tilePos, String orientation) {
        super(tilePos);
        switch (orientation) {
            case "right": this.position.x = this.tilePos.x + Tile.width; this.width = 20; this.height = Tile.height; break;
            case "left": this.position.x = this.tilePos.x - Tile.width; this.width = 20; this.height = Tile.height; break;
            case "down": this.position.y = this.tilePos.y + Tile.height; this.width = Tile.width; this.height = 20; break;
            case "up": this.position.y = this.tilePos.y - Tile.height; this.width = Tile.width; this.height = 20; break;
        }
        this.orientation = orientation;
        this.delta = new Pair(this.position.x - this.tilePos.x, this.position.y - this.tilePos.y);
        this.health = 5;
    }
    public void drop() {}
}
class Animal extends Entity {
    //Assign img to right facing
    public BufferedImage left;
    public BufferedImage img2;
    public BufferedImage left2;
    public Pair vel;
    public int mv;
    public int a;
    public String blocked = "";
    public Animal(Pair tilePos) {
        super(tilePos);
        this.mv = 0;
        this.vel = new Pair(Math.random()*2-1, Math.random()*2-1);
        this.delta = new Pair(0,0);
    }
    public void drop() {}
    //Want them tied to move according to the world's movements but also move within the world
    @Override
    public void update(double time, World w) {
        if (mv > 100) {
            this.vel = new Pair(Math.random()*2-1, Math.random()*2-1);
            this.mv = 0;
        }
        this.position.x = this.tilePos.x + this.delta.x;
        this.position.y = this.tilePos.y + this.delta.y;
        this.delta = this.delta.add(vel);
        boolean collision = false;
        for (Building b : World.buildings) {
            if (w.detectCollision(this.position, this.width, this.height, b.position, b.width, b.height)) {
                if (b.orientation.equals("right") || b.orientation.equals("left")) {
                    if (this.position.x < b.position.x) {
                        this.blocked = "right";
                    }
                    else if (this.position.x > b.position.x) {
                        this.blocked = "left";
                    }
                }
                else if (b.orientation.equals("up") || b.orientation.equals("down")){
                    if (this.position.y < b.position.y) {
                        this.blocked = "down";
                    }
                    else if (this.position.y > b.position.y) {
                        this.blocked = "up";
                    }
                }
                collision = true;
            }
        }
        if (!collision) {
            blocked = "";
        }
        switch (this.blocked) {
            case("right"): this.delta.x -= this.width; this.vel.x = -this.vel.x; break;
            case ("left"): this.delta.x += this.width; this.vel.x = -this.vel.x; break;
            case ("up"): this.delta.y += this.height; this.vel.y = -this.vel.y; break;
            case ("down"): this.delta.y -= this.height; this.vel.y = -this.vel.y; break;
        }
        mv++;
        a++;
        if (a > 20) {
            a = 0;
        }
    }
    @Override
    public void draw(Graphics g) {
        if (this.vel.x <= 0) {
            if (a <= 10) {
                g.drawImage(this.left, (int) this.position.x - this.width, (int) this.position.y - this.height, this.width * 2, this.height * 2, null, null);
            }
            else {
                g.drawImage(this.left2, (int) this.position.x - this.width, (int) this.position.y - this.height, this.width * 2, this.height * 2, null, null);
            }
        }
        else {
            if (a <= 10) {
                g.drawImage(this.img, (int) this.position.x - this.width, (int) this.position.y - this.height, this.width * 2, this.height * 2, null, null);
            }
            else {
                g.drawImage(this.img2, (int) this.position.x - this.width, (int) this.position.y - this.height, this.width * 2, this.height * 2, null, null);
            }
        }
    }
}
class Enemy extends Animal {
    public boolean attacking;
    public int t;
    public Enemy(Pair tilePos) {
        super(tilePos);
        t = 0;
    }
    public void drop() {
        double n = Math.random();
        if (n < 0.2) {
            World.items.add(new Bone(this.position));
        }
    }
    //want them tied to move according to the world's movements but also move within the world
    @Override
    public void update(double time, World w) {
        int spd;
        this.attacking = false;
        if (Player.middle.x > this.position.x) {
            spd = 2;
        }
        else {
            spd = -2;
        }
        //factor to multiply vel by
        if (w.detectCollision(Player.middle, Player.width, Player.height, this.position, this.width*10, this.width*10) && this.blocked.equals("")) {
            double theta = Math.atan((this.position.y - Player.middle.y)/(this.position.x - Player.middle.x));
            this.vel.x = spd*Math.cos(theta);
            this.vel.y = spd*Math.sin(theta);
        }
        else {
            if (mv > 100) {
                this.vel = new Pair(Math.random() * 2 - 1, Math.random() * 2 - 1);
                this.mv = 0;
            }
        }
        this.position = new Pair(this.tilePos.x + this.delta.x, this.tilePos.y + this.delta.y);
        this.delta = this.delta.add(vel);
        boolean collision = false;
        for (Building b : World.buildings) {
            if (w.detectCollision(this.position, this.width, this.height, b.position, b.width, b.height)) {
                if (b.orientation.equals("right") || b.orientation.equals("left")) {
                    if (this.position.x < b.position.x) {
                        this.blocked = "right";
                    }
                    else if (this.position.x > b.position.x) {
                        this.blocked = "left";
                    }
                }
                else if (b.orientation.equals("up") || b.orientation.equals("down")){
                    if (this.position.y < b.position.y) {
                        this.blocked = "down";
                    }
                    else if (this.position.y > b.position.y) {
                        this.blocked = "up";
                    }
                }
                collision = true;
            }
        }
        if (!collision) {
            blocked = "";
        }
        switch (this.blocked) {
            case("right"): this.delta.x -= this.width; this.vel.x = -this.vel.x; break;
            case ("left"): this.delta.x += this.width; this.vel.x = -this.vel.x; break;
            case ("up"): this.delta.y += this.height; this.vel.y = -this.vel.y; break;
            case ("down"): this.delta.y -= this.height; this.vel.y = -this.vel.y; break;
        }
        mv++;
        a++;
        if (a > 20) {
            a = 0;
        }
        if (w.detectCollision(Player.middle, width, height, this.position, this.width-5, this.height) && !this.attacking) {
            if (t == 15) {
                Player.health -= 1;
                t = 0;
            }
            t++;
            this.attacking = true;
        }
    }
}
/**
 * BUILDINGS
 */
class Stone_Wall extends Building {
    public Stone_Wall (Pair tilePos, String orientation) {
        super(tilePos, orientation);
        this.type = "stonewall";
        try {
            if (orientation.equals("right") || orientation.equals("left")) {
                this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/StoneWall/StoneWallVertical.png")));
            }
            else {
                this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/StoneWall/StoneWallHorizontal.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop(){}
}
class Wood_Fence extends Building {
    public Wood_Fence (Pair tilePos, String orientation) {
        super(tilePos, orientation);
        this.type = "woodfence";
        try {
            if (orientation.equals("right") || orientation.equals("left")) {
                this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/WoodFence/WoodFenceVertical.png")));
            }
            else {
                this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/WoodFence/WoodFenceHorizontal.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop(){}
}
/**
 * ROCKS
 */
class Rock extends Entity {
    public Rock(Pair tilePos) {
        super(tilePos);
        this.type = "rock";
        this.health = 10;
        this.width = 40;
        this.height = 20;
        this.delta = new Pair(Math.random()*Tile.width-(double)this.width/2,Math.random()*Tile.height-(double)Tile.height/2);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Rock.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n = (int)(Math.random()*3) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Stone(this.position));
        }
    }
}
class CopperRock extends Rock {
    public CopperRock(Pair tilePos) {
        super(tilePos);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/CopperRock.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new Stone(this.position));
        }
        int n2 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n2; i++) {
            World.items.add(new Copper(this.position));
        }
    }
}
class EmeraldRock extends Rock {
    public EmeraldRock(Pair tilePos) {
        super(tilePos);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/EmeraldRock.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new Stone(this.position));
        }
        int n2 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n2; i++) {
            World.items.add(new Emerald(this.position));
        }
    }
}
class GoldRock extends Rock {
    public GoldRock(Pair tilePos) {
        super(tilePos);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/GoldRock.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new Stone(this.position));
        }
        int n2 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n2; i++) {
            World.items.add(new Gold(this.position));
        }
    }
}
class RubyRock extends Rock {
    public RubyRock(Pair tilePos) {
        super(tilePos);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/RubyRock.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new Stone(this.position));
        }
        int n2 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n2; i++) {
            World.items.add(new Ruby(this.position));
        }
    }
}
class IronRock extends Rock {
    public IronRock(Pair tilePos) {
        super(tilePos);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/IronRock.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new Stone(this.position));
        }
        int n2 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n2; i++) {
            World.items.add(new Iron(this.position));
        }
    }
}
/**
 * TREES
 */
class Tree extends Entity {
    public Tree(Pair tilePos) {
        super(tilePos);
        this.health = 5;
        this.type = "tree";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Tree.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n = (int)(Math.random()*3) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Wood(this.position));
        }
    }
}
class Forest_Tree extends Tree {
    public Forest_Tree(Pair tilePos) {
        super(tilePos);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Forest_Tree.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Banana_Tree extends Tree {
    public Banana_Tree(Pair tilePos) {
        super(tilePos);
        this.health = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Banana_Tree.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n = (int)(Math.random()*3) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Banana(this.position));
        }
    }
}
class Cactus extends Entity {
    public Cactus(Pair tilePos) {
        super(tilePos);
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Cactus.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n = (int)(Math.random()*3) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Wood(this.position));
        }
    }
}
class Rain_Tree extends Tree {
    public Rain_Tree(Pair tilePos) {
        super(tilePos);
        this.health = 8;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Rain_Tree.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Snow_Tree extends Tree {
    public Snow_Tree(Pair tilePos) {
        super(tilePos);
        this.health = 8;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Snow_Tree.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Eucalyptus_Tree extends Tree {
    public Eucalyptus_Tree(Pair tilePos) {
        super(tilePos);
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Eucalyptus_Tree.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Mushroom extends Entity {
    public Mushroom (Pair tilePos) {
        super(tilePos);
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/mushroom.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drop() {
        int n = (int)(Math.random()*3) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Mushroom_Item(this.position));
        }
    }
}
/**
 * PASSIVE ANIMALS
 */
class Cow extends Animal {
    public Cow (Pair tilePos) {
        super(tilePos);
        this.width = 14*3;
        this.height = 14*3;
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Cow/Cow.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Cow/CowLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Cow/Cow2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Cow/CowLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Beef(this.position));
        }
        World.items.add(new Milk(this.position));
    }
}
class Camel extends Animal {
    public Camel (Pair tilePos) {
        super(tilePos);
        this.width = 14*4;
        this.height = 14*3;
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Camel/Camel.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Camel/CamelLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Camel/Camel2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Camel/CamelLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Beef(this.position));
        }
    }
}
class Goat extends Animal {
    public Goat (Pair tilePos) {
        super(tilePos);
        this.width = 14*3;
        this.height = 14*2;
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Goat/Goat.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Goat/GoatLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Goat/Goat2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Goat/GoatLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Milk(this.position));
        }
    }
}
class Sheep extends Animal {
    public Sheep (Pair tilePos) {
        super(tilePos);
        this.width = 42;
        this.height = 30;
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Sheep/Sheep.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Sheep/SheepLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Sheep/Sheep2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Sheep/SheepLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new Lamb(this.position));
        }
        int n2 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n2; i++) {
            World.items.add(new Wool(this.position));
        }
    }
}
class Capybara extends Animal {
    public Capybara (Pair tilePos) {
        super(tilePos);
        this.width = 25;
        this.height = 20;
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Capybara/Capybara.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Capybara/CapybaraLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Capybara/Capybara.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Capybara/CapybaraLeft.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(double time, World w) {this.position = this.tilePos.add(this.delta);}
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Beef(this.position));
        }
    }
}
class Koala extends Animal {
    public Koala (Pair tilePos) {
        super(tilePos);
        this.width = 20;
        this.height = 20;
        this.health = 10;
        this.delta = new Pair(Math.random()*Tile.height/2 - (double)Tile.height/4, Math.random()*Tile.width - (double)Tile.width/2);
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Koala/Koala.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Koala/KoalaLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Koala/Koala.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Koala/KoalaLeft.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(double time, World w) {this.position = this.tilePos.add(this.delta);}
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Beef(this.position));
        }
    }
}
class Chicken extends Animal {
    public Chicken (Pair tilePos) {
        super(tilePos);
        this.width = 7*3;
        this.height = 7*3;
        this.health = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Chicken/Chicken.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Chicken/ChickenLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Chicken/Chicken2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Chicken/ChickenLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new ChickenWing(this.position));
        }
        int n2 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n2; i++) {
            World.items.add(new Egg(this.position));
        }
    }
}
class Penguin extends Animal {
    public Penguin (Pair tilePos) {
        super(tilePos);
        this.width = 23;
        this.height = 33;
        this.health = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Penguin/Penguin.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Penguin/PenguinLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Penguin/Penguin2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Penguin/PenguinLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
    }
}
class Rabbit extends Animal {
    public Rabbit (Pair tilePos) {
        super(tilePos);
        this.width = 18;
        this.height = 18;
        this.health = 3;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Rabbit/Rabbit.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Rabbit/RabbitLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Rabbit/Rabbit2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Rabbit/RabbitLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
    }
}
class Wombat extends Animal {
    public Wombat (Pair tilePos) {
        super(tilePos);
        this.width = 25;
        this.height = 20;
        this.health = 3;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wombat/Wombat.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wombat/WombatLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wombat/Wombat2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wombat/WombatLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
    }
}
/**
 * HOSTILE ANIMALS
 */
class Monster extends Enemy {
    public Monster (Pair tilePos) {
        super(tilePos);
        this.width = 30;
        this.height = 30;
        this.health = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Monster/Monster.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Monster/MonsterLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Monster/Monster2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Monster/MonsterLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        int n = (int)(Math.random()*3) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Wood(this.position));
        }
    }
}
class Ape extends Enemy {
    public Ape (Pair tilePos) {
        super(tilePos);
        this.width = 60;
        this.height = 60;
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Ape/Ape.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Ape/ApeLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Ape/Ape2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/APe/ApeLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*4) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Beef(this.position));
        }
    }
}
class Kangaroo extends Enemy {
    public Kangaroo (Pair tilePos) {
        super(tilePos);
        this.width = 50;
        this.height = 50;
        this.health = 8;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Kangaroo/Kangaroo.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Kangaroo/KangarooLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Kangaroo/Kangaroo2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Kangaroo/KangarooLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Beef(this.position));
        }
    }
}
class Snake extends Enemy {
    public Snake (Pair tilePos) {
        super(tilePos);
        this.width = 25;
        this.height = 25;
        this.health = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Snake/Snake.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Snake/SnakeLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Snake/Snake.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Snake/SnakeLeft.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(double time, World w) {
        this.position = this.tilePos.add(this.delta);
        if (w.detectCollision(Player.middle, width, height, this.position, (double)this.width/2, (double)this.height/2) && !this.attacking) {
            if (t == 15) {
                Player.health -= 1;
                t = 0;
            }
            t++;
            this.attacking = true;
        }
    }
    public void drop() {
    }
}
class Wolf extends Enemy {
    public Wolf (Pair tilePos) {
        super(tilePos);
        this.width = 45;
        this.height = 35;
        this.health = 5;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wolf/Wolf.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wolf/WolfLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wolf/Wolf2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Wolf/WolfLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        double n = Math.random();
        if (n < 0.5) {
            World.items.add(new WolfHide(this.position));
        }
    }
}
class Lion extends Enemy {
    public Lion (Pair tilePos) {
        super(tilePos);
        this.width = 55;
        this.height = 45;
        this.health = 10;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Lion/Lion.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Lion/LionLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Lion/Lion2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/Lion/LionLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n; i++) {
            World.items.add(new Beef(this.position));
        }
        double n2 = Math.random();
        if (n2 < 0.3) {
            World.items.add(new LionHide(this.position));
        }
    }
}
class PolarBear extends Enemy {
    public PolarBear (Pair tilePos) {
        super(tilePos);
        this.width = 75;
        this.height = 55;
        this.health = 15;
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/PolarBear/PolarBear.png")));
            this.left = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/PolarBear/PolarBearLeft.png")));
            this.img2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/PolarBear/PolarBear2.png")));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Entities/PolarBear/PolarBearLeft2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drop() {
        super.drop();
        int n1 = (int)(Math.random()*2) + 1;
        for (int i = 0; i < n1; i++) {
            World.items.add(new Beef(this.position));
        }
        double n2 = Math.random();
        if (n2 < 0.5) {
            World.items.add(new PolarHide(this.position));
        }
    }
}