/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test;

import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;



public class OpenGL {
    
    protected int width = 600;
    protected int height = 800;
    protected String title = "Test";
    
    
    private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private GLFWKeyCallback keyCallback;
    private GLFWWindowSizeCallback windowSizeCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    
    protected long windowID = 0;
    
    
    public OpenGL() {
        GLFW.glfwSetErrorCallback(errorCallback);
        if ( GLFW.glfwInit() != GLFW.GLFW_TRUE ){
            throw new RuntimeException("Khong the khoi tao glfw");
        }
        
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);// su dung opengl phien ban 3.2 --> GLSL phien bn 1.50
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        
        this.windowID = GLFW.glfwCreateWindow(this.width, this.height, this.title,
                MemoryUtil.NULL, MemoryUtil.NULL);
        if ( this.windowID == MemoryUtil.NULL ){
            throw new RuntimeException("Khong the tao window");
        }
        
        GLFW.glfwMakeContextCurrent(windowID);
        GL.createCapabilities();
        GLFW.glfwSwapInterval(1);
        
        this.keyCallback = new GLFWKeyCallback(){
            @Override
            public void invoke(long arg0, int arg1, int arg2, int arg3, int arg4) {
                if ( arg1 == GLFW.GLFW_KEY_ESCAPE && arg3 != GLFW.GLFW_RELEASE ){
                    GLFW.glfwSetWindowShouldClose(windowID, GL11.GL_TRUE);
                }
                OpenGL.this.keyCallback(arg0, arg1, arg2, arg3, arg4);
            }
        };
        this.windowSizeCallback = new GLFWWindowSizeCallback(){
            @Override
            public void invoke(long arg0, int arg1, int arg2) {
                GL11.glViewport(0, 0, arg1, arg2);// view port 0,0 goc tre, arg1, arg2 goc duoi cung !
            }
            
        };
        this.cursorPosCallback = new GLFWCursorPosCallback(){
            @Override
            public void invoke(long arg0, double arg1, double arg2) {
                OpenGL.this.cursorCallback(arg0, arg1, arg2);
            }
            
        };
        GLFW.glfwSetWindowSizeCallback(windowID, windowSizeCallback);
        GLFW.glfwSetKeyCallback(windowID, keyCallback);
        GLFW.glfwSetCursorPosCallback(windowID, cursorPosCallback);
        try {
            this.init();
            this.loop();
        } catch (InterruptedException ex) {
            throw new RuntimeException("Loop !!!1");
        } 
        
    }
    
    
    
    private void loop() throws InterruptedException{
        long start;
        long end;
        long target = (long) (1000/33F);
        long result = 0;
        while ( GLFW.glfwWindowShouldClose(windowID) != GLFW.GLFW_TRUE ){
            start = (long) (GLFW.glfwGetTime()*1000);
            
            this.render();
            
            // event action key mouse ...
            GLFW.glfwPollEvents();
            // Swap buffer
            GLFW.glfwSwapBuffers(windowID);
            
            end = (long) (GLFW.glfwGetTime()*1000);// fps limit
            result = (start + target - end );
            if ( result >= 0 )        
                Thread.sleep(start + target - end);
           
            
        }
        this.dispose();
        this.release();
    }
    
    /**
     * Override sau nay
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4 
     */
    protected void keyCallback(long arg0, int arg1, int arg2, int arg3, int arg4){
        
    }
    /**
     * 
     */
    protected void cursorCallback(long arg0, double arg1, double arg2){
        
    }
    private void release(){
        GLFW.glfwDestroyWindow(windowID);
        GLFW.glfwTerminate();
        this.errorCallback.release();
    }

    /**
     * Function khoi tao
     */
    protected void init(){
        
    }
    /**
     * Function ket thuc 
     * Override khi extends
     */
    protected void dispose(){
        
    }
    
    /**
     * Function thuc thi trong vong loop game !!!
     * Override khi extends
     */
    protected void render(){
        
    }

    
    public static void main(String[] args) throws InterruptedException {
        new OpenGL();
    }
}
