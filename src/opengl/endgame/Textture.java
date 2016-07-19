/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.endgame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*; // thư viện cho phép truy nhập vào mẫu. Mẫu là một vật thể được render giống ý như vật thể ảnh chính, nhưng được shader dùng để biến đổi tạo hiệu ứng
/**
 *
 * @author l
 */
public class Textture {
    private int id;
    private int width;
    private int height;
    
    public Textture(File image){
        BufferedImage bi;
        try{

            bi = ImageIO.read(image);
            
            width = bi.getWidth();
            height = bi.getHeight();
            
            int[] pixels_raw = new int[width * height];
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
            
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
            
            for(int i = 0;i < width;i++){
                for(int j = 0;j < height;j++){
                    int pixel = pixels_raw[i*height + j];
                    pixels.put((byte)((pixel >> 16) & 0xFF));
                    pixels.put((byte)((pixel >> 8) & 0xFF));
                    pixels.put((byte)(pixel & 0xFF));
                    pixels.put((byte)((pixel >> 24) & 0xFF));
                }
            }
            
            pixels.flip();
            
            id = glGenTextures();
            
            glBindTexture(GL_TEXTURE_2D, id);
            
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public void bind(int samplerIndex){
            if(samplerIndex >= 0 && samplerIndex <= 31){
                glActiveTexture(GL_TEXTURE0 + samplerIndex);// kích hoạt sử dụng vật thể mẫu thứ sampleindex
                glBindTexture(GL_TEXTURE_2D, id);
            }
    }

}
