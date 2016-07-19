/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.endgame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.Scanner;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;

/**
 *
 * @author l
 */
public class Shader {// là chương trình render trên grapphic card và sẽ dùng để tạo các hiệu ứng ngắn, kĩ thuật này là cách để dễ dàng có thể biến đổi dãy điểm hình 
    //và dãy điểm dệt ảnh qua biến truyền trực tiếp vì các dãy này là input cố định ban đầu
    private int program; // mã chương trình gắn kết 2 loại shader: output vertexshader là input fragmentshader
    private int vs; // mã chương trình vẽ điểm
    private int fs; // mã chương trình thêm màu
    
    public Shader(String filename) {
        program = glCreateProgram();
        
        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(new File("src/opengl/endgame/" + filename + ".vs").getAbsolutePath())); // đọc source code gửi cho vật thể GL_VERTEX_SHADER
        glCompileShader(vs); // biên dịch vật thể có id là vs
        if(glGetShaderi(vs, GL_COMPILE_STATUS) != 1){ // khi có lỗi trong quá trình biên dịch mà không biết thì dùng hàm này
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(new File("src/opengl/endgame/" + filename + ".fs").getAbsolutePath()));
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1){
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }
        
        glAttachShader(program, vs); 
// vật thể program object cung cấp cơ chế để có thể link  vs  và fs với nhau, để làm đc điều này trước tiên phải gắn mã của nó vào  program object
        glAttachShader(program, fs);
        
        glBindAttribLocation(program, 0, "vertices"); // liên kết một thuộc tính vừa tạo có mã = 0 với thuộc tính có tên vertices trong vs
        glBindAttribLocation(program, 1, "textures");
        
        
        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != 1){
            System.err.println(glGetProgramInfoLog(program));
        }
        glValidateProgram(program); // cho phép chạy
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1){
            System.err.println(glGetProgramInfoLog(program));
        }
        
    }
    
    public void setUniform(String name,int value){ 
        // biến uniform trong shader có tác dụng là ta có thể 
        //truyền trực tiếp giá trị vào biến mà ko cần dùng input đầu vào
        
        // biến này nó được chứa trong vùng của program trên graphic card 
        
        
        int location = glGetUniformLocation(program, name);// lấy giá trị của vùng chứa uniform tên name mà opengl có thể dễ dàng trả lại cho chúng ta
        if(location != -1){ // == -1 nếu program chưa được validate
            glUniform1i(location, value);
            // lấy giá trị của biến uniform nới nó được định nghĩa
        }
        
    }
    
      public void setUniform(String name,Matrix4f value){ 
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);
        value.get(buffer);
        if(location != -1){ 
            glUniformMatrix4fv(location, false,buffer); //  false là không chuyển vị
        }
    }
      
       public void setUniform(String name,float value){ 
        int location = glGetUniformLocation(program, name);
        if(location != -1){ 
            glUniform1f(location,value); //  false là không chuyển vị
        }
    }
      
      
    
    public void bind(){
        glUseProgram(program);
    }
    
    public void deleteShader(){
        
        GL20.glDetachShader(this.program, this.vs);
        GL20.glDetachShader(this.program, this.fs);
        // delete shader
        GL20.glDeleteShader(this.vs);
        GL20.glDeleteShader(this.fs);
        glDeleteProgram(program);
        
        
    }
    private String readFile(String filename){
        StringBuilder string = new StringBuilder();
        Scanner br;
        try{
            br = new Scanner(new FileInputStream(filename));
            String line;
            while(br.hasNextLine()){
                string.append(br.nextLine());
                string.append("\n");
            }
            
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return string.toString();
    }
}
