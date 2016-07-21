/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object;

import Utils.Matrix4F;
import opengl.test.Caro;

/**
 *
 * @author castiel
 * mulitple objects
 * 
 */
public class GioiThieu {
    private image image;// anh gioi thieu
    private XO x;  // X object rotate
    private XO o; // O object
    private int vao;
    
    private Matrix4F Model = new Matrix4F();
    private Matrix4F Projection = new Matrix4F();
    
    private int rotateY = 0;
    
    public GioiThieu(int vao){
        this.vao = vao;
        this.image = new image(vao,0.5f,0.5f,0f,"resource/gioithieu.png");
        this.x = new XO(vao,Caro.X);
        this.o = new XO(vao,Caro.O);
    }
    
    private void chuyendong(){
        this.rotateY++;
        if ( this.rotateY >= 360 )
            this.rotateY = 0;
        Matrix4F x = Matrix4F.rotateOY(this.rotateY).nhanMaTran(Matrix4F.move(0.5f, 0.5f, -0.5f)).nhanMaTran(Model);
        this.x.setModelMatrix(x);
        Matrix4F o = Matrix4F.rotateOY(this.rotateY).nhanMaTran(Matrix4F.move(0.5f, 0.5f, -0.5f)).nhanMaTran(Model);
        this.o.setModelMatrix(o);

    }
    
    public void setModelMatrix(Matrix4F m){
        this.Model = m;
        this.image.setModelMatrix(m);
    }
    
    public void setProjectionMatrix(Matrix4F p){
        this.image.setProjectionMatrix(p);
        this.x.setProjectionMatrix(p);
        this.o.setProjectionMatrix(p);
    }
    
    public void setViewMatrix(Matrix4F v){
        this.image.setViewMatrix(v);
        this.x.setViewMatrix(v);
        this.o.setViewMatrix(v);
    }
    
    public void draw(){
        this.chuyendong();
        this.image.render();
        this.x.render();
        this.o.render();
    }
    public void dispose(){
        this.image.deleteProgram();
        this.x.deleteProgram();
        this.o.deleteProgram();
    }
}
