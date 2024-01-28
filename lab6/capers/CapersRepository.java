package capers;

import java.io.File;
import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /**
     * Current Working Directory.
     */
    static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * Main metadata folder.
     */
    static final File CAPERS_FOLDER = join(CWD, ".capers"); // TODO Hint: look at the `join`
    //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     * <p>
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     * - dogs/ -- folder containing all of the persistent data for dogs
     * - story -- file containing the current story
     */
    public static void setupPersistence() {
        File dogs = join(CAPERS_FOLDER, "dogs");
        CAPERS_FOLDER.mkdir();// TODO
        Dog.DOG_FOLDER.mkdir();
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     *
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        File storyFile = join(CAPERS_FOLDER, "story");
        String newContent;
        if (storyFile.exists()) {
            newContent = readContentsAsString(storyFile) + "\n" + text;
        } else {
            newContent = text;
        }
        writeContents(storyFile, newContent);
        System.out.println(newContent);
        // TODO
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog newDog = new Dog(name, breed, age);// TODO
        newDog.saveDog();
        System.out.println(newDog.toString());
    }
        /**
         * Advances a dog's age persistently and prints out a celebratory message.
         * Also prints out the dog's information using toString().
         * Chooses dog to advance based on the first non-command argument of args.
         * @param name String name of the Dog whose birthday we're celebrating.
         */
        public static void celebrateBirthday (String name) {
            File birthdayDogFile = join(Dog.DOG_FOLDER, name);// TODO
            if (! birthdayDogFile.exists()) {
                exitWithError(String.format("This dog doesn't exist: %s", name));
            }
            else {
                Dog birthdayDog = readObject(birthdayDogFile, Dog.class);
                birthdayDog.haveBirthday();
                birthdayDog.saveDog();
            }
        }
}

