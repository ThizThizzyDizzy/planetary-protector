package planetaryprotector.building;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
public enum BuildingType{
    SKYSCRAPER("Skyscraper"),
    MINE("Mine"),
    GENERATOR("Generator"),
    SHIELD_GENERATOR("Shield Generator"),
    EMPTY("Empty Plot"),
    WRECK("Wreck"),
    BUNKER("Bunker"),
    SILO("Silo"),
    BASE("Base"),
    WORKSHOP("Workshop"),
    OBSERVATORY("Observatory");
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
                new ItemStack(Item.ironIngot, 30)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 30)
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
                new ItemStack(Item.ironIngot, 45)
            },
            new ItemStack[]{
                new ItemStack(Item.ironIngot, 50)
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
        BASE.repairCost = new ItemStack[]{
            new ItemStack(Item.ironIngot, 10)
        };
    }
    public final String texture;
    public ItemStack[][] costs;
    public ItemStack[] repairCost;
    public int[] constructionTime;
    public final String name;
    private BuildingType(String name){
        this.name = name;
        texture = name.toLowerCase();
        costs = new ItemStack[0][0];
        repairCost = new ItemStack[0];
    }
    public ItemStack[] getCosts(int level){
        return costs[Math.min(costs.length-1, level+1)];
    }
}