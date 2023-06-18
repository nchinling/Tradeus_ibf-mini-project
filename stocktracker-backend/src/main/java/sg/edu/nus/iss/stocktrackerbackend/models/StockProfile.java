package sg.edu.nus.iss.stocktrackerbackend.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class StockProfile{
    private String symbol;
    private String name;
    private String sector;
    private String industry;
    private String ceo;
    private Integer employees;
    private String website;
    private String description;
    private String logoUrl;
    public StockProfile() {
    }


    public StockProfile(String symbol, String name, String sector, String industry, String ceo, Integer employees,
            String website, String description, String logoUrl) {
        this.symbol = symbol;
        this.name = name;
        this.sector = sector;
        this.industry = industry;
        this.ceo = ceo;
        this.employees = employees;
        this.website = website;
        this.description = description;
        this.logoUrl = logoUrl;
    }


    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
    public String getIndustry() {
        return industry;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public String getCeo() {
        return ceo;
    }
    public void setCeo(String ceo) {
        this.ceo = ceo;
    }
    public Integer getEmployees() {
        return employees;
    }
    public void setEmployees(Integer employees) {
        this.employees = employees;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

        public String getLogoUrl() {
        return logoUrl;
    }


    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    
    @Override
    public String toString() {
        return "StockProfile [symbol=" + symbol + ", name=" + name + ", sector=" + sector + ", industry=" + industry
                + ", ceo=" + ceo + ", employees=" + employees + ", website=" + website + ", description=" + description
                + ", logoUrl=" + logoUrl + "]";
    }


    public static StockProfile createUserObject(String json) throws IOException {
        StockProfile sp = new StockProfile();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            sp.setSymbol(o.getString("symbol"));
            sp.setName(o.getString("name"));
            sp.setSector(o.getString("sector"));
            sp.setIndustry(o.getString("industry"));
            sp.setCeo(o.getString("CEO"));
            sp.setEmployees(o.getInt("employees"));
            sp.setWebsite(o.getString("website"));
            sp.setDescription(o.getString("description"));
 
        }
        return sp;
    }

    public static StockProfile createLogo(String json) throws IOException {
        StockProfile spl = new StockProfile();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            spl.setLogoUrl(o.getString("url"));
        }
        return spl;
    }


        public JsonObject toJSON(){
    
            return Json.createObjectBuilder()
                .add("symbol", this.getSymbol())
                .add("name", this.getName())
                .add("sector", this.getSector())
                .add("industry", this.getIndustry())
                .add("ceo", this.getCeo())
                .add("employees", this.getEmployees())
                .add("website", this.getWebsite())
                .add("description", this.getDescription())
                .add("logoUrl", this.getLogoUrl())
                .build();            
    }


    
    public static StockProfile createUserObjectFromRedis(String jsonStr) throws IOException{
    StockProfile s = new StockProfile();
        try(InputStream is = new ByteArrayInputStream(jsonStr.getBytes())) {
            JsonObject o = toJSON(jsonStr);
            s.setSymbol(o.getString("symbol"));
            s.setName(o.getString("name"));
            s.setSector(o.getString("sector"));
            s.setIndustry(o.getString("industry"));
            s.setCeo(o.getString("ceo"));
            s.setEmployees(o.getJsonNumber("employees").intValue());
            s.setWebsite(o.getString("website"));
            s.setDescription(o.getString("description"));
            s.setLogoUrl(o.getString("logoUrl"));

        }
        return s;
    }

    public static JsonObject toJSON(String json){
        JsonReader r = Json.createReader(new StringReader(json));
        return r.readObject();
    }



    

    

    
    
}