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
        HashMap<String, Command> history = new HashMap<>();
        ArrayList<Command> lista = new ArrayList<>();
        HashMap<String, ArrayList> programar = new HashMap<>();
        String hora="4:00";
        Estore es = new Estore("Alumínio Térmico");
        Lampada la = new Lampada("Incandescente Halógena");
        ArCondicionado ar = new ArCondicionado("Window Split");
        DescerEstoreCommand a = new DescerEstoreCommand(es);
        DesligarArCommand b = new DesligarArCommand(ar);
        DesligarLampadaCommand c = new DesligarLampadaCommand(la);
        lista.add(new LigarArCommand(ar));
        lista.add(new SubirEstoreCommand(es));
        programar.put(hora,lista);
        history.put("OffEstore",a);
        history.put("OffAr",b);
        history.put("OffLampada",c);
        Controller con = new Controller(history,programar,ar,la,es);
        con.executeCommand("OffEstore");
        con.executeCommand("OffAr");
        con.executeCommand("OffLampada");
        Thread t = new Thread(con);
        t.start();
    }
}
