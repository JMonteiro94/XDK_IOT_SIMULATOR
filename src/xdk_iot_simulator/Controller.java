/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author User
 */
public class Controller extends Thread{
    
    private final ArrayList<Command> historico;
    private HashMap<String, ArrayList> programar = new HashMap<>();
    private final XDK xdk = new XDK();
    private final ArCondicionado ac;
    private final Estore es;
    private final Lampada la;
    private Timer timer;
    private final DesligarArCommand desligarAr;
    private final LigarArCommand ligarAr;
    private final DesligarLampadaCommand desligarLampada;
    private final LigarLampadaCommand ligarLampada;
    private final SubirEstoreCommand subirEstore;
    private final DescerEstoreCommand descerEstore;
    
    public Controller(HashMap<String, ArrayList> programar, ArCondicionado ar, Lampada la, Estore es){
        this.historico = new ArrayList<>();
        this.programar=programar;
        this.ac=ar;
        this.es=es;
        this.la=la;
        this.desligarAr = new DesligarArCommand(ar);
        this.ligarAr = new LigarArCommand(ar);
        this.desligarLampada = new DesligarLampadaCommand(la);
        this.ligarLampada = new LigarLampadaCommand(la);
        this.descerEstore = new DescerEstoreCommand(es);
        this.subirEstore = new SubirEstoreCommand(es);
    }
    
    class Auto extends TimerTask {
        
        ArrayList<Command> comandos;
        int temp,luz;
        boolean blocked;
        ArCondicionado ac;
        Estore es;
        Lampada la;
         
        public Auto(ArrayList<Command> c,int temp,int luz,ArCondicionado ar, Lampada la, Estore es){
            this.comandos=c;
            this.temp=temp;
            this.luz=luz;
            this.ac=ar;
            this.es=es;
            this.la=la;
        }
        public Auto(ArCondicionado ar, Lampada la, Estore es){    
            this.ac=ar;
            this.es=es;
            this.la=la;
        }
        public void set(boolean c){this.blocked=c;}
        
        @Override
        public void run() {
            boolean done=true;
            int count=0;
            try{
                while(true && done && count < 5){
                luz=xdk.getLuz();
                temp=xdk.getTemperatura();
                System.out.println("Auto_XDK-- Temp: "+temp+" Luz: "+luz);
                    Command element = comandos.get(0);
                    if(element instanceof LigarArCommand){
                        if(this.temp < 30){
                            count++;
                            Thread.sleep(5*60*1000);
                        }
                        else{
                            element.execute();
                            done=false;
                            this.ac.estado=1;
                        }
                    }
                    if(element instanceof SubirEstoreCommand){
                        if(this.temp < 30){
                           element.execute();
                           done=false;
                           this.es.estado=1;
                        }
                        else{
                            count++;
                            Thread.sleep(5*60*1000);
                        }
                    }
                    if(element instanceof LigarLampadaCommand){
                        if(this.luz < 700){
                            element.execute();
                            done=false;
                            this.la.estado=1;
                        }
                        else{
                            count++;
                            Thread.sleep(5*60*1000);
                        }
                    }
                }
                set(false);
            }catch(InterruptedException e){    
                System.out.println("got interrupted!");
            }
        }
    }
    
    @Override
    public void run() {
        int temp,luz,hora1;
        boolean done=false,ok=true;
        ArrayList <Command> aux;
        Date date,date1;
        SimpleDateFormat h = new SimpleDateFormat("kk");
        SimpleDateFormat m = new SimpleDateFormat("mm");
        SimpleDateFormat s = new SimpleDateFormat("ss");
        String hora,minutos,segundos;
        Auto auto = new Auto(ac,la,es);
        auto.set(false);
        try{
            while(true){
                luz=xdk.getLuz();
                temp=xdk.getTemperatura();
                date = new Date();
                hora=h.format(date);
                minutos=m.format(date);
                segundos=s.format(date);
                if(segundos.equals("00") && minutos.equals("00") && ok){ok=false;}
                if( auto.blocked==false && !ok){
                    aux = programar.get(hora);
                    auto = new Auto(aux,temp,luz,ac,la,es);
                    hora1=Integer.parseInt(hora);
                    date1=new Date();
                    date1.setHours(hora1);
                    date1.setMinutes(00);
                    date1.setSeconds(0);
                    ok=true;
                    timer = new Timer();
                    if(!aux.isEmpty() && !done){
                        System.out.println("Atuador pré-programado ativado ...");
                        timer.schedule(auto,date1,1000 * 60 * 60 * 24);
                        done=true;
                        auto.set(true);
                    }
                }
                if(!auto.blocked){
                    System.out.println("XDK-- Temp: "+temp+" Luz: "+luz+" Hora: "+hora+":"+minutos);
                    if(temp >= 30 && ac.estado==0 && es.estado==0 ){             
                        historico.add(ligarAr);
                        ligarAr.execute();
                        ac.estado=1;
                    }
                    else{
                        if(temp >= 30 && ac.estado==0 && es.estado==1){
                            ligarAr.execute();
                            descerEstore.execute();
                            historico.add(ligarAr);
                            historico.add(descerEstore);
                            ac.estado=1;
                            es.estado=0;
                        }
                    }
                    if(temp <= 20 && ac.estado==1 ){
                        desligarAr.execute();
                        historico.add(desligarAr);
                        ac.estado=0;
                    }
                    if(luz < 500 && es.estado==1 && la.estado==0){
                        ligarLampada.execute();
                        historico.add(ligarLampada);
                        la.estado=1;
                    }else{
                        if(temp < 30 && luz < 500 && es.estado==0 && la.estado==0){
                            subirEstore.execute();
                            historico.add(subirEstore);
                            es.estado=1;
                        }
                    }   
                    if(luz > 700 && la.estado==1){
                        desligarLampada.execute();
                        historico.add(desligarLampada);
                        la.estado=0;
                    }
                    Thread.sleep(5*60*1000);
                }
            }
        }catch(InterruptedException e){    
            System.out.println("got interrupted!");
        }
    }
}
