import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdaterMain {
    public static String apppath = "C:/Users/"+System.getProperty("user.name")+"/AppData/Local";
    /*
    This downloads an updated copy of the Birdox master list from the official site.
     */
    public static void main(String[] args) {
        if (checkFiles()){
            try {
                updateFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (missingMasterlist()){
            File masterlist= new File(apppath+"/birdox/masterlist.bdff");
            try {
                masterlist.createNewFile();
                updateFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: missing appdata dirs");
        }
    }

    private static void updateFiles() throws IOException {
        FileWriter masterlist= new FileWriter(apppath+"/birdox/masterlist.bdff");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.birdoxapp.tk/masterlist")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String res = response.body();
            masterlist.write(res.replace("<br>","\n"));
            masterlist.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private static boolean missingMasterlist() {
        File appdir= new File(apppath+"/birdox");
        File masterlist= new File(apppath+"/birdox/masterlist.bdff");
        return appdir.exists() && !(masterlist.exists());
    }

    private static boolean checkFiles() {
        File appdir= new File(apppath+"/birdox");
        File masterlist= new File(apppath+"/birdox/masterlist.bdff");
        return appdir.exists() && masterlist.exists();
    }
}
