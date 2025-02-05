import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

class Game extends JPanel implements KeyListener, MouseInputListener {
    public static final int width = 1024;
    public static final int height = 768;
    public static final int fps = 60;
    public static Pair mousePos = new Pair(0,0);
    public static boolean mouseClicked = false;
    public static boolean mouseClickProcessed = false;
    public static boolean menuOpen = true;
    public static Menu menu = new Menu();
    public final static Random seed = new Random();
    World world;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game mainInstance = new Game();
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);
    }
    class Runner implements Runnable {
        public void run() {
            while (true){
                if (menuOpen) {
                    menu.update();
                }
                else {
                    world.update(1.0 / (double) fps);
                }
                repaint();
                try {
                    Thread.sleep(1000/fps);
                }
                catch(InterruptedException e){}
            }
        }
    }
    public Game() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.world = new World();
        this.setPreferredSize(new Dimension(width, height));
        Thread mainThread = new Thread(new Runner());
        mainThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menuOpen) {
            menu.draw(g);
        }
        else {
            world.draw(g);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        switch (c) {
            case '1': Inventory.barSlot = 1; break;
            case '2': Inventory.barSlot = 2; break;
            case '3': Inventory.barSlot = 3; break;
            case '4': Inventory.barSlot = 4; break;
            case '5': Inventory.barSlot = 5; break;
            case '6': Inventory.barSlot = 6; break;
            case 'w': Player.up = true; Player.walk++; break;
            case 'a': Player.left = true; Player.walk++; break;
            case 's': if (Game.menuOpen && !Menu.loading && !Menu.saving) {menu.save();} else {Player.down = true; Player.walk++;} break;
            case 'd': Player.right = true; Player.walk++; break;
            case 'l': if (Game.menuOpen && !Menu.loading && !Menu.saving) {menu.load();} break;
            case 'q': Player.attack = true; break;
            case 'e':
                if (!Player.openInventory) {
                    if (Player.crafting) {
                        Player.crafting = false;
                    } else {
                        Player.crafting = true;
                    }
                } break;
            case 'r':
                if (!Player.crafting) {
                    if (Player.openInventory) {
                        Player.openInventory = false;
                    } else {
                        Player.openInventory = true;
                    }
                } break;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (Game.menuOpen && !Menu.loading && !Menu.saving) {
                Game.menuOpen = false;
            }
            else {
                Game.menuOpen = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            Player.placing = "up";
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            Player.placing = "down";
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            Player.placing = "left";
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            Player.placing = "right";
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
        switch (c) {
            case 'w': Player.up = false; Player.walk = 0; break;
            case 'a': Player.left = false; Player.walk = 0; break;
            case 's': Player.down = false; Player.walk = 0; break;
            case 'd': Player.right = false; Player. walk = 0; break;
            case 'q': Player.attack = false; Entity.attacked = false; break;
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {mousePos = new Pair(e.getX(), e.getY());}
    @Override
    public void mouseClicked(MouseEvent e) {mouseClicked = true; mouseClickProcessed = false;}
    @Override
    public void mouseReleased(MouseEvent e) {mouseClicked = false;}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
class Menu {
    static BufferedImage img;
    static BufferedImage[] animation = new BufferedImage[5];
    static File save = new File("save.txt");
    static boolean saving = false;
    static boolean loading = false;
    static double[][] vels = new double[5][2];
    static int[][] pos = new int[5][2];
    static int n = 0;
    public Menu() {
        for (int i = 0; i < vels.length; i++) {
            for (int j = 0; j < 2; j++) {
                vels[i][j] = Math.random()*3-1.5;
            }
        }
        try {
            img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Menu/Menu.png")));
            animation[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Menu/Menu_Animation.png")));
            animation[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Menu/Menu_Animation1.png")));
            animation[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Menu/Menu_Animation2.png")));
            animation[3] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Menu/Menu_Animation3.png")));
            animation[4] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Menu/Menu_Animation4.png")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void save(){
        saving = true;
        try {
            PrintWriter writer = new PrintWriter(save);
            writer.write(World.position + " ");
            for (Biome b : World.biomes) {
                writer.write("Biome" + " " + b.coordinates[0] + " " + b.coordinates[1] + " " + b.type + " " + b.initX + " " + b.initY + " " + b.seed + " ");
            }
            for (Building b : World.buildings) {
                writer.write("Building" + " " + b.tilePos + " " + b.type + " " + b.orientation + " ");
            }
            for (int i = 0; i < Inventory.inventory.length; i++) {
                for (int j = 0; j < Inventory.inventory[0].length; j++) {
                    if (Inventory.inventory[i][j] != null) {
                        writer.write("Stack" + " " + Inventory.inventory[i][j].index[0] + " " + Inventory.inventory[i][j].index[1] + " " + Inventory.inventory[i][j].type + " " + Inventory.inventory[i][j].size);
                    }
                }
            }
            writer.close();
        }
        catch (FileNotFoundException e){
            System.err.println(e);
        }
        saving = false;
    }
    public void load() {
        loading = true;
        World.rendered.clear();
        World.biomes.clear();
        World.items.clear();
        World.animals.clear();
        World.buildings.clear();
        for (int i = 0; i < Inventory.inventory.length; i++) {
            for (int j = 0; j < Inventory.inventory[0].length; j++) {
                Inventory.inventory[i][j] = null;
            }
        }
        try {
            Scanner a = new Scanner(save);
            World.position = new Pair(a.nextDouble(), a.nextDouble());
            while (a.hasNext()) {
                String next = a.next();
                if (next.equals("Biome")) {
                    int c1 = a.nextInt();
                    int c2 = a.nextInt();
                    World.rendered.add(new int[]{c1, c2});
                    switch (a.next()) {
                        case ("grassland"):
                            Biome g = new Grassland(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(g);
                            break;
                        case ("savannah"):
                            Biome s = new Savannah(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(s);
                            break;
                        case ("forest"):
                            Biome f = new Forest(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(f);
                            break;
                        case ("tundra"):
                            Biome t = new Tundra(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(t);
                            break;
                        case ("outback"):
                            Biome o = new Outback(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(o);
                            break;
                        case ("rainforest"):
                            Biome r = new Rainforest(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(r);
                            break;
                        case ("mountain"):
                            Biome m = new Mountain(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(m);
                            break;
                        case ("desert"):
                            Biome d = new Desert(a.nextDouble(), a.nextDouble(), c1, c2, a.nextInt());
                            World.biomes.add(d);
                            break;
                    }
                }
                else if (next.equals("Building")) {
                    double p1 = a.nextDouble();
                    double p2 = a.nextDouble();
                    //Iterate through each biome to find where the structure is. Then iterate through tile to find it's tilepos
                    for (Biome b: World.biomes) {
                        if (p1 < b.position.x + World.position.x + Biome.width && p1 > b.position.x + World.position.x - Biome.width && p2 < b.position.y + World.position.y + Biome.height && p2 > b.position.y + World.position.y - Biome.height) {
                            for (int i = 0; i < b.tiles.length; i++) {
                                for (int j = 0; j < b.tiles[0].length; j++) {
                                    if (p1 == b.tiles[i][j].position.x + World.position.x && p2 == b.tiles[i][j].position.y + World.position.y) {
                                        switch (a.next()) {
                                            case("stonewall"): World.buildings.add(new Stone_Wall(b.tiles[i][j].position, a.next()));
                                            case("woodfence"): World.buildings.add(new Wood_Fence(b.tiles[i][j].position, a.next()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else if (next.equals("Stack")) {
                    int[] index = new int[]{a.nextInt(), a.nextInt()};
                    switch (a.next()) {
                        //I know this is ugly but I also don't know how to fix it
                        case ("stonewall"): Player.inventory.addItem(new StoneWall(20,20),index,a.nextInt()); break;
                        case ("woodfence"): Player.inventory.addItem(new WoodFence(20,20),index,a.nextInt()); break;
                        case ("stonesword"): Player.inventory.addItem(new StoneSword(20,20),index,a.nextInt()); break;
                        case ("goldsword"): Player.inventory.addItem(new GoldSword(20,20),index,a.nextInt()); break;
                        case ("ironsword"): Player.inventory.addItem(new IronSword(20,20),index,a.nextInt()); break;
                        case ("rubysword"): Player.inventory.addItem(new RubySword(20,20),index,a.nextInt()); break;
                        case ("emeraldsword"): Player.inventory.addItem(new EmeraldSword(20,20),index,a.nextInt()); break;
                        case ("stoneaxe"): Player.inventory.addItem(new StoneAxe(20,20),index,a.nextInt()); break;
                        case ("ironaxe"): Player.inventory.addItem(new IronAxe(20,20),index,a.nextInt()); break;
                        case ("rubyaxe"): Player.inventory.addItem(new RubyAxe(20,20),index,a.nextInt()); break;
                        case ("copperaxe"): Player.inventory.addItem(new CopperAxe(20,20),index,a.nextInt()); break;
                        case ("stonepickaxe"): Player.inventory.addItem(new StonePickaxe(20,20),index,a.nextInt()); break;
                        case ("ironpickaxe"): Player.inventory.addItem(new IronPickaxe(20,20),index,a.nextInt()); break;
                        case ("rubypickaxe"): Player.inventory.addItem(new RubyPickaxe(20,20),index,a.nextInt()); break;
                        case ("copperpickaxe"): Player.inventory.addItem(new CopperPickaxe(20,20),index,a.nextInt()); break;
                        case ("wood"): Player.inventory.addItem(new Wood(new Pair(0,0)),index,a.nextInt()); break;
                        case ("stone"): Player.inventory.addItem(new Stone(new Pair(0,0)),index,a.nextInt()); break;
                        case ("mushroom"): Player.inventory.addItem(new Mushroom_Item(new Pair(0,0)),index,a.nextInt()); break;
                        case ("beef"): Player.inventory.addItem(new Beef(new Pair(0,0)),index,a.nextInt()); break;
                        case ("chicken"): Player.inventory.addItem(new ChickenWing(new Pair(0,0)),index,a.nextInt()); break;
                        case ("banana"): Player.inventory.addItem(new Banana(new Pair(0,0)),index,a.nextInt()); break;
                        case ("copper"): Player.inventory.addItem(new Copper(new Pair(0,0)),index,a.nextInt()); break;
                        case ("emerald"): Player.inventory.addItem(new Emerald(new Pair(0,0)),index,a.nextInt()); break;
                        case ("gold"): Player.inventory.addItem(new Gold(new Pair(0,0)),index,a.nextInt()); break;
                        case ("ruby"): Player.inventory.addItem(new Ruby(new Pair(0,0)),index,a.nextInt()); break;
                        case ("iron"): Player.inventory.addItem(new Iron(new Pair(0,0)),index,a.nextInt()); break;
                        case ("ironhelmet"): Player.inventory.addItem(new IronHelmet(20,20),index,a.nextInt()); break;
                        case ("polarhelmet"): Player.inventory.addItem(new PolarHelmet(20,20),index,a.nextInt()); break;
                        case ("crown"): Player.inventory.addItem(new Crown(20,20),index,a.nextInt()); break;
                        case ("ironchest"): Player.inventory.addItem(new IronChest(20,20),index,a.nextInt()); break;
                        case ("lionarmor"): Player.inventory.addItem(new LionArmor(20,20),index,a.nextInt()); break;
                        case ("polarcover"): Player.inventory.addItem(new PolarCover(20,20),index,a.nextInt()); break;
                        case ("wolfcover"): Player.inventory.addItem(new WolfCover(20,20),index,a.nextInt()); break;
                        case ("cake"): Player.inventory.addItem(new Cake(new Pair(0,0)),index,a.nextInt()); break;
                        case ("egg"): Player.inventory.addItem(new Egg(new Pair(0,0)),index,a.nextInt()); break;
                        case ("friedegg"): Player.inventory.addItem(new FriedEgg(new Pair(0,0)),index,a.nextInt()); break;
                        case ("milk"): Player.inventory.addItem(new Milk(new Pair(0,0)),index,a.nextInt()); break;
                        case ("polarhide"): Player.inventory.addItem(new PolarHide(new Pair(0,0)),index,a.nextInt()); break;
                        case ("wolfhide"): Player.inventory.addItem(new WolfHide(new Pair(0,0)),index,a.nextInt()); break;
                        case ("lionhide"): Player.inventory.addItem(new WolfHide(new Pair(0,0)),index,a.nextInt()); break;
                        case ("bone"): Player.inventory.addItem(new Bone(new Pair(0,0)),index,a.nextInt()); break;
                        case ("lamb"): Player.inventory.addItem(new Lamb(new Pair(0,0)),index,a.nextInt()); break;
                    }
                }
            }
            a.close();
        }
        catch (Exception e){
            System.err.println(e);
        }
        loading = false;
    }
    public void update() {
        for (int i = 1; i < pos.length; i++) {
            for (int j = 0; j < 2; j++) {
                pos[i][j] += vels[i][j];
            }
        }
        if (n < 100) {
            n++;
        }
        else {
            n = 0;
            for (int i = 0; i < pos.length; i++) {
                for (int j = 0; j < 2; j++) {
                    double vel = Math.random()*3-1.5;
                    if (Math.signum(vels[i][j]) == Math.signum(vel)) {
                        vels[i][j] = -vel;
                    }
                    else {
                        vels[i][j] = vel;
                    }
                }
            }
        }
    }
    public void draw(Graphics g) {
        g.drawImage(animation[0],pos[0][0],pos[0][1],Game.width,Game.height,null,null);
        for (int i = 1; i < pos.length; i++) {
            g.drawImage(animation[i],pos[i][0]-Game.width*2,pos[i][1]-Game.height*2,Game.width*4,Game.height*4,null,null);
        }
        g.drawImage(img,0,0,Game.width,Game.height,null,null);
    }
}
class World {
    static Pair position = new Pair(0,0);
    static Player player = new Player();
    static LinkedList<int[]> rendered = new LinkedList<int[]>();
    static LinkedList<Biome> biomes = new LinkedList<Biome>();
    static LinkedList<Item> items = new LinkedList<>();
    static LinkedList<Animal> animals = new LinkedList<>();
    static LinkedList<Building> buildings = new LinkedList<>();
    public World() {
        rendered.add(new int[]{0,0});
        biomes.add(new Grassland((double)Game.width/2, (double)Game.height/2, 0,0,Game.seed.nextInt()));
    }
    public void update(double time) {
        player.update(this);
        //rendering based on proximity to player (all of the if statements in the loops)
        for (int i = 0; i < biomes.size(); i++) {
            if ((player.coords[0] + 1 > biomes.get(i).coordinates[0] || player.coords[0] - 1 < biomes.get(i).coordinates[0]) && player.coords[0] + 1 > biomes.get(i).coordinates[1] || player.coords[1] - 1 < biomes.get(i).coordinates[1]) {
                biomes.get(i).update(time, this);
            }
        }
        for (int i = 0; i < animals.size(); i++) {
            if (detectCollision(Player.middle,Player.width,Player.height,animals.get(i).tilePos,Game.width*2,Game.height*2)) {
                animals.get(i).update(time, this);
            }
        }
        for (int i = 0; i < items.size(); i++) {
            items.get(i).update(this);
        }
        for (int i = 0; i < buildings.size(); i++) {
            if (detectCollision(Player.middle,Player.width,Player.height,buildings.get(i).tilePos,Game.width*2,Game.height*2)) {
                buildings.get(i).update(time, this);
            }
        }
    }
    public void draw(Graphics g) {
        for (int i = 0; i < biomes.size(); i++) {
            if ((player.coords[0] + 1 > biomes.get(i).coordinates[0] || player.coords[0] - 1 < biomes.get(i).coordinates[0]) && player.coords[0] + 1 > biomes.get(i).coordinates[1] || player.coords[1] - 1 < biomes.get(i).coordinates[1]) {
                biomes.get(i).draw(g);
            }
        }
        for (int i = 0; i < animals.size(); i++) {
            if (detectCollision(Player.middle,Player.width,Player.height,animals.get(i).tilePos,Game.width*2,Game.height*2)) {
                animals.get(i).draw(g);
            }
        }
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(g);
        }
        for (int i = 0; i < buildings.size(); i++) {
            if (detectCollision(Player.middle,Player.width,Player.height,buildings.get(i).tilePos,Game.width*2,Game.height*2)) {
                buildings.get(i).draw(g);
            }
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("X: " + -World.position.x + "   Y:" + -World.position.y, 20, 40);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        player.draw(g);
    }
    public Biome randomBiome(double x, double y, int coord1, int coord2) {
        double n = Math.random();
        if (n <= 0.125) {
            return new Grassland(x,y,coord1,coord2,Game.seed.nextInt());
        }
        else if (n <= 0.25) {
            return new Savannah(x,y,coord1,coord2,Game.seed.nextInt());
        }
        else if (n <= 0.375) {
            return new Tundra(x,y,coord1,coord2,Game.seed.nextInt());
        }
        else if (n <= 0.5) {
            return new Desert(x,y,coord1,coord2,Game.seed.nextInt());
        }
        else if (n <= 0.625) {
            return new Outback(x,y,coord1,coord2,Game.seed.nextInt());
        }
        else if (n <= 0.75) {
            return new Rainforest(x,y,coord1,coord2,Game.seed.nextInt());
        }
        else if (n <= 0.875) {
            return new Mountain(x,y,coord1,coord2,Game.seed.nextInt());
        }
        else {
            return new Forest(x,y,coord1,coord2,Game.seed.nextInt());
        }
    }
    public boolean detectCollision(Pair pos1, double w1, double h1, Pair pos2, double w2, double h2) {
        double x1 = pos1.x;
        double y1 = pos1.y;
        double x2 = pos2.x;
        double y2 = pos2.y;

        boolean hit = (x1 + w1 >= x2 - w2 && x1 - w1 <= x2 + w2) && (y1 + h1 >= y2 - h2 && y1 - h1 <= y2 + h2);
        return hit;
    }
}
class Pair {
    public double x;
    public double y;
    public Pair(double initX, double initY){
        x = initX;
        y = initY;
    }
    public Pair add(Pair toAdd){
        return new Pair(x + toAdd.x, y + toAdd.y);
    }
    public Pair divide(double denom){
        return new Pair(x / denom, y / denom);
    }
    public Pair times(double val){
        return new Pair(x * val, y * val);
    }
    public void flipX(){
        x = -x;
    }
    public void flipY(){
        y = -y;
    }
    @Override
    public String toString() {
        return this.x + " " + this.y;
    }
}