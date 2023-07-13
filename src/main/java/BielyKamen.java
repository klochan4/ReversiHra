import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//trieda dedi z superclassy HernyKamen
public class BielyKamen extends HernyKamen{
    public BielyKamen(Hrac paHrac, Pozicia pozicia){
        super(paHrac, pozicia); //musi sa zavolat konstruktor superclassy
        
    }
    
    public BielyKamen(Hrac hrac, int riadok, int stlpec){ //overloading konstruktora, su pouzite ine parametre
        super(hrac, new Pozicia(stlpec, riadok));
        
    }

    @Override
    public ImageIcon getIkona() { //metoda je overridnuta, je ju nutne implementovat, kedze je zdedena z abstraktnej triedy
        File file = new File(".");
        
        try {
        BufferedImage imgBiely;
        
            imgBiely = ImageIO.read(new File("src\\main\\java\\obrazky\\bielyKamen.png")); //nacitanie ikonky zo suboru
            
        Image dimgBiely = imgBiely.getScaledInstance(Konstanty.ROZMER_POLICKA, Konstanty.ROZMER_POLICKA,
        Image.SCALE_SMOOTH);
        
        return new ImageIcon(dimgBiely); //vrati sa ikonka bieleho kamena
        } catch (IOException e) {
            System.out.println("Nepodarilo sa nacitat bielu ikonu!");
        }
        
        return null;
    }

    @Override
    public Farba getFarba() {
        return Farba.BIELY;
    } //vrati sa biela farba, taktiez bolo nutne tuto metodu implementovat v tejto triede
}
