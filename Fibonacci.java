package CoreJava;

import java.util.Scanner;

public class Fibonacci {


  int arr[]=new int[]{1,1,7,3,5,8,13};
  int n1=0,n2=0,n3=0;
  int count=0;
	
  public void chk() {
	  
	  System.out.println(arr.length-2);
	  
	  for(int i=arr[0]; i<arr.length-2; i++) {
			
			n1=arr[i];
			System.out.println("now n1 = "+n1);
			n2=arr[i+1];
			System.out.println("\n now n2 = "+n2);
			n3=arr[i+2];
			System.out.println("\n now n3 = "+n3+"\n");
			
			if(n1+n2==n3) {
				
				count++;
	
			}
			
		} 
	  
	
	  if(count==4) {
			
			System.out.println("Given Series is Fibonacci Series.");
		}
		else {
			
			System.out.println("Given Series is Not Fibonacci Series.");
		}
	  
  }
	
	
	
	public static void main(String[] args) {
		
		
		Fibonacci f=new Fibonacci();
		f.chk();
		
		
	}
}
