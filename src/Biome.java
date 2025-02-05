import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
abstract class Biome {
    public Pair position;
    public String type;
    public int[] coordinates = new int[2];
    public double initX;
    public double initY;
    static final int width = Game.width*3;
    static final int height = Game.width*3;
    int seed;
    Random rand;
    public BufferedImage img;
    Tile[][] tiles;
    public Biome(double x, double y, int coord1, int coord2, int seed) {
        this.seed = seed;
        this.rand = new Random(seed);
        position = new Pair(x, y);
        coordinates = new int[]{coord1, coord2};
        initX = this.position.x;
        initY = this.position.y;
        createTiles();
    }
    public void createTiles() {
        tiles = new Tile[width/Tile.width][height/Tile.height];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = new Tile();
                tiles[i][j].position = new Pair((int)this.position.x - width+Tile.width+2*Tile.width*i, (int)this.position.y - height+Tile.height+2*Tile.height*j);
            }
        }
    }
    public void update(double time, World w) {
        //basically just updating the biome based on the change in the world's position
        this.position.x = initX + World.position.x;
        this.position.y = initY + World.position.y;
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                tiles[i][j].position.x = (int)this.position.x - width+Tile.width+2*Tile.width*i;
                tiles[i][j].position.y = (int)this.position.y - height+Tile.height+2*Tile.height*j;
                if (tiles[i][j].entity != null) {
                    tiles[i][j].entity.update(time, w);
                }
            }
        }
    }
    public void draw(Graphics g) {
        g.drawImage(this.img, (int)this.position.x - width, (int)this.position.y - height, width*2, height*2, null, null);
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                if (tiles[i][j].entity != null) {
                    tiles[i][j].entity.draw(g);
                }
            }
        }
    }

}
class Tile {
    //default width and height
    static final int width = 64*3;
    static final int height = 64*3;
    public Pair position;
    public Entity entity;
}
/**
 * CODE FOR EACH BIOME BELOW
 */
