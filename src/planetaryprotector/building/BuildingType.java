package planetaryprotector.building;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
public enum BuildingType{
    SKYSCRAPER("Skyscraper","skyscraper"),
    MINE("Mine","mine"),
    GENERATOR("Generator","generator"),
    SHIELD_GENERATOR("Shield Generator","shield generator"),
    EMPTY("Empty Plot","empty plot"),
    WRECK("Wreck","wreck"),
    BUNKER("Bunker","bunker"),
    SILO("Silo","silo"),
    BASE("Base","base");
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
            12000
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
        //<editor-fold defaultstate="collapsed" desc="GENERATOR">
        GENERATOR.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 15)
        };
        GENERATOR.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 25)
            }
        };
        GENERATOR.constructionTime = new int[]{
            900
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
        //<editor-fold defaultstate="collapsed" desc="BUNKER">
        BUNKER.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 25)
        };
        BUNKER.costs = new ItemStack[][]{
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
            }
        };
        BUNKER.constructionTime = new int[]{
            3000
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
        BASE.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 10)
        };
    }
    public final String texture;
    public ItemStack[][] costs;
    public ItemStack[] repairCost;
    public int[] constructionTime;
    public final String name;
    private BuildingType(String name, String texture){
        this.name = name;
        this.texture = texture;
        costs = new ItemStack[0][0];
        repairCost = new ItemStack[0];
    }
}