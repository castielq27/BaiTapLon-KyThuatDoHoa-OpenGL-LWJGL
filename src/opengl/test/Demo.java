/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test;

import opengl.test.object.tamgiac;
import Utils.Matrix4F;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import opengl.test.object.CaroTable;
import opengl.test.object.XO;
import opengl.test.object.cube;
import opengl.test.object.lineCube;
import opengl.test.object.tugiac;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author castiel
 */
public class Demo extends OpenGL {
    
    private int vao = 0;
    private int vbo = 0;

    private Caro caro;
    
    private float CursorX = 0;
    private float DeltaX = 0;
    private float CursorY = 0;
    private float DeltaY = 0;
  
    private float zNear = 0;
    private float zFar = 0;
    
    {
        Logger.getGlobal().setLevel(Level.FINEST);
        Handler h = new ConsoleHandler() {};
        h.setLevel(Level.FINEST);
        Logger.getGlobal().addHandler(h);
    }
   
    @Override
    protected void init(){
        
        this.vao = GL30.glGenVertexArrays(); 
        this.caro = new Caro(vao);
        

        GL11.glEnable(GL11.GL_DEPTH_TEST);
                
    }
    
    @Override
    protected void render(){
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
        
        Matrix4F v = Matrix4F.rotateOX(this.DeltaX).nhanMaTran(Matrix4F.rotateOY(this.DeltaY));
        caro.setViewMatrix(v);

        caro.draw();

        
    }
 
    @Override
    protected void keyCallback(long arg0, int arg1, int arg2, int arg3, int arg4){
            if ( arg1 == GLFW.GLFW_KEY_UP && arg3 != GLFW.GLFW_RELEASE ){
                this.caro.moveUp();
            } else if ( arg1 == GLFW.GLFW_KEY_DOWN && arg3 != GLFW.GLFW_RELEASE ){
                this.caro.moveDown();
            } else if ( arg1 == GLFW.GLFW_KEY_LEFT && arg3 != GLFW.GLFW_RELEASE ){
                this.caro.moveLeft();
            } else if ( arg1 == GLFW.GLFW_KEY_RIGHT && arg3 != GLFW.GLFW_RELEASE ){
                this.caro.moveRight();
            } else if ( arg1 == GLFW.GLFW_KEY_ENTER && arg3 != GLFW.GLFW_RELEASE ){
                this.caro.tic();
            }/* else if ( arg1 == GLFW.GLFW_KEY_PAGE_DOWN && arg3 != GLFW.GLFW_RELEASE ){
                x+=1;
            } else if ( arg1 == GLFW.GLFW_KEY_F1 && arg3 != GLFW.GLFW_RELEASE ){
                this.zNear+=0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_F2 && arg3 != GLFW.GLFW_RELEASE ){
                this.zNear-=0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_F5 && arg3 != GLFW.GLFW_RELEASE ){
                this.zFar+=0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_F6 && arg3 != GLFW.GLFW_RELEASE ){
                this.zFar-=0.1;
            }*/
    }
    
    @Override
    protected void cursorCallback(long arg0, double arg1, double arg2){
        System.out.println(arg0+":"+arg1+":"+arg2);
        if ( arg0 == super.windowID ){
            
            float tmp = (float) (this.DeltaY + this.CursorX - arg1);
            if ( tmp <= 90 && tmp >= -90 )
                this.DeltaY = tmp;
            
            tmp = (float) (this.DeltaX + this.CursorY - arg2);
            if ( tmp <= 90 && tmp >= -90 )
                this.DeltaX = tmp;
            
            this.CursorX = (float) arg1;
            this.CursorY = (float) arg2;
        }
//        GLFW.glfwSetCursorPos(windowID, super.width/2, super.height/2);
    }
    
    @Override
    protected void dispose()
    {
        this.caro.dispose();

    }
    
    public static void main(String[] args){
        new Demo().init();
    }
}
