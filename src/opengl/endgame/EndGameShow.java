/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.endgame;
import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Matrix4f;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author l
 */
public class EndGameShow{
    public static String win = "resource/win.png";
    public static String lose = "resource/lose.png";
    public static String draw = "resource/draw.png";
    
    private String path;
    
    private int vao;
    
    
    private Model model;
    private Shader shader;
    private int red = 1;
    private int green = 0;
    private int blue = 0;
    private int alpha = 0;
          
    private int i = 1;
    private int flag = 0;
    private double angdeg = 0f;
    private float increase = -0.5f;
    private float scale = 1;
    
    private float[] vertices;
    private float[] texture;
    private int[] indices;
    private Textture tex;
    
    
    public EndGameShow(int vao){
        this.vao = vao;
        this.init();
    }
    
    public EndGameShow(String path,int vao){
        this.vao = vao;
        this.path = path;
        this.init();
    }
    
    public void init(){
    
        tex = new Textture(new File(this.path));
        float c_r = 1;
        float c_b = 0;
        //glEnable(GL_TEXTURE_2D);
        

        vertices = new float[]{
                                -0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
        };
        texture = new float[]{
                                0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0
        };
        
        indices = new int[]{
                                0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

        };
        model = new Model(vertices,texture,indices,this.vao);
        shader = new Shader("shader");


    }
    

    public void render(){


            angdeg += 0.04f;
            
            if(flag == 0){
                i++;
                scale += 0.0002f;
                increase += 0.0002f;
            }else{
                i--;
                scale -= 0.0002f;
                increase -= 0.0002f;
            }
            if(i == 10000){
                flag = 1;
            }
            if(i == 0){
                flag = 0;
            }
            
            
           if(i % 400 == 0){
                Random r = new Random();
            
            red = r.nextInt(2);
            green = r.nextInt(2);
            blue = r.nextInt(2);
            alpha = r.nextInt(2);
            if(red == green && red == blue && red == 1){
                red = 0;
            }
        }
            
            //Ma trận quay theo trục XYZ
            Matrix4f rotateMatrix = new Matrix4f().rotateXYZ((float)Math.toRadians(angdeg),(float)Math.toRadians(angdeg),(float)Math.toRadians(angdeg)); 
            //Ma trận tỉ lệ
            Matrix4f scaleMatrix = new Matrix4f().scale((float)(scale/3));
            //Ma trận tịnh tiến
            Matrix4f translateMatrix = new Matrix4f().translate(increase-1,0,0);
            
            
            Matrix4f transportationMatrix = new Matrix4f();
            rotateMatrix.mul(scaleMatrix,transportationMatrix);
            transportationMatrix.mul(translateMatrix,transportationMatrix);
             
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("red", red);
            shader.setUniform("green", green);
            shader.setUniform("blue", blue);
            shader.setUniform("alpha", alpha);
            shader.setUniform("transportationMatrix", transportationMatrix);
            shader.setUniform("increase",increase);
            tex.bind(0);
            
            
            model.render();


    }
    
    
    public void deleteProgram(){
        // disable program
        shader.deleteShader();
        
        this.model.deleteModel();
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public static void main(String[] args) {

    }
}
