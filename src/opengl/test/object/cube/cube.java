/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object.cube;

import Utils.Matrix4F;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author castiel
 */
public class cube {
    
    private float x;
    private float y;
    private float z;
    private int vao;
    private wall wall1;
    private wall wall2;
    private wall wall3;
    private wall wall4;
    private wall wall5;
    private wall wall6;
    /*private cube2 cube2;
    private cube3 cube3;
    private cube4 cube4;*/
    
    public cube(int vao,float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.vao =vao;

        this.wall1 = new wall(vao,x,y,"resource/wall.png",2);
        this.wall1.setModelMatrix(Matrix4F.move(0, 0, -z));
        this.wall2 = new wall(vao,x,y,"resource/wall.png",2);
        this.wall2.setModelMatrix(Matrix4F.move(0, 0, z));
        
        
        
        this.wall3 = new wall(vao,x,z,"resource/Ceiling.png",0.5f);
        this.wall3.setModelMatrix(Matrix4F.rotateOX(90).nhanMaTran(Matrix4F.move(0, y, 0)));
        
        this.wall4 = new wall(vao,x,z,"resource/flood.png",2);
        this.wall4.setModelMatrix(Matrix4F.rotateOX(90).nhanMaTran(Matrix4F.move(0, -y, 0)));

        this.wall5 = new wall(vao,z,y,"resource/wall.png",2);
        this.wall5.setModelMatrix(Matrix4F.rotateOY(90).nhanMaTran(Matrix4F.move(x, 0, 0)));

        this.wall6 = new wall(vao,z,y,"resource/wall.png",2);        
        this.wall6.setModelMatrix(Matrix4F.rotateOY(90).nhanMaTran(Matrix4F.move(-x, 0, 0)));
        
    }
    
   
    
    public void deleteProgram(){
        
        wall1.deleteProgram();
        wall2.deleteProgram();
        wall3.deleteProgram();
        wall4.deleteProgram();
        wall5.deleteProgram();
        wall6.deleteProgram();
    }

   
    
    public void setModelMatrix(Matrix4F m){
        wall1.setModelMatrix(m);
        wall2.setModelMatrix(m);
        wall3.setModelMatrix(m);
        wall4.setModelMatrix(m);
        wall5.setModelMatrix(m);
        wall6.setModelMatrix(m);
    }
    
    public void setViewMatrix(Matrix4F v){
        wall1.setViewMatrix(v);
        wall2.setViewMatrix(v);
        wall3.setViewMatrix(v);
        wall4.setViewMatrix(v);
        wall5.setViewMatrix(v);
        wall6.setViewMatrix(v);
    }

    public void setProjectionMatrix(Matrix4F p){
        wall1.setProjectionMatrix(p);
        wall2.setProjectionMatrix(p);
        wall3.setProjectionMatrix(p);
        wall4.setProjectionMatrix(p);
        wall5.setProjectionMatrix(p);
        wall6.setProjectionMatrix(p);
    }

    
    
    
    public void render(){
        wall1.render();
        wall2.render();
        wall3.render();
        wall4.render();
        wall5.render();
        wall6.render();
    }
    
    public void dispose(){
        wall1.deleteProgram();
        wall2.deleteProgram();
        wall3.deleteProgram();
        wall4.deleteProgram();
        wall5.deleteProgram();
        wall6.deleteProgram();
    }
    
    

}

