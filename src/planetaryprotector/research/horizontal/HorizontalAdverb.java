package planetaryprotector.research.horizontal;
public abstract class HorizontalAdverb{
    public final void draw(float x, float y, float size){
        float w = getWidth(size);
        float h = getHeight(size);
        render(x, y, w, h);
    }
    public abstract float getWidth(float size);
    public abstract float getHeight(float size);
    protected abstract void render(float x, float y, float w, float h);
}
