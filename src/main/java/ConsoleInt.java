public class ConsoleInt {
	
	private String consIn;
	private String[] consOut;


	public ConsoleInt(String str) {
		consIn = str;
	}
	
	
//====================================================================
	public void print() {
		String[] out;
		out = analiz(consIn);
		
		if(out !=null) {
		System.out.print("Entered command=\""+out[0]+"\", parameters=");
		if(out[1].equals(""))
			System.out.print("[]");
		else {
			if(out.length == 2) {
				if(out[0].equals("file"))
					System.out.print("[\""+out[1]+"\", <No Files>]");
				else
				System.out.print("[\""+out[1]+"\"]");
			}
			else{
				if(out[0].equals("msg")||out[0].equals("list")||out[0].equals("file")||out[0].equals("file")||out[0].equals("ping")||out[0].equals("login")||out[0].equals("msg")) {
					for(int i = 1; i < out.length; i++) {
						if(i == 1)
							System.out.print("[");
							System.out.print("\""+ out[i]+"\"");
							if(i < out.length - 1)
								System.out.print(",");
							if(i == out.length - 1)
								System.out.print("]");
							}
					}
				}
			}
		}
		}
	
		
//=========================================================		
	private String[] analiz(String consIn) {
		consIn = consIn.trim();
		char[] ch;
		int ind;
		String commanda = "";
		String parametrTmp = "";
		ch = consIn.toCharArray();
		ind = consIn.indexOf(' ');
		
		for(int i = 0; i < ch.length; i++) {
			if(ind > 0) {
			if(i < ind)
				commanda +=String.valueOf(ch[i]);
			else
				parametrTmp +=String.valueOf(ch[i]);
			}
			else {
				commanda +=String.valueOf(ch[i]);
			}
		}

		parametrTmp = parametrTmp.trim();
		
		
		switch(commanda){
        case "ping":{
        	if(parametrTmp.equals("")) {
        		consOut = new String[2];
        		consOut[0] = commanda;
        		consOut[1] = "";
        	}else {
        		consOut = new String[2];
        		if(parametrTmp.contains(" ")) {
        			consOut = null;
        			System.out.println("Error input parameters");
        		}
        		else {
        			consOut[0] = commanda;
        			consOut[1] = parametrTmp;
        		}
        	}
        	break;
        	}
        case "echo":{
        	consOut = new String[2];
    		consOut[0] = commanda;
    		consOut[1] = parametrTmp;
        	break;
        	}
        case "login":{
        	if(parametrTmp.equals("")) {
        		consOut = new String[2];
        		consOut[0] = commanda;
        		consOut[1] = "";
        	}else if(!parametrTmp.contains(" ")) {
        		consOut = new String[3];
        		consOut[0] = commanda;
        		consOut[1] = parametrTmp;
        		consOut[2] = "";
        	}
        	else {
        		String[] tmp;
        		tmp = new String[numWorld(parametrTmp).length];
        		tmp = numWorld(parametrTmp);
        		if(tmp.length > 2) {
        			System.out.println("Error enter <login> or <password>");
        			consOut = null;
        		}
        		else if(tmp.length == 2) {
        			 consOut = new String [3];
        			 consOut[0] = commanda;
        			 consOut[1] = tmp[0];
        			 consOut[2] = tmp[1];
        			 }
        	}
        	break;
        	}
        case "list":{
        	if(parametrTmp.equals("")) {
        		consOut = new String[2];
        		consOut[0] = commanda;
        		consOut[1] = "";
        	}
        	else {
        		String[] tmp;
        		tmp = new String[numWorld(parametrTmp).length];
        		tmp = numWorld(parametrTmp);
        		
        		consOut = new String[tmp.length + 1];
        		consOut[0] = commanda;
        		for(int i = 1, j = 0; i < tmp.length + 1; i++, j++) {
        			consOut[i] = tmp[j];
        			}
        	}
        	break;
        	}
        case "msg":{
        	if(parametrTmp.equals("")) {
        		consOut = null;
        		System.out.println("Error enter parameters");
        	}else {
    		if(!parametrTmp.contains(" ")) {
    			consOut = new String[3];
    			consOut[0] = commanda;
    			consOut[1] = parametrTmp;
    			consOut[2] = "";
    		}else {
    			consOut = new String[3];
    			String temp;
    			String[] tem;
    			String[] t;
    			t = parametrTmp.split(" ");

    			temp = consIn.substring(t[0].length()).trim();
    			tem = temp.split(" ");
    			temp = temp.substring(tem[0].length()).trim();
    			consOut[0] = commanda;
    			consOut[1] = t[0];
    			consOut[2] = temp;
    		}
    		}
        	break;
        }  
        case "file":{
        	if(parametrTmp.equals("")) {
        		consOut = null;
        		System.out.println("Error enter parameters");
        		}
        	else {
        		String[] tmp;
        		tmp = new String[numWorld(parametrTmp).length];
        		tmp = numWorld(parametrTmp);
        		if(tmp.length == 1) {
        			System.out.println("Not enter name of files");
        		}else {
        			consOut = new String[tmp.length + 1];
        			consOut[0] = commanda;
        		for(int i = 1, j = 0; i < tmp.length + 1; i++, j++) {
        			consOut[i] = tmp[j];
        			}
        		}
        		
        		}
        	break;
        	}
        case "exit":{
        	String[] tmp;
    		tmp = new String[numWorld(parametrTmp).length];
    		tmp = numWorld(parametrTmp);


    		if(tmp.length == 0) {
    			consOut = new String[tmp.length + 2];
    			consOut[0] = commanda;
    			consOut[1] = "";
    		}else {
    			consOut = new String[tmp.length + 1];
    			consOut[0] = commanda;
    			for(int i = 1, j = 0; i < tmp.length + 1; i++, j++) {
    				consOut[i] = tmp[j];
    			}
    		}
        	break;
        	} 
        	
        default:{
        	System.out.println("Command not found");
        	consOut = null;
            break;
            }
        }
		return consOut;
		}
	
//==============================================================	
	private String[] numWorld(String parametrTMP) {
		String[] T;
		int count = 0;
		T = parametrTMP.split(" ");
		for (int i =0 ; i < T.length; i++) {
			if(T[i].equals(""))
				count++;
		}
		String[] tem = new String[T.length - count];
		for(int i = 0, j = 0; i < T.length; i++) {
			if(T[i].equals(""))
				continue;
			else {
				tem[j] = T[i];
				j++;
			}
		}
		return tem;
	}
}
