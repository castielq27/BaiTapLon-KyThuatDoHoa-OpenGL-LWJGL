/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test;

import Utils.Matrix4F;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import opengl.test.object.CaroTable;
import opengl.test.object.XO;
import opengl.test.object.lineCube;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author castiel
 */
public class Caro {

    
    
    private CaroTable table;
    private ArrayList<XO> lst = new ArrayList<>();
    private lineCube select;
    private int vao;
    
    private int[][] matrix;
    private int[] position = new int[2];
    
    public static int X = 0;
    public static int O = 1;
    public static int Z = -5;
    private int human = 0;
    private int computer = 1;
    
    
    private int end = Caro.Z;// Z chua ket thuc, X, O win
    
    public Caro(int vao){
        this.vao = vao;
        this.table = new CaroTable(vao);
        this.select = new lineCube(vao,1,1);
        
        this.matrix = new int[3][3];
        for ( int i = 0; i<3; i++ )
            for ( int j = 0; j<3; j++ )
                matrix[i][j] = Caro.Z;
        this.position[0] = 1;
        this.position[1] = 1;
    }
    
    public void setXO(int o){
        if ( o != Caro.X && o != Caro.O )
            return;
        this.human = o;
        this.computer = 1 - o;
    }
    
    public void moveUp(){
        if ( this.position[0] > 0 )
        this.position[0]--;
    }
    
    public void moveDown(){
        if ( this.position[0] < 2 )
        this.position[0]++;        
    }
    
    public void moveLeft(){
        if ( this.position[1] > 0 )
        this.position[1]--;                
    }
    
    public void moveRight(){
        if ( this.position[1] < 2 )
        this.position[1]++;
    }
    
    public void tic(){
        if ( this.matrix[this.position[0]][this.position[1]] == Caro.Z ){
            this.matrix[this.position[0]][this.position[1]] = this.human;
            XO xo = new XO(this.vao, this.human,this.position[0],this.position[1]);
            lst.add(xo);            
            this.AI();
        }

    }
    
    private void AI(){ // AI Skynet :v :v :V 
        Random r = new Random();
        int x = -1;
        int y = -1;
        while( !this.isFull() ){
            x = r.nextInt(3);
            y = r.nextInt(3);
            if ( this.matrix[x][y] == Caro.Z )
                break;
            System.out.println(x+"::"+y);
        }
        if ( x != -1 && y != -1 ){
            this.matrix[x][y] = this.computer;
            XO xo = new XO(this.vao, this.computer,x,y);
            lst.add(xo);                        
        }

    }
    
    private boolean isFull(){
        for ( int i = 0; i<3; i++ )
            for ( int j = 0; j<3; j++ )
                if ( this.matrix[i][j] == Caro.Z )
                    return false;
        return true;
        
    }
    
    public void setViewMatrix(Matrix4F v){
        this.table.setViewMatrix(v);
        this.select.setViewMatrix(v);
       
        for( XO e : lst ){
            e.setViewMatrix(v);
        }
    }
    /**
     * Chua hoan thanh
     * @return nguoi win
     */
    private int whoWin(){
        int[] t = {0,0,0,0,0,0,0,0};
        return 0;
        
    }
    
    public void draw(){
        this.table.render();
        this.select.update(this.position[0], this.position[1]);
        this.select.render();
        
        for( XO e : lst ){
            e.render();
        }
    }
    
    public void restart(){
        
    }
    
    public void dispose(){
        this.table.deleteProgram();
        this.select.deleteProgram();
        for( XO e : lst ){
            e.deleteProgram();
        }        
        lst.clear();
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
      
    }
    
}
