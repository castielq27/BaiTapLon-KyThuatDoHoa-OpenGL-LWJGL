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
import opengl.endgame.EndGameShow;
import opengl.endgame.dat.endgame;
import opengl.test.object.CaroTable;
import opengl.test.object.XO;
import opengl.test.object.image;
import opengl.test.object.lineCube;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author castiel
 */
public class Caro2 {

    
    
    private CaroTable table;
    private ArrayList<XO> lst = new ArrayList<>();
    private lineCube select;
    private int vao;
    
    private int[][] matrix;
    private int[] position = new int[2];
    
    public static int X = 0;
    public static int O = 1;
    public static int Z = -5;
    public static int ChuaKetThuc = -1;
    private int human = 0;
    private int computer = 1;
    private int winner = Caro2.ChuaKetThuc; // hòa = ChuaKetThuc, 
    
    private endgame status;
    private float alpha = 0;
    private float beta = -2;
    
    
    private Matrix4F Model;
    private Matrix4F Projection;
    
    public Caro2(int vao){
        this.vao = vao;
        this.table = new CaroTable(vao);
        this.select = new lineCube(vao,1,1);
        
        this.status  = new endgame(this.vao);
        
        this.matrix = new int[3][3];
        for ( int i = 0; i<3; i++ )
            for ( int j = 0; j<3; j++ )
                matrix[i][j] = Caro2.Z;
        this.position[0] = 1;
        this.position[1] = 1;
    }
    
    public void setXO(int o){
        if ( o != Caro2.X && o != Caro2.O )
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
        whoWin();
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
        if(winner != Caro2.ChuaKetThuc){// Ket thuc 
            System.out.println("Restart!");
            this.restart();
            return;
        }        
        if ( this.matrix[this.position[0]][this.position[1]] == Caro2.Z ){
            this.matrix[this.position[0]][this.position[1]] = this.human;
            XO xo = new XO(this.vao, this.human,this.position[0],this.position[1]);
            xo.setModelMatrix(Model);
            xo.setProjectionMatrix(this.Projection);
            lst.add(xo);    
            whoWin();
            this.AI();
        }

    }
    
    private void AI(){ // AI Skynet :v :v :V 
        if(winner != Caro2.ChuaKetThuc){
            return;
        }
        Random r = new Random();
        int x = -1;
        int y = -1;
        while( !this.isFull()){
            x = r.nextInt(3);
            y = r.nextInt(3);
            if ( this.matrix[x][y] == Caro2.Z )
                break;
        }
        if ( x != -1 && y != -1 ){
            this.matrix[x][y] = this.computer;
            XO xo = new XO(this.vao, this.computer,x,y);
            xo.setModelMatrix(Model);
            xo.setProjectionMatrix(this.Projection);
            lst.add(xo);    
            whoWin();
        }

    }
    
    public boolean isFull(){
        for ( int i = 0; i<3; i++ )
            for ( int j = 0; j<3; j++ )
                if ( this.matrix[i][j] == Caro2.Z )
                    return false;
        this.winner = Caro2.Z;
        return true;
        
    }
    
    public void setViewMatrix(Matrix4F v){
        this.status.setViewMatrix(v);
        this.table.setViewMatrix(v);
        this.select.setViewMatrix(v);
       
        for( XO e : lst ){
            e.setViewMatrix(v);
        }
    }

    public void setProjectionMatrix(Matrix4F p){
        this.Projection = p;
        this.status.setProjectionMatrix(p);
        this.table.setProjectionMatrix(p);
        this.select.setProjectionMatrix(p);
       
        for( XO e : lst ){
            e.setProjectionMatrix(p);
        }
    }

    public void setModelMatrix(Matrix4F m){
        this.Model = m;
        this.status.setModelMatrix(m);
        this.table.setModelMatrix(m);
        this.select.setModelMatrix(m);
       
        for( XO e : lst ){
            e.setModelMatrix(m);
        }
    }
    /**
     * Chua hoan thanh
     * @return nguoi win
     */
    private void whoWin(){
        for(int check = 0;check < 3;check++){
                if (matrix[0][check] == matrix[1][check] && matrix[1][check] == matrix[2][check] && matrix[0][check] != Caro2.Z) {
                    winner = matrix[0][check]; // winner ở trên
                    return;
                }
                if (matrix[check][0] == matrix[check][1] && matrix[check][1] == matrix[check][2] && matrix[check][0] != Caro2.Z) {
                    winner = matrix[check][0];
                    return;
                }
        }
        
        if(matrix[0][0] == matrix[1][1] && matrix[1][1] == matrix[2][2] && matrix[0][0] != -5){
            winner = matrix[0][0];
            return;
        }
        if(matrix[0][2] == matrix[1][1] && matrix[1][1] == matrix[2][0] && matrix[0][2] != -5){
            winner = matrix[0][2];
            return;
        }
        
        // sau mỗi lần người hoặc AI đánh sẽ kiểm tra  winner
    }
    
    public void draw(){
        if ( this.winner != Caro2.ChuaKetThuc ){
            if ( beta > 0.005 ){
                beta-=0.1;
            }
            if ( alpha >= 360 )
                alpha =0;
            else
                alpha+=1.5;
            
            this.status.setModelMatrix(Matrix4F.rotateOY(alpha).nhanMaTran(Matrix4F.move(0, 0, beta)).nhanMaTran(Model));
            if ( this.winner == this.human )
                this.status.render(endgame.win);
            else //( this.winner == this.computer )
                this.status.render(endgame.lose);
            //else 
              //  this.status.render(endgame.lose);
        } else {
            if ( beta < 2 ){
                beta+=0.1;
            }
            this.status.setModelMatrix(Matrix4F.rotateOY(alpha).nhanMaTran(Matrix4F.move(0, 0, beta)).nhanMaTran(Model));
            this.status.render(endgame.draw);
        }
        this.table.render();
        this.select.update(this.position[0], this.position[1]);
        this.select.render();
        
        for( XO e : lst ){
            e.render();
        }
        
    }
    
    public void restart(){
        for ( int i = 0; i<lst.size(); i++ ){
            lst.get(i).deleteProgram();
            lst.remove(i);
        }
        lst.clear();
        for ( int i = 0; i<3; i++ )
            for ( int j = 0; j<3; j++ )
                matrix[i][j] = Caro2.Z;
        this.position[0] = 1;
        this.position[1] = 1;
        this.winner = Caro2.ChuaKetThuc;
    }
    
    public void dispose(){
        this.status.deleteProgram();
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
