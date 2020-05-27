package planetaryprotector.structure.building;
import java.util.ArrayList;
import planetaryprotector.structure.building.Building.Upgrade;
import planetaryprotector.structure.building.task.TaskAnimated;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.game.Game;
import simplelibrary.opengl.ImageStash;
public enum BuildingType{
    BASE("Base"),
    EMPTY("Empty Plot"),
    WRECK("Wreck"),
    SKYSCRAPER("Skyscraper"),
    MINE("Mine"),
    SOLAR_GENERATOR("Solar Generator"),
    COAL_GENERATOR("Coal Generator"),
    POWER_STORAGE("Power Storage"),
    SHIELD_GENERATOR("Shield Generator"),
    WORKSHOP("Workshop"),
    LABORATORY("Laboratory"),
    SILO("Silo"),
    OBSERVATORY("Observatory");
//    INFUSION_ALTAR("Infusion Altar");
    static{
        //<editor-fold defaultstate="collapsed" desc="SKYSCRAPER">
        SKYSCRAPER.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 10)
        };
        SKYSCRAPER.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 125)
            }
        };
        SKYSCRAPER.constructionTime = new int[]{
            11500
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="MINE">
        MINE.repairCost = new ItemStack[]{
            new ItemStack(Item.stone, 2)
        };
        MINE.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 10)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 5)
            }
        };
        MINE.constructionTime = new int[]{
            1800
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="SHIELD_GENERATOR">
        SHIELD_GENERATOR.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 20)
        };
        SHIELD_GENERATOR.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 80)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 70)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 80)
            }
        };
        SHIELD_GENERATOR.constructionTime = new int[]{
            1200
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="EMPTY">
        EMPTY.repairCost = new ItemStack[]{
            new ItemStack(Item.stone, 10)
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="SILO">
        SILO.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 50),
            new ItemStack(Item.stone, 100)
        };
        SILO.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 250),
                new ItemStack(Item.stone, 500)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 110),
                new ItemStack(Item.stone, 100)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 170),
                new ItemStack(Item.stone, 100)
            }
        };
        SILO.constructionTime = new int[]{
            20*60*15,
            20*60*5,
            (int)(20*60*7.5)
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="WORKSHOP">
        WORKSHOP.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 10)
        };
        WORKSHOP.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 100)
            }
        };
        WORKSHOP.constructionTime = new int[]{
            3000
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="OBSERVATORY">
        OBSERVATORY.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 15)
        };
        OBSERVATORY.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.stone, 250),
                new ItemStack(Item.ironIngot, 100),
                new ItemStack(Item.star, 1)
            }
        };
        OBSERVATORY.constructionTime = new int[]{
            4000
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="SOLAR_GENERATOR">
        SOLAR_GENERATOR.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 10)
        };
        SOLAR_GENERATOR.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 20)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 25)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 25)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 25)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 25)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 25)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 30)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 30)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 30)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            }
        };
        SOLAR_GENERATOR.constructionTime = new int[]{
            1000
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="COAL_GENERATOR">
        COAL_GENERATOR.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 20)
        };
        COAL_GENERATOR.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 75)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 35)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 40)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 55)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            }
        };
        COAL_GENERATOR.constructionTime = new int[]{
            1400
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="POWER_STORAGE">
        POWER_STORAGE.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 40)
        };
        POWER_STORAGE.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 100)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 60)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 70)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 70)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 70)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 80)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 90)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 100)
            }
        };
        POWER_STORAGE.constructionTime = new int[]{
            2000
        };
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="LABORATORY">
        LABORATORY.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 100)
        };
        LABORATORY.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 100),
                new ItemStack(Item.stone, 50)
            }
        };
        LABORATORY.constructionTime = new int[]{
            6000
        };
//</editor-fold>
        BASE.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 10)
        };
    }
    public final String texture;
    public ItemStack[][] costs;
    public ItemStack[] repairCost;
    public int[] constructionTime;
    public final String name;
    public ArrayList<Upgrade> upgrades = new ArrayList<>();
    private BuildingType(String name){
        this.name = name;
        texture = name.toLowerCase();
        costs = new ItemStack[0][0];
        repairCost = new ItemStack[0];
    }
    public ItemStack[] getCosts(int level){
        return costs[Math.min(costs.length-1, level)];
    }
    public int getTexture(){
        return ImageStash.instance.getTexture(getTextureS());
    }
    public int getDamageTexture(){
        return ImageStash.instance.getTexture(getDamageTextureS());
    }
    public int getTexture(String path){
        return ImageStash.instance.getTexture(getTextureS(path));
    }
    public String getTextureS(){
        return "/textures/buildings/"+texture+"/"+Game.theme.tex()+".png";
    }
    public String getDamageTextureS(){
        return "/textures/buildings/"+texture+"/damage.png";
    }
    public String getTextureS(String path){
        return "/textures/buildings/"+texture+"/"+path+".png";
    }
    public boolean isConstructable(){
        switch(this){
            case EMPTY:
            case WRECK:
            case BASE:
                return false;
            default:
                return true;
        }
    }
    public boolean isConstructable(Game game){
        switch(this){
            case SILO:
                return game.phase>=3;
            case OBSERVATORY:
                return game.observatory;
            case EMPTY:
            case WRECK:
            case BASE:
                return false;
            default:
                return true;
        }
    }
    public Building createNewBuilding(Game game, double x, double y){
        switch(this){
            case BASE:
                return new Base(game, x, y);
            case COAL_GENERATOR:
                return new CoalGenerator(game, x, y);
            case EMPTY:
                return new Plot(game, x, y);
            case LABORATORY:
                return new Laboratory(game, x, y);
            case MINE:
                return new Mine(game, x, y);
            case OBSERVATORY:
                return new Observatory(game, x, y);
            case POWER_STORAGE:
                return new PowerStorage(game, x, y);
            case SHIELD_GENERATOR:
                return new ShieldGenerator(game, x, y);
            case SILO:
                return new Silo(game, x, y);
            case SKYSCRAPER:
                return new Skyscraper(game, x, y);
            case SOLAR_GENERATOR:
                return new SolarGenerator(game, x, y);
            case WORKSHOP:
                return new Workshop(game, x, y);
            case WRECK:
                return new Wreck(game, x, y, 0);
            default:
                throw new IllegalBuildingException(this);
        }
    }
    public int[] getAnimation(){
        return TaskAnimated.getAnimation(getAnimationS());
    }
    public String getAnimationS(){
        return "/textures/tasks/construct/"+texture+"/"+Game.theme.tex();
    }
}