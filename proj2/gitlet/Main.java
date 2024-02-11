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
            case "rm":
                //TODO: handle the "rm [filename]" command
                validateNumArgs("rm", args, 2);
                MainHelper.rm(args[1]);
                break;
            case "log":
                //TODO: handle the "log" command
                validateNumArgs("log", args, 1);
                MainHelper.log();
                break;
            case "status":
                //TODO: handle the "status" command
                validateNumArgs("status", args, 1);
                MainHelper.status();
                break;
            case "branch":
                //TODO: handle the "branch [branch name]" command
                validateNumArgs("branch", args, 2);
                MainHelper.branch(args[1]);
                break;
            case "rm-branch":
                //TODO: handle the "rm-branch [branch name]" command
                validateNumArgs("rm-branch", args, 2);
                MainHelper.rmBranch(args[1]);
                break;
            case "checkout":
                switch (args.length) {
                    case 3:
                        //TODO: handle the "checkout -- [file name]"
                        validateNumArgs("checkout", args, 3);
                        validateOperand("--", args[1]);
                        MainHelper.checkoutFileName(args[2]);
                        break;
                    case 4 :
                        //TODO: handle the "checkout [commit ID] -- [file name]" command
                        validateNumArgs("checkout", args, 4);
                        validateOperand("--", args[2]);
                        MainHelper.checkoutCommitFileName(args[1], args[3]);
                        break;
                }
        }
    }

    public static void validateNumArgs(String cmd, String[] Args, int n) {
        if (Args.length != n) {
            switch (cmd) {
                case "commit":
                    if (Args.length == 1) {
                        System.out.println("Please enter a commit message. ");
                        break;
                    }
            }
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void validateOperand(String expectedOperand, String actualOperand) {
        if (! expectedOperand.equals(actualOperand)) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
