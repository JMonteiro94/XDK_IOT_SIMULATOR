/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;

import java.util.Random;

/**
 *
 * @author User
 */
public class XDK {
    
    public String getAcelerometro(){
        Random rand = new Random();
        int x = rand.nextInt(64000+1)+ (-32000);
        int y = rand.nextInt(64000+1)+ (-32000);
        int z = rand.nextInt(64000+1)+ (-32000);
        return ("Acelerometro | X: "+x+" | Y: "+y+"| Z :"+z+" |");
    }
    
    public String getGiroscopio(){
        Random rand = new Random();
        int x = rand.nextInt(64000+1)+ (-32000);
        int y = rand.nextInt(64000+1)+ (-32000);
        int z = rand.nextInt(64000+1)+ (-32000);
        return "Giroscopio |X:"+x+"| Y:"+y+"| Z:"+z;
    }
    
    public String getMagnetometro(){
        Random rand = new Random();
        int r= rand.nextInt(400+1)+ (-200);
        return "Magnetometro "+r+"nT";
    }
    
    public String getHumidade(){
        Random rand = new Random();
        int r= rand.nextInt(100+1)+ 0;
        return "Humidade"+r+"%";
    }
    
    public String getPressao(){
        Random rand = new Random();
        int r= rand.nextInt(1950)+ 870;
        return "Pressao "+r+"hPa";
    }
    
    public int getTemperatura(){
        int i=25;
        Random rand = new Random();
        int r= rand.nextInt(3)+ (-1);
        i+=r;
        return i;
    }
    
    public String getAcustica(){
        Random rand = new Random();
        int r= rand.nextInt(140+1)+ (0);
        return "Acustica "+r+"dB";
    }
    
    public int getLuz(){
        Random rand = new Random();
        int r= rand.nextInt(1100)+ 300;
        return r;
    }
}
