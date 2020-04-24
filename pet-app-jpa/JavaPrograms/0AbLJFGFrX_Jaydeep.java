import java.util.*;  
class 0AbLJFGFrX_Jaydeep{  
public static void main(String args[]){  
ArrayList<String> list=new ArrayList<String>();//Creating arraylist  
list.add("Ravi");//Adding object in arraylist  
list.add("Vijay");  
list.add("Ravi");  
list.add("Ajay"); 
list.add("jaydeep");
String s[] = new String[3];
s[0] = "aaa"; s[1] = "bbb"; s[2] = "ccc";
main(s);
//Traversing list through Iterator  
Iterator itr=list.iterator();  
while(itr.hasNext()){  
System.out.println(itr.next());  
}  
}  
}  