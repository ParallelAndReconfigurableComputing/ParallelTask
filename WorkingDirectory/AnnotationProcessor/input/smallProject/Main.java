package smallProject;

import sp.annotations.InitParaTask;

public class Main {
	@InitParaTask
	static public void main(String[] args){
		FutureArray f = new FutureArray(15);
		//FutureArray f = new FutureArray();
		f.processTasks();
	}
}
