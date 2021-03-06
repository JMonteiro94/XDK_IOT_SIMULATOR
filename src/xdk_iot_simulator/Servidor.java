/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class Servidor extends Thread{
    
    public static void main(String[] args) {
        
        ArrayList<Command> lista = new ArrayList<>();
        HashMap<String, ArrayList> programar = new HashMap<>();
        Estore es = new Estore("Alumínio Térmico");
        Lampada la = new Lampada("Incandescente Halógena");
        ArCondicionado ac = new ArCondicionado("Window Split");
        DescerEstoreCommand descerEstore = new DescerEstoreCommand(es);
        DesligarArCommand desligarAr = new DesligarArCommand(ac);
        DesligarLampadaCommand desligarLampada = new DesligarLampadaCommand(la);
        lista.add(new LigarArCommand(ac));
        programar.put("16",lista);
        lista=new ArrayList<>();
        lista.add(new LigarLampadaCommand(la));
        programar.put("17",lista);
        lista=new ArrayList<>();
        lista.add(new SubirEstoreCommand(es));
        programar.put("18",lista);
        Controller con = new Controller(programar,ac,la,es);
        desligarLampada.execute();
        descerEstore.execute();
        desligarAr.execute();
        Thread t = new Thread(con);
        t.start();
    }
}
