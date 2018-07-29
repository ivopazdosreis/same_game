/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SameGame;

import java.util.ArrayList;

/**
 *
 * @author Ivo Reis
 * @author Sérgio Vasconcelos
 */
public class Node {
    
    //private Bubble[][] board;
    int depth;
    private int value;
    private ArrayList<Coordenada> coords;
    
   /* public Bubble[][] get_board() {
        
        return board;
    }*/
    
    public int get_value() {
        
        return value;
    }
    
    public int get_depth() {
        
        return depth;
    }
    
    public ArrayList<Coordenada> get_coordenadas() {
        
        return coords;
    }
    /*
    public void set_board(Bubble[][] board) {
        
        this.board = board.clone();
    }
    */
    public void set_value(int value) {
        
        this.value = value;
    }
    
    public void set_depth(int depth) {
        
        this.depth = depth;
    }
    
    public void add_coordenada(Coordenada coord) {
        
        coords.add(coord);
        
    }
    
    
    
    public Node(int value, ArrayList<Coordenada> coords) {
       
        //this.board = board.clone();
        this.value = value;
        this.coords = coords;
    }
    
    public Node() {
        
        coords = new ArrayList<Coordenada>();        
        this.value = 0;
    }

}
