package BeeClustering.aux;

    
public class Key {

    public float x, y;

    public Key(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Key)) {
            return false;
        }
        Key key = (Key) o;
        return x == key.x && y == key.y;
    }

    @Override
    public int hashCode() {
        return (int) (((int) this.x << 16) + this.y);
    }
}
