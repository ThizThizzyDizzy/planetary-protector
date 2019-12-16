package planetaryprotector.item;
import planetaryprotector.menu.component.MenuComponentClickable;
import java.util.ArrayList;
import planetaryprotector.menu.MenuGame;
import simplelibrary.opengl.ImageStash;
public class Item{
    public static final ArrayList<Item> items = new ArrayList<>();
    public static final ArrayList<Item> allItems = new ArrayList<>();
    public static final Item stone = new Item("Stone", "Stone");
    public static final Item ironOre = new Item("Iron Chunk", "Iron Chunk");
    public static final Item ironIngot = new Item("Iron Ingot", "Iron Ingot");
    public static final Item coal = new Item("Coal", "Coal");
    public static final Item star = new Item("Shooting Star", "Shooting Star");
    static{
        items.add(stone);
        items.add(ironOre);
        items.add(ironIngot);
        items.add(coal);
        star.priority = 1;
    }
    public static Item getItemByName(String get) {
        for(Item item : allItems){
            if(item.name.equals(get)){
                return item;
            }
        }
        return null;
    }
    public final String name;
    private final String texture;
    public MenuComponentClickable button;
    public int priority = 0;
    public Item(String name, String texture){
        this.name = name;
        this.texture = texture;
        allItems.add(this);
    }
    public int getWorldTexture(){
        return ImageStash.instance.getTexture(getWorldTextureS());
    }
    public String getWorldTextureS() {
        return "/textures/items/"+MenuGame.theme.tex()+"/"+texture+".png";
    }
    public int getTexture(){
        return ImageStash.instance.getTexture(getTextureS());
    }
    public String getTextureS() {
        return "/textures/items/"+MenuGame.Theme.NORMAL.tex()+"/"+texture+".png";
    }
}