
import java.time.LocalDateTime;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zack
 */
public class Expiry {

    public Expiry() {
    }
    
    protected LocalDateTime getExpiry() {
        LocalDateTime current = LocalDateTime.now();
        return current.plusHours(1);
    }
}
