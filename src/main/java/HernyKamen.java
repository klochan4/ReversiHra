import javax.swing.*;

//abstraktna trieda dedi zo superklassy Kamen
public abstract class HernyKamen extends Kamen{

    //tento konstruktor sa zavola vtedy, ked sa vytvara instancia tried Biely a Cierny kamen
    public HernyKamen(Hrac hrac, Pozicia pozicia){
        super(hrac, pozicia);
    } //zavola sa konstruktor superklassy

    
    public abstract ImageIcon getIkona(); //tieto metody je nutne implementovat v podtriedach tejto triedy
    public abstract Farba getFarba();
}
