/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test;

import Utils.Matrix4F;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import opengl.test.object.endgame.endgame;
import opengl.test.object.CaroTable;
import opengl.test.object.GioiThieu;
import opengl.test.object.XO;
import opengl.test.object.cube.cube;
import opengl.test.object.lineCube;
import opengl.test.object.table.table;
import opengl.test.object.tivi.tivi;
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
    private GioiThieu gt;

    private cube c;
    private table table;
    private tivi tivi;
    
    private float CursorX = 0;
    private float DeltaX = 0;
    private float CursorY = 0;
    private float DeltaY = -180;
  
    private int Screen = 0;
    private float fScreen = 0;
    
    private float xControl=0;
    private float yControl=0;
    private float zControl=0;
    private float x1 = 0;
    private float x2 = 0;
    private float x3 = 0;
    private float x4 = 0;
    private float x5 = 0;
    private float x6 = 0;
    
    {
        Logger.getGlobal().setLevel(Level.FINEST);
        Handler h = new ConsoleHandler() {};
        h.setLevel(Level.FINEST);
        Logger.getGlobal().addHandler(h);
    }
   
    @Override
    protected void init(){
      //  GLFW.glfwSetInputMode(super.windowID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        this.CursorX = super.width/2;
        this.CursorY = super.height/2;
        this.xControl = 11.7f;
        
        
        this.vao = GL30.glGenVertexArrays(); 
        
        
        this.caro = new Caro(vao);
        this.caro.setProjectionMatrix(Matrix4F.PROJECTION(0.5f, -0.5f, -0.5f, 0.5f, 1f, 10));
        this.caro.setModelMatrix(Matrix4F.move(0, 0, 11.9f));

        this.gt = new GioiThieu(vao);
        this.gt.setProjectionMatrix(Matrix4F.PROJECTION(0.5f, -0.5f, -0.5f, 0.5f, 1f, 10));
        this.gt.setModelMatrix(Matrix4F.rotateOY(180).nhanMaTran(Matrix4F.move(0, 0, -11.9f)));
        
        this.c = new cube(vao,3.0f,3.0f,12.0f);
        this.c.setProjectionMatrix(Matrix4F.PROJECTION(0.5f, -0.5f, -0.5f, 0.5f, 1f, 10));
        
        this.table = new table(vao);
        this.table.setProjectionMatrix(Matrix4F.PROJECTION(0.5f, -0.5f, -0.5f, 0.5f, 1f, 10));
        this.table.setModelMatrix(Matrix4F.rotateOY(180).nhanMaTran(Matrix4F.move(2, -3, -10.6f)));

        this.tivi = new tivi(vao);
        this.tivi.setProjectionMatrix(Matrix4F.PROJECTION(0.5f, -0.5f, -0.5f, 0.5f, 1f, 10));
        this.tivi.setModelMatrix(Matrix4F.rotateOY(92.99915f).nhanMaTran(Matrix4F.move(1.8000002f, -1.9000002f, -10.600004f)));
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);

                
    }

    @Override
    protected void render(){
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT );
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT );

        Matrix4F view = Matrix4F.move(yControl, zControl, xControl).nhanMaTran(Matrix4F.rotateOY(this.DeltaY).nhanMaTran(Matrix4F.rotateOX(this.DeltaX)));

        this.c.setViewMatrix(view);
        this.caro.setViewMatrix(view);
        this.gt.setViewMatrix(view);
        this.table.setViewMatrix(view);
        this.tivi.setViewMatrix(view);
        
        this.gt.draw();
        this.c.render();
        this.table.render();
        this.tivi.render();
        caro.draw();
        
        
      
      
    }
 
    @Override
    protected void keyCallback(long arg0, int arg1, int arg2, int arg3, int arg4){
            if ( arg1 == GLFW.GLFW_KEY_LEFT && arg3 != GLFW.GLFW_RELEASE ){
                    this.caro.moveLeft();
            } else if ( arg1 == GLFW.GLFW_KEY_RIGHT && arg3 != GLFW.GLFW_RELEASE ){
                    this.caro.moveRight();
            } else if ( arg1 == GLFW.GLFW_KEY_UP && arg3 != GLFW.GLFW_RELEASE ){
                    this.caro.moveUp();
            } else if ( arg1 == GLFW.GLFW_KEY_DOWN && arg3 != GLFW.GLFW_RELEASE ){
                    this.caro.moveDown();
            } else if ( arg1 == GLFW.GLFW_KEY_ENTER && arg3 != GLFW.GLFW_RELEASE ){
                    this.caro.tic();
            } else if ( arg1 == GLFW.GLFW_KEY_W && arg3 != GLFW.GLFW_RELEASE ){
                //this.xControl+=0.1;
                this.xControl+= Math.cos(Math.toRadians(this.DeltaY))*0.1;
                this.yControl+= -Math.sin(Math.toRadians(this.DeltaY))*0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_S && arg3 != GLFW.GLFW_RELEASE ){
                this.xControl-= Math.cos(Math.toRadians(this.DeltaY))*0.1;
                this.yControl-= -Math.sin(Math.toRadians(this.DeltaY))*0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_A && arg3 != GLFW.GLFW_RELEASE ){
                this.yControl-=0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_D && arg3 != GLFW.GLFW_RELEASE ){
                this.yControl+=0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_Q && arg3 != GLFW.GLFW_RELEASE ){
                this.zControl-=0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_E && arg3 != GLFW.GLFW_RELEASE ){
                this.zControl+=0.1;
            } else if ( arg1 == GLFW.GLFW_KEY_1 && arg3 != GLFW.GLFW_RELEASE ){
                this.x1+=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_2 && arg3 != GLFW.GLFW_RELEASE ){
                this.x2+=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_3 && arg3 != GLFW.GLFW_RELEASE ){
                this.x3+=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_4 && arg3 != GLFW.GLFW_RELEASE ){
                this.x4+=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_5 && arg3 != GLFW.GLFW_RELEASE ){
                this.x5+=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_6 && arg3 != GLFW.GLFW_RELEASE ){
                this.x6+=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_Z && arg3 != GLFW.GLFW_RELEASE ){
                this.x1-=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_X && arg3 != GLFW.GLFW_RELEASE ){
                this.x2-=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_C && arg3 != GLFW.GLFW_RELEASE ){
                this.x3-=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_V && arg3 != GLFW.GLFW_RELEASE ){
                this.x4-=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_B && arg3 != GLFW.GLFW_RELEASE ){
                this.x5-=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            } else if ( arg1 == GLFW.GLFW_KEY_N && arg3 != GLFW.GLFW_RELEASE ){
                this.x6-=0.1;
                System.out.println(x1+":"+x2+":"+x3+":"+x4+":"+x5+":"+x6);
            }
    }



    @Override
    protected void cursorCallback(long arg0, double arg1, double arg2){

        if ( arg0 == super.windowID ){
            
            float tmp = (float) (this.DeltaY + (this.CursorX - arg1)/10);
            //if ( tmp <= 45 && tmp >= -45 )
                this.DeltaY = tmp;
            
            float tmp2 = (float) (this.DeltaX + (this.CursorY - arg2)/10);
            if ( tmp2 <= 90 && tmp2 >= -90 )
                this.DeltaX = tmp2;
            
        }
        GLFW.glfwSetCursorPos(windowID, super.width/2, super.height/2);
        
    }
    
    @Override
    protected void dispose()
    {
       this.caro.dispose();
       this.gt.dispose();
       this.c.dispose();
       this.table.deleteProgram();
       this.tivi.deleteProgram();
       
       
       GL30.glBindVertexArray(0);
       GL30.glDeleteVertexArrays(this.vao);

    }
    
    public static void main(String[] args){
        new Demo().init();

    }
}
