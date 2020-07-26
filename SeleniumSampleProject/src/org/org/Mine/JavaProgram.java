package org.org.Mine;

public class JavaProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int total=0;
		for(int i=1;i<=1000;i++) {
			if (i % 3 == 0 || i % 5 == 0) {
				total=total + i;
				
			}
			System.out.println(total);
		}
		

	}

}
