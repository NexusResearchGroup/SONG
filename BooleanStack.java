public class BooleanStack {	private boolean Stack[];	private int StackPosition;		public BooleanStack() {		this.StackPosition = -1;		}		public BooleanStack(int Length) {		this.Stack = new boolean[Length];		this.StackPosition = -1;   // StackPosition represents the position of the current element in the stack							// -1 means there are no elements.	}		public void push(boolean pushme) {		if(StackPosition<=Stack.length-1) {			if(this.StackPosition == Stack.length-1) {				boolean temp[] = new boolean[2*Stack.length];				for(int i=0; i<Stack.length; i++) temp[i] = Stack[i]; 				Stack = temp;				Stack[++StackPosition] = pushme;			}			else				Stack[++StackPosition] = pushme;		}	}		public void print_stack() {		for(int i=0; i<=StackPosition; i++) System.out.print(Stack[i] + " ");		System.out.println();			 	}		public void replace(int position, boolean pushme) {		if(position <= StackPosition)			Stack[position] = pushme;		else			System.out.println("Invalid position.  The position is beyond the Stack Position");	}		public boolean access(int position) {		if(position <= StackPosition)			return Stack[position];		else {			System.out.println("Invalid position. cannot return a value returning a false value");			return false;		}	}		public boolean check(boolean Value) {		for(int i=StackPosition; i>=0; i--) {			if(Stack[i] == Value) return true;		}		return false;	}	 	public boolean check(int position, boolean Value) {		if(position <= StackPosition & position >=0)			return (Stack[position] == Value);		else {			System.out.println("Invalid position. returning a false");			return false;		}	}			public void ChangeStackPosition(int NewPosition) {		if( NewPosition < -1 || NewPosition > Stack.length-1 )			System.out.println("Invalid position in the stack. Cannot change to the position.");		else			StackPosition = NewPosition;	}           ////////   Send -1 as argument to this function to reset the stack.  		public int noofelements() {		return (StackPosition+1);	} 			 }