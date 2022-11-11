package indicesGenerators;


public class StandardIndexGenerator implements IndicesGenerator {

	@Override
	public int[] generateIndexBuffer(int vertexCount) {
		int indexCount = (vertexCount - 1) * (vertexCount - 1) * 6;
		int[] indices = new int[indexCount];
		int pointer = 0;
		for (int col = 0; col < vertexCount - 1; col++) {
			for (int row = 0; row < vertexCount - 1; row++) {
				int topLeft = (row * vertexCount) + col;
				int topRight = topLeft + 1;
				int bottomLeft = ((row + 1) * vertexCount) + col;
				int bottomRight = bottomLeft + 1;
				pointer = storeQuad(indices, pointer, topLeft, topRight, bottomLeft, bottomRight);
			}
		}
		return indices;
	}


	private int storeQuad(int[] indices, int pointer, int topLeft, int topRight, int bottomLeft, int bottomRight) {
		indices[pointer++] = topLeft;
		indices[pointer++] = bottomLeft;
		indices[pointer++] = bottomRight;
		indices[pointer++] = topLeft;
		indices[pointer++] = bottomRight;
		indices[pointer++] = topRight;
		return pointer;
	}

}
