public class Console_int {
	
	private String consIN;
	private String[] consOUT;


	public Console_int(String str) {
		consIN = str;
	}
	
	
//====================================================================
	public void Print() {
		String[] Out;
		Out = Analiz(consIN);
		
		if(Out !=null) {
		System.out.print("Entered command=\""+Out[0]+"\", parameters=");
		if(Out[1].equals(""))
			System.out.print("[]");
		else {
			if(Out.length == 2) {
				if(Out[0].equals("file"))
					System.out.print("[\""+Out[1]+"\", <No Files>]");
				else
				System.out.print("[\""+Out[1]+"\"]");
			}
			else{
				if(Out[0].equals("msg")||Out[0].equals("list")||Out[0].equals("file")||Out[0].equals("file")||Out[0].equals("ping")||Out[0].equals("login")||Out[0].equals("msg")) {
					for(int i = 1; i < Out.length; i++) {
						if(i == 1)
							System.out.print("[");
							System.out.print("\""+ Out[i]+"\"");
							if(i < Out.length - 1)
								System.out.print(",");
							if(i == Out.length - 1)
								System.out.print("]");
							}
					}
				}
			}
		}
		}
	
		
//=========================================================		
	private String[] Analiz(String consIN) {
		consIN = consIN.trim();
		char[] ch;
		int ind;
		String commanda = "";
		String parametrTMP = "";
		ch = consIN.toCharArray();
		ind = consIN.indexOf(' ');
		
		for(int i = 0; i < ch.length; i++) {
			if(ind > 0) {
			if(i < ind)
				commanda +=String.valueOf(ch[i]);
			else
				parametrTMP +=String.valueOf(ch[i]);
			}
			else {
				commanda +=String.valueOf(ch[i]);
			}
		}

		parametrTMP = parametrTMP.trim();
		
		
		switch(commanda){
        case "ping":{
        	if(parametrTMP.equals("")) {
        		consOUT = new String[2];
        		consOUT[0] = commanda;
        		consOUT[1] = "";
        	}else {
        		consOUT = new String[2];
        		if(parametrTMP.contains(" ")) {
        			consOUT = null;
        			System.out.println("Error input parameters");
        		}
        		else {
        			consOUT[0] = commanda;
        			consOUT[1] = parametrTMP;
        		}
        	}
        	break;
        	}
        case "echo":{
        	consOUT = new String[2];
    		consOUT[0] = commanda;
    		consOUT[1] = parametrTMP;
        	break;
        	}
        case "login":{
        	if(parametrTMP.equals("")) {
        		consOUT = new String[2];
        		consOUT[0] = commanda;
        		consOUT[1] = "";
        	}else if(!parametrTMP.contains(" ")) {
        		consOUT = new String[3];
        		consOUT[0] = commanda;
        		consOUT[1] = parametrTMP;
        		consOUT[2] = "";
        	}
        	else {
        		String[] tmp;
        		tmp = new String[NumWorld(parametrTMP).length];
        		tmp = NumWorld(parametrTMP);
        		if(tmp.length > 2) {
        			System.out.println("Error enter <login> or <password>");
        			consOUT = null;
        		}
        		else if(tmp.length == 2) {
        			 consOUT = new String [3];
        			 consOUT[0] = commanda;
        			 consOUT[1] = tmp[0];
        			 consOUT[2] = tmp[1];
        			 }
        	}
        	break;
        	}
        case "list":{
        	if(parametrTMP.equals("")) {
        		consOUT = new String[2];
        		consOUT[0] = commanda;
        		consOUT[1] = "";
        	}
        	else {
        		String[] tmp;
        		tmp = new String[NumWorld(parametrTMP).length];
        		tmp = NumWorld(parametrTMP);
        		
        		consOUT = new String[tmp.length + 1];
        		consOUT[0] = commanda;
        		for(int i = 1, j = 0; i < tmp.length + 1; i++, j++) {
        			consOUT[i] = tmp[j];
        			}
        	}
        	break;
        	}
        case "msg":{
        	if(parametrTMP.equals("")) {
        		consOUT = null;
        		System.out.println("Error enter parameters");
        	}else {
    		if(!parametrTMP.contains(" ")) {
    			consOUT = new String[3];
    			consOUT[0] = commanda;
    			consOUT[1] = parametrTMP;
    			consOUT[2] = "";
    		}else {
    			consOUT = new String[3];
    			String temp;
    			String[] tem;
    			String[] T;
    			T = parametrTMP.split(" ");

    			temp = consIN.substring(T[0].length()).trim();
    			tem = temp.split(" ");
    			temp = temp.substring(tem[0].length()).trim();
    			consOUT[0] = commanda;
    			consOUT[1] = T[0];
    			consOUT[2] = temp;
    		}
    		}
        	break;
        }  
        case "file":{
        	if(parametrTMP.equals("")) {
        		consOUT = null;
        		System.out.println("Error enter parameters");
        		}
        	else {
        		String[] tmp;
        		tmp = new String[NumWorld(parametrTMP).length];
        		tmp = NumWorld(parametrTMP);
        		if(tmp.length == 1) {
        			System.out.println("Not enter name of files");
        		}else {
        			consOUT = new String[tmp.length + 1];
        		consOUT[0] = commanda;
        		for(int i = 1, j = 0; i < tmp.length + 1; i++, j++) {
        			consOUT[i] = tmp[j];
        			}
        		}
        		
        		}
        	break;
        	}
        case "exit":{
        	String[] tmp;
    		tmp = new String[NumWorld(parametrTMP).length];
    		tmp = NumWorld(parametrTMP);


    		if(tmp.length == 0) {
    			consOUT = new String[tmp.length + 2];
    			consOUT[0] = commanda;
    			consOUT[1] = "";
    		}else {
    			consOUT = new String[tmp.length + 1];
    			consOUT[0] = commanda;
    			for(int i = 1, j = 0; i < tmp.length + 1; i++, j++) {
    			consOUT[i] = tmp[j];
    			}
    		}
        	break;
        	} 
        	
        default:{
        	System.out.println("Command not found");
        	consOUT = null;
            break;
            }
        }
		return consOUT;
		}
	
//==============================================================	
	private String[] NumWorld(String parametrTMP) {
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
