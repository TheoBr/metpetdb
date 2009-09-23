package edu.rpi.metpetdb.client.ui.plot.threeD;

public class PlaneEquation {
	public double A;
	public double B;
	public double C;
	public double D;
	
	public PlaneEquation(Point3D p1, Point3D p2, Point3D p3){
		A = p1.getY()*(p2.getZ() - p3.getZ()) + p2.getY()*(p3.getZ()-p1.getZ()) + p3.getY()*(p1.getZ() - p2.getZ());
		B = p1.getZ()*(p2.getX() - p3.getX()) + p2.getZ()*(p3.getX() - p1.getX()) + p3.getZ()*(p1.getX() - p2.getX());
		C = p1.getX()*(p2.getY() - p3.getY()) + p2.getX()*(p3.getY() - p1.getY()) + p3.getX()*(p1.getY() - p2.getY());
		D = -1*(p1.getX()*((p2.getY()*p3.getZ()) - (p3.getY()*p2.getZ())) + p2.getX()*((p3.getY()*p1.getZ()) - (p1.getY()*p3.getZ())) +
					p3.getX()*((p1.getY()*p2.getZ()) - (p2.getY()*p1.getZ())));
	}
	
	public static Point3D getPlaneIntersection(PlaneEquation pe1, PlaneEquation pe2, PlaneEquation pe3){
		double matrix[][] = new double[][] {{pe1.A, pe1.B, pe1.C, -1*pe1.D},
				{pe2.A, pe2.B, pe2.C, -1*pe2.D},
				{pe3.A, pe3.B, pe3.C, -1*pe3.D}};

		int i = 0;
		int j = 0;
		int m = 3;
		int n = 4;
		while (i < m && j < n) {
			//Find pivot in column j, starting in row i:
			int maxi = i;
			for (int k = i+1; k < m; k++){
				if (Math.abs(matrix[k][j]) > Math.abs(matrix[maxi][j])) {
					maxi = k;
				}
			}
			if (matrix[maxi][j] != 0){
				// swap rows i and maxi
				double temp[] = new double[] {matrix[i][0], matrix[i][1], matrix[i][2], matrix[i][3]};
				matrix[i][0]=matrix[maxi][0]; matrix[i][1]=matrix[maxi][1]; matrix[i][2]=matrix[maxi][2]; matrix[i][3]=matrix[maxi][3];
				matrix[maxi][0]= temp[0]; matrix[maxi][1]=temp[1]; matrix[maxi][2]=temp[2]; matrix[maxi][3]=temp[3];
				//  divide each entry in row i by A[i,j]
				double divisor = matrix[i][j];
				for (int u = 0; u < n; u++){
					matrix[i][u] /= divisor;
				}
				for (int u = i+1; u < m; u++)  {
					// subtract A[u,j] * row i from row u
					double subtract = matrix[u][j];
					for (int v = 0; v < n; v++){
						matrix[u][v] -= (subtract*matrix[i][v]);
					}
				}
				for (int u = i-1; u >= 0; u--)  {
					// subtract A[u,j] * row i from row u
					double subtract = matrix[u][j];
					for (int v = 0; v < n; v++){
						matrix[u][v] -= (subtract*matrix[i][v]);
					}
				}
				i++;
			}
			j++;
		}		
		return new Point3D(matrix[0][3],matrix[1][3],matrix[2][3]);
	}
	
	public String toString(){
		return A + " " + B + " " + C + " " + D;
	}
}
