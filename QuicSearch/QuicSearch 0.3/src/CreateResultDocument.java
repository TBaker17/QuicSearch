import java.io.*;

public class CreateResultDocument {

    int docCount = 0;
    String str;

    public static void main(String[] args) {

        new CreateResultDocument();

    }

    // Stores results taken from webpage ***NEEDS FIX***
    public CreateResultDocument() {
        str = docCount + "";
        String localDir = System.getProperty("user.dir");
        File nF = new File(localDir + "\\QuicSearch 0.3\\results\\Res0s.txt");
        boolean bool = true;

        String filePath = nF.getAbsolutePath();
        System.out.println(filePath);

        System.out.println(localDir + "\\QuicSearch 0.3\\results\\Res0s.txt");

        try {
            // increment Result # by 1 to avoid overwrite if name is already used
            while (bool) {
                System.out.println(nF.isFile());
                if (nF.getAbsoluteFile().isFile()) {
                    System.out.println("File created: " + nF.getName());
                    bool = false;
                } else {
                    System.out.println("File already exists.");
                    str = (docCount++) + "";
                    nF = new File(
                            System.getProperty("user.dir") + "\\QuicSearch 0.3\\results\\Res0s.txt");
                }

            }

            System.out.println("Result created: " + nF.getName());

        } catch (Exception e) {
            System.out.println("Something went wrong...");
            e.printStackTrace();
        }

    }
}
