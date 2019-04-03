package lab3;

public class SymMatrix {
	int[] arr;
	int dim;
	SymMatrix(int n){
		this.arr = new int [n*(n-1)/2];
		this.dim = n-1;
	}
	
	public SymMatrix add(int e, int i, int j) {
		if(i<=j) {
			this.arr[i*dim+j] = e;			
		}else {
			this.arr[j*dim+i]=e;
		}
		
		return this;
	}
	
	public SymMatrix remove(int i, int j) {
		this.arr[(i-1)*dim+j] = -1;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				sb.append(" "+arr[(i-1)*dim+j]);
			}
			sb.append("\n");
		}
		return sb.toString();
		
	}
	

}
