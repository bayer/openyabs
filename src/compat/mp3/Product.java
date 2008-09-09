/*
 *  This file is part of MP by anti43 /GPL.
 *  
 *      MP is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *  
 *      MP is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with MP.  If not, see <http://www.gnu.org/licenses/>.
 */
package compat.mp3;



/**
 *
 * @author anti43
 */
public class Product extends compat.mp3.Things implements compat.mp3.Structure {
/**
 * "nummer" + "," +"name" + "," + "text" + "," +"vk" + "," +"ek" +
              "," +"tax"+ ","+ "herstellerid"+ ","+"lieferantenid"+ ","+"warengruppenid"
              
              + ","+"datum"+ ","+"url";
 */
    private String Ean = "";
    private String Nummer = "";
    private String Name = "";
    private String Datum = "";
    private String url = "";
    private String Text = "";
    private String VK = "";
    private String EK = "";
    private String TAX = "";
    private String Hersteller = "";
    private String LieferantenId = "0";
    private String WarengruppenId = "0";
 
//    private Manufacturer manufacturer;
    private Supplier supplier;
    private Query query;
    public boolean isvalid=false;
//    private ProductFile[] files;

    public Product(Query query) {
        super(query.clone(TABLE_PRODUCTS));

       
      
        supplier = new Supplier(query);
        
        this.query = query;

    }

    /**
     * 
     * @param query
     * @param id
     */
    public Product(Query query, String id) {
        super(query.clone(TABLE_PRODUCTS));
        this.id = Integer.valueOf(id);
        this.explode(this.selectLast("*", "id", id, true));

//        group = new ProductGroup(query, this.getWarengruppenId());
//        manufacturer = new Manufacturer(query, this.getHerstellerId());
        
        
          if(!this.getLieferantenId().equals("0")) {
            this.supplier = new Supplier(query, this.getLieferantenId());
        }
          this.isvalid =true;
          this.query = query;
//        files = getFiles(query);
    }

   
        
   
        
    


  

    private void explode(String[] select) {

        this.setNummer(select[1]);
        this.setName(select[2]);
       
        
        this.setText(select[3]);
        this.setVK(select[4]);
        this.setEK(select[5]);
        this.setTAX(select[6]);
        this.setHersteller(select[7]);
        this.setLieferantenId(select[8]);
        this.setWarengruppenId(select[9]); 
        
        this.setDatum(select[10]);
        this.setUrl(select[11]);
        this.setEan(select[12]);
    }

    public String getAll(){
//    "produktnummer", "name", "text", "vk",
//                    "ek", "tax", "hersteller", "warengruppenkategorie", "warengruppenfamilie",
//                    "warengruppe", "url", "ean", "lieferantenid"
        Query q = query.clone(TABLE_PRODUCTS);

        String[][] str = q.selectFreeQuery("SELECT produkte.Produktnummer,produkte.Name,produkte.Text," +
                "produkte.VK,produkte.EK,produkte.Tax,Hersteller," +
                "Warengruppenkategorien.name, warengruppenfamilien.name, warengruppengruppen.name,produkte.Url,produkte.EAN, produkte.lieferantenid FROM produkte " +
                "LEFT OUTER JOIN  lieferanten ON produkte.lieferantenid = lieferanten.id " +
                "LEFT OUTER JOIN  warengruppengruppen ON produkte.warengruppenid = warengruppengruppen.id " +
                "LEFT OUTER JOIN  warengruppenfamilien ON warengruppengruppen.familienid = warengruppenfamilien.id " +
                "LEFT OUTER JOIN  Warengruppenkategorien ON warengruppenfamilien.kategorieid = Warengruppenkategorien.id "
                );

        String string = "";
        for (int i = 0; i < str.length; i++) {
            string += str[i][0] + ";" + str[i][1] + ";" + str[i][2] + ";" + str[i][3] + ";" + str[i][4] 
                    + ";" + str[i][5] + ";" + str[i][6] + ";" + str[i][7] + ";" + str[i][8] + ";" + str[i][9] + ";" 
                    + str[i][10] + ";" + str[i][11] + ";" + str[i][12] +"\r\n";
        }
        
        
        return string;  
    }

