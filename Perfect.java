package CoreJava;

import java.util.Scanner;
public class Perfect {

	public static void main(String[] args) {
		int n=0,sum=0;
       Scanner sc=new Scanner(System.in);
		
		System.out.println("Enter number to check");
		n=sc.nextInt();
		
		
		for(int i=1; i<n; i++) {
			
			if(n%i==0) {
				
				sum=sum+i;
			}
			
		}
		
		if(sum==n) {
			
			System.out.println(n+" is perfect number.");
			
		}
		else {
			
			System.out.println(n+" is not perfect number");
		}
		
	}

}
