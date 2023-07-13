public class Pozicia {
    private final int x;
    private final int y;
    
    public Pozicia(int x_stlpec, int y_riadok){ //x je vlastne stlpec, y riadok
        this.x = x_stlpec;
        this.y = y_riadok;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getRiadok(){
        return this.y;
    }
    
    public int getStlpec(){
        return this.x;
    }
   
}
