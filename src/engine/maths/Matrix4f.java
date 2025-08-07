package engine.maths;

import java.util.Arrays;

public class Matrix4f {
	public static final int SIZE = 4;
	private float[] elements = new float[SIZE * SIZE];

	public static Matrix4f identity() {
		Matrix4f result = new Matrix4f();

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				result.set(i, j, 0);
			}
		}

		// set all the diagonal ones
		result.set(0, 0, 1);
		result.set(1, 1, 1);
		result.set(2, 2, 1);
		result.set(3, 3, 1);

		return result;
	}

	public static Matrix4f translate(float[] translate) {
		Matrix4f result = Matrix4f.identity();

		result.set(3, 0, translate[0]);
		result.set(3, 1, translate[1]);
		result.set(3, 2, translate[2]);

		return result;
	}

	public static Matrix4f rotate(float angle, float[] axis) {
		Matrix4f result = Matrix4f.identity();
		
		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));
		float C = 1 - cos;
		
		result.set(0, 0, cos + axis[0] * axis[0] * C);
		result.set(0, 1, axis[0] * axis[1] * C - axis[2] * sin);
		result.set(0, 2, axis[0] * axis[2] * C + axis[1]  * sin);
		result.set(1, 0, axis[1]  * axis[0] * C + axis[2] * sin);
		result.set(1, 1, cos + axis[1]  * axis[1]  * C);
		result.set(1, 2, axis[1]  * axis[2] * C - axis[0] * sin);
		result.set(2, 0, axis[2] * axis[0] * C - axis[1]  * sin);
		result.set(2, 1, axis[2] * axis[1]  * C + axis[0] * sin);
		result.set(2, 2, cos + axis[2] * axis[2] * C);
		
		return result;
	}

	public static Matrix4f scale(float[] scalar) {
		Matrix4f result = Matrix4f.identity();
		result.set(0, 0, scalar[0]);
		result.set(1, 1, scalar[1]);
		result.set(2, 2, scalar[2]);

		return result;
	}

	public static Matrix4f transform(float[] position, float[] rotation, float[] scale) {
		Matrix4f result = Matrix4f.identity();
		
		Matrix4f translationMatrix = Matrix4f.translate(position);
		Matrix4f rotXMatrix = Matrix4f.rotate(rotation[0], new float[] {1, 0, 0});
		Matrix4f rotYMatrix = Matrix4f.rotate(rotation[1], new float[] {0, 1, 0});
		Matrix4f rotZMatrix = Matrix4f.rotate(rotation[2], new float[] {0, 0, 1});
		Matrix4f scaleMatrix = Matrix4f.scale(scale);
		
		Matrix4f rotationMatrix = Matrix4f.multiply(rotXMatrix, Matrix4f.multiply(rotYMatrix, rotZMatrix));
		
		result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));
		
		return result;
	}
	
	
	
	
	public static Matrix4f projection(float fov, float aspect, float near, float far) {
		Matrix4f result = Matrix4f.identity();
		
		float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
		float range = far - near;
		
		result.set(0, 0, 1.0f / (aspect * tanFOV));
		result.set(1, 1, 1.0f / tanFOV);
		result.set(2, 2, -((far + near) / range));
		result.set(2, 3, -1.0f);
		result.set(3, 2, -((2 * far * near) / range));
		result.set(3, 3, 0.0f);
		
		return result;
	}
	
	public static Matrix4f view(float[] position, float[] rotation) {
		Matrix4f result = Matrix4f.identity();

		float[] negative = {-position[0],  -position[1], -position[2]};
		Matrix4f translationMatrix = translate(negative);
		Matrix4f rotXMatrix = rotate(rotation[0], new float[] {1, 0, 0});
		Matrix4f rotYMatrix = rotate(rotation[1], new float[] {0, 1, 0});
		Matrix4f rotZMatrix = rotate(rotation[2], new float[] {0, 0, 1});

		Matrix4f rotationMatrix = multiply(rotZMatrix, multiply(rotYMatrix, rotXMatrix));
		result = multiply(translationMatrix,rotationMatrix);

		return result;

	}

	public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
		Matrix4f result = Matrix4f.identity();

		for (int i = 0; i < Matrix4f.SIZE; i++) {
			for (int j = 0; j < Matrix4f.SIZE; j++) {
				result.set(i, j, matrix.get(i, 0) * other.get(0, j) + matrix.get(i, 1) * other.get(1, j)
						+ matrix.get(i, 2) * other.get(2, j) + matrix.get(i, 3) * other.get(3, j));
			}
		}
		return result;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix4f other = (Matrix4f) obj;
		return Arrays.equals(elements, other.elements);
	}

	public float get(int x, int y) {
		return elements[y * SIZE + x];
	}

	public void set(int x, int y, float value) {
		elements[y * SIZE + x] = value;
	}

	public float[] getAll() {
		return elements;
	}

}
