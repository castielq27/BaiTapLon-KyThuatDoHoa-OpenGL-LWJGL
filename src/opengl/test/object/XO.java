/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object;

import Utils.objLoad;
import Utils.Matrix4F;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utils.objLoad;
import java.io.FileNotFoundException;
import opengl.test.Caro;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author castiel
 */
public class XO {


    private int programID;
    private int vertexID;
    private int fragmentID;
    
    private int vao;
    private int vbo;
    private int ebo;
    
    private FloatBuffer VertexColorBuffer;

    private IntBuffer indices;
    
    private int modelID;
    private int viewID;
    private int projectionID;
    
    private int value;
    
    private int x;// vi tri tren ban co caro
    private int y;
    
    public XO(int vao,int XO, int x, int y ){
        Logger.getGlobal().entering("tugiac", "tugiac", new Object[]{vao,XO} );

        if ( XO != Caro.X && XO != Caro.O )
            throw new IllegalArgumentException("XO != Caro.X && XO != Caro.Y");
        if ( vao == 0 )
            throw new IllegalArgumentException("init : paramaters is null ");

        this.x = x;
        this.y = y;
        
        this.vao = vao;
        this.value = XO;
        
        this.programID = GL20.glCreateProgram();
        this.vertexShader("opengl/test/object/XO.vs");
        this.fragmentShader("opengl/test/object/XO.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    private void vertexShader(String file){
        this.vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(this.vertexID, XO.sourceLoader(file));
        GL20.glCompileShader(this.vertexID);
        if ( GL20.glGetShaderi(this.vertexID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile vertexShader");
        }
        GL20.glAttachShader(this.programID, this.vertexID);
    }

    private void fragmentShader(String file){
        this.fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(this.fragmentID, XO.sourceLoader(file));
        GL20.glCompileShader(this.fragmentID);
        if ( GL20.glGetShaderi(this.fragmentID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile fragmentShader");
        }
        GL20.glAttachShader(this.programID, this.fragmentID);        
        
    }
    private static String sourceLoader(String file){
        Scanner in = new Scanner( tamgiac.class.getClassLoader().getResourceAsStream(file));
        StringBuilder source = new StringBuilder("");
        while( in.hasNextLine() ){
            source.append(in.nextLine() + "\n");
        }
        return source.toString();
    }
    
    private void link(){
        GL20.glLinkProgram(programID);
        if ( GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the link program");
        }
    }
    
    public void bind(){
        GL20.glUseProgram(programID);
    }
    
    public void unbind(){
        GL20.glUseProgram(0);
    }
    
    public void deleteProgram(){
        // disable program
        this.unbind();
        
        // detach shader
        GL20.glDetachShader(this.programID, this.vertexID);
        GL20.glDetachShader(this.programID, this.fragmentID);
        // delete shader
        GL20.glDeleteShader(this.vertexID);
        GL20.glDeleteShader(this.fragmentID);
        GL20.glDeleteProgram(this.programID);
        
        
        // delete vao
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(this.vao);
        // delete vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.vbo);
       
        //delete vbo
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.ebo);
        
    }
    public int getProgramID(){
        return this.programID;
    }
    
    private void initVertex(){
        if ( this.value == Caro.X)
            this.initVertexX();
        else if ( this.value == Caro.O )
            this.initVertexO();
    }
    
    private void initVertexX(){
        GL30.glBindVertexArray(vao);//bind vao  
        
        
        this.VertexColorBuffer = objLoad.VertexColorLoad(XO.class.getClassLoader().getResourceAsStream("opengl/test/object/X.obj"));
        
        Logger.getGlobal().log(Level.SEVERE, "FloatBuffer capacity  : " + VertexColorBuffer.capacity() );

        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo); 
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorBuffer, GL15.GL_STATIC_DRAW);
        
        this.VertexAttribPointer();

        this.indices = objLoad.indicesLoad(XO.class.getClassLoader().getResourceAsStream("opengl/test/object/X.obj"));
        
        this.ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_DYNAMIC_DRAW);
        
        
        GL30.glBindVertexArray(0);//unbind vao
    }
    
    
    private void initVertexO(){
        GL30.glBindVertexArray(vao);//bind vao  
        
        this.VertexColorBuffer = objLoad.VertexColorLoad(XO.class.getClassLoader().getResourceAsStream("opengl/test/object/O.obj"));

        Logger.getGlobal().log(Level.SEVERE, "FloatBuffer capacity  : " + VertexColorBuffer.capacity() );

        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo); 
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorBuffer, GL15.GL_STATIC_DRAW);
        
        this.VertexAttribPointer();

        this.indices = objLoad.indicesLoad(XO.class.getClassLoader().getResourceAsStream("opengl/test/object/O.obj"));
        
        this.ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_DYNAMIC_DRAW);
        
        
        GL30.glBindVertexArray(0);//unbind vao
    }
    
    
    /**
     * Method nay duoc tach rieng ra tu initVertex 
     * Vi render nhieu doi tuong neu dung Multi vbo --> truoc khi draw phai bind lai vbo --> moi lan bind lai se phai set lai VertexAttribPointer
     */
    private void VertexAttribPointer(){
        if ( this.vbo == 0 )
            throw new RuntimeException("Chua khoi tao VBO");
        
        int positionID = GL20.glGetAttribLocation(programID, "position");
        GL20.glEnableVertexAttribArray(positionID);
        GL20.glVertexAttribPointer(positionID, 3, GL11.GL_FLOAT, false, 6*4, 0);// float size = 4 byte --> next is 3*4 byte
                                            // size = 3 --> position = x,y,z vec3

        int colorID = GL20.glGetAttribLocation(programID, "color");
        GL20.glEnableVertexAttribArray(colorID);
        GL20.glVertexAttribPointer(colorID, 3, GL11.GL_FLOAT, false, 6*4, 3*4);
                                             // size = 3 --> vec3

    }

    private void initUniformValues(){
        this.bind();

        modelID = GL20.glGetUniformLocation(this.getProgramID(), "model");
        float x_calc=0;
        float y_calc=0;
        if ( x == 0 )
            y_calc = 0.3f;
        if ( x == 1 )
            y_calc = 0;
        if ( x == 2 )
            y_calc = -0.3f;
        if ( y == 0 )
            x_calc = -0.3f;
        if ( y == 1 )
            x_calc = 0f;
        if ( y == 2 )
            x_calc = 0.3f;
                    
        Matrix4F m = Matrix4F.ratio(0.10f, 0.10f, 0.10f).nhanMaTran(Matrix4F.rotateOX(90)).nhanMaTran(Matrix4F.move(x_calc, y_calc, -0.0125f));
        GL20.glUniformMatrix4fv(modelID, false, m.toFloatBuffer());
        
        viewID = GL20.glGetUniformLocation(this.getProgramID(), "view");
        Matrix4F v = new Matrix4F();
        GL20.glUniformMatrix4fv(viewID, false, v.toFloatBuffer());
        
        projectionID = GL20.glGetUniformLocation(this.getProgramID(), "projection");
        Matrix4F p = new Matrix4F();
        
        GL20.glUniformMatrix4fv(projectionID, false, p.toFloatBuffer());      
        

        this.unbind();
    }
    
    public void setViewMatrix(Matrix4F v){
        this.bind();
        GL20.glUniformMatrix4fv(viewID, false, v.toFloatBuffer());
        this.unbind();
    }
    
    public void render(){
        
        this.bind();// use porgrma --> ket thuc disable program
        
        GL30.glBindVertexArray(this.vao);// bind vao -- > unbind vao
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        this.VertexAttribPointer();

        
        GL20.glEnableVertexAttribArray(0);// set index ve 0 
        
        GL11.glDrawElements(GL11.GL_TRIANGLES, this.indices.capacity(), GL11.GL_UNSIGNED_INT, 0);// capacity --> luon luon chua max
        //GL11.
        GL20.glDisableVertexAttribArray(0);// disable 
        
        GL30.glBindVertexArray(0);// unbind vao
        
        this.unbind();// dsiable program
    }

    public int getModelID() {
        return modelID;
    }

    public int getViewID() {
        return viewID;
    }

    public int getProjectionID() {
        return projectionID;
    }
    
    
    
    

}
    

