package planetaryprotector.game;
import java.util.ArrayList;
import java.util.HashMap;
import planetaryprotector.structure.SkyscraperDecal;
/**
 * Game state for save/load with Gson
 *
 * @author Thiz
 */
public class GameState{
    public String saveName;//name of the save, as different from filename
    public boolean tutorial;
    public int level = 1;
    public String worldGenerator;
    public String story;
    public String version;
    public double meteorTimer;
    public int mothershipHealth = -1;
    public int furnaceLevel;
    public int furnaceTimer = 100;
    public int furnaceOre;
    public int furnaceCoal;
    public int furnaceXP;
    public double enemyStrength = 1;
    public int enemyTimer = 20*30;
    public ArrayList<Item> droppedItems = new ArrayList<>();
    public boolean meteorShower;
    public int meteorShowerTimer;
    public int workers = 1;
    public ArrayList<Structure> structures = new ArrayList<>();
    public int workerCooldown;
    public int phase;
    public int phaseCounter;
    public int targetPopulation;
    public int popPerFloor;
    public double meteorShowerDelay;
    public double meteorShowerIntensity;
    public Expedition pendingExpedition;
    public ArrayList<Expedition> activeExpeditions = new ArrayList<>();
    public ArrayList<Expedition> finishedExpeditions = new ArrayList<>();
    public int fogTimer;
    public int cloudTimer;
    public double fogTime;
    public double fogTimeIncrease;
    public double fogIntensity;
    public double fogHeightIntensity;
    public int secretTimer;
    public int secretWaiting;
    public boolean observatoryUnlocked;
    public int time;
    public HashMap<String, Research> researches = new HashMap<>();
    public ArrayList<Resource> resources = new ArrayList<>();
    public int losing;
    int bboxX;
    int bboxY;
    int bboxWidth;
    int bboxHeight;
    public static class Item{
        public String item = planetaryprotector.item.Item.ironIngot.name;
        public int life;
        public int x;
        public int y;
    }
    public static class Resource{
        public String item;
        public int count;
    }
    public static class Structure{
        public Skyscraper skyscraper;
        public ShieldGenerator shieldGenerator;
        public Silo silo;
        public CoalGenerator coalGenerator;
        public Laboratory laboratory;
        public Mine mine;
        public Observatory observatory;
        public PowerStorage powerStorage;
        public Wreck wreck;
        public String type;
        public int level;
        public ArrayList<String> upgrades = new ArrayList<>();
        public ArrayList<Damage> damages = new ArrayList<>();
        public int x;
        public int y;
        public double fire;
        public int fireDamage;
        public double fireIncreaseRate;
        public ArrayList<Fire> fires = new ArrayList<>();
        public double power;
        public double starlight;
        public int shield;
        public static class Damage{
            public float x;
            public float y;
            public int tex1;
            public int tex2;
            public float size;
            public float opacity;
        }
        public static class Fire{
            public int x;
            public int y;
        }
        public static class Skyscraper{
            public int floors = 10;
            public boolean falling;
            public int fallen;
            public boolean falled;
            public double population;
            public ArrayList<Decal> decals = new ArrayList<>();
            public static class Decal{
                public int x;
                public int y;
                public SkyscraperDecal.Type type;
                public int variant;
            }
        }
        public static class ShieldGenerator{
            public float blastRecharge;
            public double shieldSize;
            public double maxShieldSize = 500;
            public float shieldStrength = 1;
            public double oldPower;
            public int target = -1;
        }
        public static class Silo{
            public int drones;
            public int missiles;
        }
        public static class CoalGenerator{
            public boolean autoFuel;
        }
        public static class Laboratory{
            public String target = "null";
        }
        public static class Mine{
            public int timer;
            public int delay;
            public boolean powerTools;
        }
        public static class Observatory{
            public double starlight;
            public int scanning;
            public double collecting;
        }
        public static class PowerStorage{
            public boolean charge;
            public boolean discharge;
            public int daylightThreshold;
            public boolean automaticControl;
            public boolean daylightControl;
            public boolean daylightRecharge;
            public boolean daylightDischarge;
            public boolean nightRecharge;
            public boolean nightDischarge;
            public boolean neutralRecharge;
            public boolean neutralDischarge;
            public boolean meteorOverride;
            public boolean meteorRecharge;
            public boolean meteorDischarge;
            public int lastDaylight;
            public boolean lastMeteor;
        }
        public static class Wreck{
            public int ingots = 1;
            public int progress;
        }
    }
    public static class Expedition{
        public int requiredWorkers = 1;
        public int workers = 1;
        public int time;
        public int totalTime;
        public int civilians;
        public int civilianCooldown;
        public int dieCooldown;
        public boolean returning;
        public boolean recalled;
        public boolean done;
        public boolean returned;
    }
    public static class Research{
        public int discovery;
        public ArrayList<DiscoveryStage> stages = new ArrayList<>();
        public int power;
        public double starlight;
        public int time;
        public ArrayList<Integer> itemCosts = new ArrayList<>();
        public boolean completed;
        public static class DiscoveryStage{
            public double progress;
            public ArrayList<Prerequisite> prerequisites = new ArrayList<>();
            public static class Prerequisite{
                public double progress;
                public int discovery;
                public ArrayList<Prerequisite> prerequisites = new ArrayList<>();
            }
        }
    }
}
