import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//prakticky to iste co pri bielom kameni, s upravenymi farbou a ikonkou
public class CiernyKamen extends HernyKamen {
    
    public CiernyKamen(Hrac hrac, Pozicia pozicia){
        super(hrac, pozicia);
        
    }
    
    public CiernyKamen(Hrac hrac, int riadok, int stlpec){
        super(hrac, new Pozicia(stlpec, riadok));
        
    }
    
    @Override
    public ImageIcon getIkona() {
        File file = new File(".");
        
        try {
        BufferedImage img;
        
            img = ImageIO.read(new File("src\\main\\java\\obrazky\\ciernyKamen.png"));
            
        Image dimgCierny = img.getScaledInstance(Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA,
        Image.SCALE_SMOOTH);
        
        return new ImageIcon(dimgCierny);
        
        
        } catch (IOException e) {
            System.out.println("Nepodarilo sa nacitat ciernu ikonu!");
        }
        
       
        return null;
    }
    
    @Override
    public Farba getFarba() {
        return Farba.CIERNY;
    }
}
