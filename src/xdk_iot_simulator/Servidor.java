/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;

import java.util.HashMap;

/**
 *
 * @author User
 */
public class Servidor extends Thread{
    
    public static void main(String[] args) {
        HashMap<String, Command> history = new HashMap<>();
        Estore es = new Estore();
        Lampada la = new Lampada();
        ArCondicionado ar = new ArCondicionado();
        DescerEstoreCommand a = new DescerEstoreCommand(es);
        DesligarArCommand b = new DesligarArCommand(ar);
        DesligarLampadaCommand c = new DesligarLampadaCommand(la);
        history.put("OffEstore",a);
        history.put("OffAr",b);
        history.put("OffLampada",c);
        Controller con = new Controller(history,ar,la,es);
        con.executeCommand("OffEstore");
        con.executeCommand("OffAr");
        con.executeCommand("OffLampada");
        Thread t = new Thread(con);
        t.start();
    }
}