class Grassland extends Biome {
    public Grassland (double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "grassland";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Grassland.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.05) {
                    t.entity = new Rock(tiles[i][j].position);
                }
                else if(p < 0.1) {
                    t.entity = new Tree(tiles[i][j].position);
                }
                else if (p < 0.15) {
                    World.animals.add(new Chicken(tiles[i][j].position));
                }
                else if (p < 0.2) {
                    World.animals.add(new Sheep(tiles[i][j].position));
                }
                else if (p < 0.25) {
                    World.animals.add(new Cow(tiles[i][j].position));
                }
                else if (p < 0.3) {
                    World.animals.add(new Monster(tiles[i][j].position));
                }
                else if (p < 0.4) {
                    World.animals.add(new Rabbit(tiles[i][j].position));
                }
                else if (p < 0.45) {
                    t.entity = new EmeraldRock(tiles[i][j].position);
                }
                else if (p < 0.5) {
                    World.animals.add(new Goat(tiles[i][j].position));
                }
                else {
                    t.entity = null;
                }
            }
        }
    }
}
class Forest extends Biome {
    public Forest(double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "forest";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Forest.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.3) {
                    t.entity = new Forest_Tree(tiles[i][j].position);
                }
                else if(p < 0.35) {
                    t.entity = new Mushroom(tiles[i][j].position);
                }
                else if (p < 0.4) {
                    World.animals.add(new Monster(tiles[i][j].position));
                }
                else if (p < 0.425) {
                    World.animals.add(new Sheep(tiles[i][j].position));
                }
                else if (p < 0.475) {
                    World.animals.add(new Rabbit(tiles[i][j].position));
                }
                else if (p < 0.5) {
                    t.entity = new EmeraldRock(tiles[i][j].position);
                }
                else {
                    t.entity = null;
                }
            }
        }
    }
}
class Savannah extends Biome {
    public Savannah(double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "savannah";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Savannah.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.05) {
                    t.entity = new Cactus(tiles[i][j].position);
                }
                else if (p < 0.1) {
                    World.animals.add(new Capybara(tiles[i][j].position));
                }
                else if (p < 0.2) {
                    World.animals.add(new Lion(tiles[i][j].position));
                }
                else if (p < 0.25) {
                    World.animals.add(new Rabbit(tiles[i][j].position));
                }
                else if (p < 0.3) {
                    t.entity = new IronRock(tiles[i][j].position);
                }
                else if (p < 0.35) {
                    t.entity = new RubyRock(tiles[i][j].position);
                }
                else {
                    t.entity = null;
                }
            }
        }

    }
}
class Tundra extends Biome {
    public Tundra(double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "tundra";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Tundra.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.1) {
                    t.entity = new Snow_Tree(tiles[i][j].position);
                }
                else if (p < 0.2) {
                    World.animals.add(new Wolf(tiles[i][j].position));
                }
                else if(p < 0.3) {
                    World.animals.add(new Penguin(tiles[i][j].position));
                }
                else if (p < 0.375) {
                    World.animals.add(new PolarBear(tiles[i][j].position));
                }
                else if (p < 0.45) {
                    t.entity = new IronRock(tiles[i][j].position);
                }
                else if (p < 0.5) {
                    t.entity = new RubyRock(tiles[i][j].position);
                }
                else if (p < 0.55) {
                    t.entity = new Rock(tiles[i][j].position);
                }
                else {
                    t.entity = null;
                }
            }
        }
    }
}
class Desert extends Biome {
    public Desert(double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "desert";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Desert.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.05) {
                    World.animals.add(new Camel(tiles[i][j].position));
                }
                else if(p < 0.2) {
                    t.entity = new Cactus(tiles[i][j].position);
                }
                else if (p < 0.25) {
                    World.animals.add(new Snake(tiles[i][j].position));
                }
                else if (p < 0.3) {
                    t.entity = new CopperRock(tiles[i][j].position);
                }
                else {
                    t.entity = null;
                }
            }
        }
    }
}
class Outback extends Biome {
    public Outback(double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "outback";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Outback.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.2) {
                    World.animals.add(new Kangaroo(tiles[i][j].position));
                }
                if (p < 0.35) {
                    World.animals.add(new Wombat(tiles[i][j].position));
                }
                else if (p < 0.4) {
                    t.entity = new CopperRock(tiles[i][j].position);
                }
                else if (p < 0.5) {
                    t.entity = new GoldRock(tiles[i][j].position);
                }
                else if (p < 0.6) {
                    t.entity = new Eucalyptus_Tree(tiles[i][j].position);
                    double p2 = rand.nextDouble(1);
                    if (p2 < 0.4) {
                        World.animals.add(new Koala(tiles[i][j].position));
                    }
                }
                else {
                    t.entity = null;
                }
            }
        }
    }
}
class Rainforest extends Biome {
    public Rainforest(double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "rainforest";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Rainforest.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.05) {
                    World.animals.add(new Ape(tiles[i][j].position));
                }
                else if (p < 0.2) {
                    t.entity = new Rain_Tree(tiles[i][j].position);
                }
                else if (p < 0.4) {
                    t.entity = new Banana_Tree(tiles[i][j].position);
                }
                else if (p < 0.425) {
                    World.animals.add(new Capybara(tiles[i][j].position));
                }
                else if (p < 0.5) {
                    t.entity = new RubyRock(tiles[i][j].position);
                }
                else {
                    t.entity = null;
                }
            }
        }
    }
}
class Mountain extends Biome {
    public Mountain(double x, double y, int coord1, int coord2, int seed) {
        super(x, y, coord1, coord2, seed);
        this.type = "mountain";
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("Biomes/Mountain.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < width/Tile.width; i++) {
            for (int j = 0; j < height/Tile.height; j++) {
                Tile t = this.tiles[i][j];
                double p = this.rand.nextDouble(1);
                if (p < 0.05) {
                    World.animals.add(new Sheep(tiles[i][j].position));
                }
                else if(p < 0.2) {
                    t.entity = new Snow_Tree(tiles[i][j].position);
                }
                else if (p < 0.25) {
                    World.animals.add(new Wolf(tiles[i][j].position));
                }
                else if (p < 0.4) {
                    t.entity = new Rock(tiles[i][j].position);
                }
                else if (p < 0.5) {
                    t.entity = new IronRock(tiles[i][j].position);
                }
                else if (p < 0.55) {
                    t.entity = new RubyRock(tiles[i][j].position);
                }
                else if (p < 0.6) {
                    t.entity = new CopperRock(tiles[i][j].position);
                }
                else if (p < 0.65) {
                    t.entity = new GoldRock(tiles[i][j].position);
                }
                else if (p < 0.75) {
                    World.animals.add(new Goat(tiles[i][j].position));
                }
                else {
                    t.entity = null;
                }
            }
        }
    }
}


