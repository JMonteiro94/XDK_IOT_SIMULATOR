/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;

/**
 *
 * @author User
 */
public class SubirEstoreCommand implements Command{
    
    private Estore estore;

        public SubirEstoreCommand(Estore e) {
            this.estore = e;
        }

        @Override
        public void execute() {
            estore.on();
        }
}
