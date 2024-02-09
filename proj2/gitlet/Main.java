package gitlet;

import java.io.File;
import java.util.Date;

import static gitlet.Utils.join;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Junru Wang
 */
public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        //args= new String[1];
        //args[0] = "init";
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                MainHelper.init();
                break;
            case "add":
                // TODO: handle the "add [filename]" command
                validateNumArgs("add", args, 2);
                MainHelper.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                // TODO: handle the "commit [message]" command
                validateNumArgs("commit", args, 2);
                Date nowDate = new Date();
                MainHelper.commit(args[1], nowDate);
                break;
        }
    }

    public static void validateNumArgs(String cmd, String[] Args, int n) {
        if (Args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