    private String collect() {
        String str = "";
        
        str = str + "(;;2#4#1#1#8#0#;;)" + this.getNummer() + "(;;2#4#1#1#8#0#;;)" +  "(;;,;;)";
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getName() + "(;;2#4#1#1#8#0#;;)" +  "(;;,;;)";

        
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getText() +  "(;;2#4#1#1#8#0#;;)" + "(;;,;;)";
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getVK() + "(;;2#4#1#1#8#0#;;)" +  "(;;,;;)";
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getEK() +  "(;;2#4#1#1#8#0#;;)" + "(;;,;;)";
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getTAX() +  "(;;2#4#1#1#8#0#;;)" + "(;;,;;)";
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getHersteller() + "(;;2#4#1#1#8#0#;;)" + "(;;,;;)";
        str = str + this.getLieferantenId() + "(;;,;;)";
        str = str + this.getWarengruppenId()+ "(;;,;;)";        
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getDatum() +  "(;;2#4#1#1#8#0#;;)" + "(;;,;;)";
        str = str +  "(;;2#4#1#1#8#0#;;)" + this.getUrl() +  "(;;2#4#1#1#8#0#;;)" + "(;;,;;)";
        
        str = str + this.getEan();
        
        return str;
    }

    public void save() {

        if (id > 0) {
            this.update(TABLE_PRODUCTS_FIELDS, this.collect(), id.toString());

            isSaved = true;
        } else if (id == 0) {
           this.id = this.insert(TABLE_PRODUCTS_FIELDS, this.collect());
            

        } else {
        }
    }

    public String getWarengruppenId() {
        return WarengruppenId;
    }

    public String getProductgroupPath(){
    
        try {
            return ProductGroupHandler.instanceOf().getHierarchyPath(Integer.valueOf(this.getWarengruppenId()));


        } catch (NumberFormatException numberFormatException) {
            
            return "";
        }

    
    }
    
    public void setWarengruppenId(String Warengruppenid) {
        this.WarengruppenId = Warengruppenid;
        this.isSaved = false;
    }

//    private ProductFile[] getFiles(Query query) {
//        Query q = query.clone(TABLE_PRODUCTS_FILES);
//
//        String[] wher = {"productid", this.getId(), ""};
//
//        String[][] str = q.select("id", wher);
//        ProductFile[] prof = null;
//
//        for (int t = 0; t < str.length; t++) {
//
//            prof[t] = new ProductFile(query, str[0][t]);
//        }
//        return prof;
//    }

  

  



    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(compat.mp3.Supplier supplier) {

        
        if(supplier!=null) {
            this.setLieferantenId(supplier.getId());
        }else {
            this.setLieferantenId("0");
        }
        this.supplier = supplier;


        this.isSaved = false;
    }

//    public ProductFile[] getFiles() {
//        return files;
//    }

    public String getHersteller() {
        return Hersteller;
    }

    public void setHersteller(String Hersteller) {
        this.Hersteller = Hersteller;
    }

    public String getLieferantenId() {
        return LieferantenId;
    }

    public void setLieferantenId(String LieferantenId) {
        this.LieferantenId = LieferantenId;
    }

    public String getNummer() {
        return Nummer;
    }

    public void setNummer(String Nummer) {
        this.Nummer = Nummer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String Datum) {
        this.Datum = Datum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }

    public String getVK() {
        return VK;
    }

    public void setVK(String VK) {
        this.VK = VK;
    }

    public String getEK() {
        return EK;
    }

    public void setEK(String EK) {
        this.EK = EK;
    }

    public String getTAX() {
        return TAX;
    }

    public void setTAX(String TAX) {
        this.TAX = TAX;
    }

    public String getEan() {
        return Ean;
    }

    public void setEan(String Ean) {
        this.Ean = Ean;
    }


    public String[][] getPrintModel(){
    
        Query q = query.clone(TABLE_PRODUCTS);

        String[][] str = q.selectFreeQuery("SELECT produkte.Produktnummer AS Nummer,produkte.Name,produkte.Text," +
                "produkte.VK,produkte.EK,produkte.Tax,Hersteller,Lieferanten.firma AS Lieferant," +
                "Warengruppenid,produkte.Datum,produkte.EAN FROM produkte " +
                "LEFT OUTER JOIN  lieferanten ON produkte.lieferantenid = lieferanten.id " +
                "LEFT OUTER JOIN  warengruppengruppen ON produkte.warengruppenid = warengruppengruppen.id");
   
        return str;  
    }
}
