package CoreJava;

import java.util.Scanner;

public class Prime {

	public static void main(String[] args) {
		int n;
		int count=0;
		Scanner sc=new Scanner(System.in);

		System.out.println("Enter the number to check");
		n=sc.nextInt();
		
		for(int i=1; i<=n; i++) {
			
			if(n%i==0) {
				
				count++;
			}
		}
		
		
		if(count==2) {
			
			System.out.println(n+" is Prime Number.");
		}
		else {
			
			System.out.println(n+" is not Prime Number.");
		}	
	
	
	}
	
	

}
