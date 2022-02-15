package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;}

  /*  public Shader(String filepath){
        this.filepath = filepath;
        try{
        String source = new String(Files.readAllBytes(Paths.get(filepath)));
        String[] splitString = source.split("(#type)( )([a-z]A-Z]+)");

        int index = source.indexOf("#type") + 6; //  6 is the value of #type lines
        int eol = source.indexOf("\r\n", index);
        String firstPattern = source.substring(index, eol).trim();
        index = source.indexOf("#type", eol) + 6;
        eol   = source.indexOf("\r\n",index);


    }catch(IOException e){
            e.printStackTrace();
            assert false : "Error: could not open file for shader: ' "+ filepath + "'";
        }

    public void compile(){

    }

    public void use(){

    }

    public void detach(){

    }
}
*/