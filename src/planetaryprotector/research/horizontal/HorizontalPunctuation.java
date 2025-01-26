package planetaryprotector.research.horizontal;
public abstract class HorizontalPunctuation{
    public abstract float getWidth(float size);
    public abstract float getHeight(float size);
    public final void draw(float x, float y, float size){
        render(x, y, size);
    }
    protected abstract void render(float x, float y, float size);
}
