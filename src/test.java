import java.util.ArrayList;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
	
public class test {
	public static void main(String args[]) {
		List<Double> latitudes= new ArrayList();
		List<Double> longitudes= new ArrayList();
				double area=0.0;
				double centerX=0.0;
				double centerY=0.0;
				latitudes.add(3.0);
				latitudes.add(3.0);
				latitudes.add(-0.0);
				latitudes.add(-0.0);
				longitudes.add(-0.0);
				longitudes.add(3.0);
				longitudes.add(3.0);
				longitudes.add(-0.0);
				int position=0;
				for(int k=0; k< latitudes.size(); k++){
					if(k+1==latitudes.size()){
						position=0;
					}
					else{
						position=k+1;
					}
					area+= (longitudes.get(k)*latitudes.get(position))- (longitudes.get(position)*latitudes.get(k));
					//area+= ((longitudes.get(k+1)- longitudes.get(k)) * (latitudes.get(k+1)- latitudes.get(k)))/2;
					centerX+= (longitudes.get(k)*longitudes.get(position))*(longitudes.get(k)*latitudes.get(position) + longitudes.get(position)* latitudes.get(k));
					centerY+= (latitudes.get(k)*latitudes.get(position))*(longitudes.get(k)*latitudes.get(position) + longitudes.get(position)* latitudes.get(k));
					//centerX+=longitudes.get(k);
					//centerY+=latitudes.get(k);
				}
				//centerX=centerX/longitudes.size()-1;
				//centerY=centerY/latitudes.size()-1;
				area*=.5;
				centerX= centerX*(1/(6*area));
				centerY= centerY*(1/(6*area));
				System.out.println(area);
				System.out.println(centerX);
				System.out.println(centerY);
	}
}