package CoreJava;

import java.util.Scanner;

public class StringPalindrome {

	public static void main(String[] args) {
          int length;
          String s;
          boolean flag=true;
		  Scanner sc=new Scanner(System.in);
          int i=0, j;
		  
		  System.out.println("Enter String to check");
		  s=sc.next();
		  
		  length=s.length();
		  
		  for(i=0, j=length-1; i<=j; i++, j--) {
			  
			if(s.charAt(i)==s.charAt(j)) {
				
				flag=true;
				continue;
				
			}  
			else {
				
				flag=false;
			}
			
		  }
	
	
	       if(flag==true) {
	    	   
	    	   System.out.println("String is Palindrome");
	       }
	       else {
	    	   
	    	   System.out.println("String is not palindrome");
	       }
	      
	}

}
