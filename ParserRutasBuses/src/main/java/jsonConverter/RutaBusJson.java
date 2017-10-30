package jsonConverter;

public class RutaBusJson {
    private String zona;
    private String html;
    private String gps;
    private String line;

    public RutaBusJson(String zona1, String html1, String gps1, String line1){
        this.zona = zona1;
        this.html = html1;
        this.gps = gps1;
        this.line = line1;
    }
}
