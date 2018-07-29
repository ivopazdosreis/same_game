package SameGame;

public class Coordenada {
    
    private int x, y;
    
    public Coordenada(int x, int y) {
        
        this.x = x;
        this.y = y;
    }
    
    public int get_x() { return x; }
    public int get_y() { return y; }
    
    public void set_xy(int x, int y) { 
        this.x = x;
        this.y = y;
    }
    
}
