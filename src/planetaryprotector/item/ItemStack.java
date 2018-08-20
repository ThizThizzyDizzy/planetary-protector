package planetaryprotector.item;
public class ItemStack{
    public final Item item;
    public int count;
    public ItemStack(Item item, int amount){
        this.item = item;
        this.count = amount;
    }
    public ItemStack(Item item){
        this(item, 1);
    }
    public ItemStack(ItemStack stack){
        this(stack.item, stack.count);
    }
}