package kosakai.functions;

public class BFInterpreter {

	public String printData;
	private int pc;
	private int ptr;
	private int memory_usege;
	private int code_usege;
	private int loop_count;
	private byte[] memory;
	private byte[] code;

	public static void main(String args[]) {
		String data = "+++++++++[>++++++++>+++++++++++>+++++<<<-]>.>++.+++++++..+++.>-.------------.<++++++++.--------.+++.------.--------.>+.";
		BFInterpreter ip = new BFInterpreter();
		ip.run(data);
		System.out.println(ip.printData);
	}

	public BFInterpreter() {
		pc = 0;
		ptr = 0;
		memory_usege = 0;
		code_usege = 0;
		loop_count = 1;
		memory = new byte[1000];
		code = new byte[200];
		printData = "";
	}

	public void run(String str) {
		putCode(str);
		process();
	}

	private void print(char c) {
		printData += c;
	}

	private void process() {
		boolean continue_flag = true;
		while (code[pc] > 0 && continue_flag) {
			switch (code[pc]) {
			case '>':
				ptr++;
				if (memory_usege < ptr)
					memory_usege = ptr;
				break;
			case '<':
				if (ptr > 0)
					ptr--;
				break;
			case '+':
				memory[ptr]++;
				break;
			case '-':
				memory[ptr]--;
				break;
			case '.':
				print((char) memory[ptr]);
				break;
			case ',':
				// 入力は無視する
				break;
			case '[':
				if (!isLoopContinue()) {
					toLoopEnd();
				}
				break;
			case ']':
				// System.out.println(loop_count);
				if (loop_count >= 10000) {
					continue_flag = false;
					printData = "(実行中断)" + printData;
					break;
				}
				toLoopHead();
				break;
			}
			pc++;
		}
	}

	private boolean isLoopContinue() {
		if (memory[ptr] == 0) {
			return false;
		}
		return true;
	}

	private void toLoopEnd() {
		int nest = 1;
		while (nest > 0) {
			switch (code[++pc]) {
			case '[':
				nest++;
				break;
			case ']':
				nest--;
				break;
			}
		}
		loop_count = 1;
	}

	private void toLoopHead() {
		int nest = 1;
		while (nest > 0) {
			switch (code[--pc]) {
			case '[':
				nest--;
				break;
			case ']':
				nest++;
				break;
			}
		}
		pc--;
		loop_count++;
	}

	private void putCode(String buf) {
		byte[] data = buf.getBytes();
		for (int i = 0; i < data.length; i++) {
			if (data[i] == '>' || data[i] == '<' || data[i] == '+'
					|| data[i] == '-' || data[i] == '.' || data[i] == ','
					|| data[i] == '[' || data[i] == ']') {
				code[code_usege] = data[i];
				code_usege++;
			}
		}
	}
}
