package com.bymarcin.openglasses.surface;
		import org.apache.commons.io.IOUtils;
		import org.lwjgl.opengl.GL11;
		import org.lwjgl.opengl.GL20;
		
		import java.io.IOException;
		import java.nio.charset.StandardCharsets;
		import java.util.stream.Collectors;

/**
 * Created by marcin212 on 2016-05-30.
 */
public class ShaderProgram {
	
	private int program;
	
	public ShaderProgram(String vertex, String fragment) throws RuntimeException {
		String srcVertex;
		String srcFragment;
		try {
			srcVertex = IOUtils.readLines(ClassLoader.getSystemResourceAsStream(vertex), StandardCharsets.UTF_8).stream().collect(Collectors.joining("\n"));
			srcFragment = IOUtils.readLines(ClassLoader.getSystemResourceAsStream(fragment), StandardCharsets.UTF_8).stream().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		int frag = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		int vert = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		program = GL20.glCreateProgram();
		
		try {
			//fragment shader
			GL20.glShaderSource(vert, srcVertex);
			GL20.glCompileShader(vert);
			if (GL20.glGetShaderi(vert, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				throw new RuntimeException(GL20.glGetShaderInfoLog(vert,1000));
			}
			GL20.glAttachShader(program, vert);
			
			//fragment shader
			GL20.glShaderSource(frag, srcFragment);
			GL20.glCompileShader(frag);
			if (GL20.glGetShaderi(frag, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				throw new RuntimeException(GL20.glGetShaderInfoLog(frag,1000));
			}
			
			GL20.glAttachShader(program, frag);
			
			GL20.glLinkProgram(program);
			if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
				throw new RuntimeException(GL20.glGetProgramInfoLog(frag,1000));
			}
			
			GL20.glValidateProgram(program);
			if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
				throw new RuntimeException(GL20.glGetProgramInfoLog(frag,1000));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			GL20.glDeleteProgram(program);
			throw e;
		} finally {
			GL20.glDeleteShader(vert);
			GL20.glDeleteShader(frag);
		}
	}
	
	public int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(program, name);
	}
	
	public int getAttirbLocation(String name) {
		return GL20.glGetAttribLocation(program, name);
	}
	
	public void uniform1i(String name, int value) {
		GL20.glUniform1i(getUniformLocation(name), value);
	}
	
	public void uniform2i(String name, int v0, int v1) {
		GL20.glUniform2i(getUniformLocation(name), v0, v1);
	}
	
	public void useProgram() {
		GL20.glUseProgram(program);
	}
	
	public int getProgramId() {
		return program;
	}
	
	public void dispose() {
		GL20.glDeleteProgram(program);
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		dispose();
	}
}
