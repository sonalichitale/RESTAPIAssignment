package Assignment;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class Assignment1 {

    String data = null;
    PostJSON RJB;
    String user_id,name1;
    String response_body;
    Map <String,String> excelDataMap = new HashMap<String,String>();
    int count1;



    @BeforeClass
    public void setup()
    {
        RJB = new PostJSON();
    }

    @Parameters({"url"})
    @Test(priority = 0)
    public void post_data(String url)
    {
        read_And_Print_XL_AsPerTestData("Name");

        //System.out.println("test1");
        for (Map.Entry Hash:excelDataMap.entrySet())
        {
           // Create_Post_API ("https://petstore.swagger.io/v2/pet",Hash.getKey().toString(),Hash.getValue().toString());
            //getcall();
            Response res = given().
                    contentType(ContentType.JSON).
                    body(RJB.CreatePostJson(Hash.getKey().toString(),Hash.getValue().toString())).
                    when().
                    post(url);
            int status_code =res.statusCode();
            response_body =res.getBody().asString();
            System.out.println(status_code);
            System.out.println(response_body);
            user_id = res.getBody().jsonPath().getString("id");
            System.out.println("ID is "+user_id);
            name1 = res.getBody().jsonPath().getString("name");
            System.out.println("Name is "+name1);

            String Status = res.getBody().jsonPath().getString("status");
            System.out.println("Status is "+Status);
            Assert.assertEquals(Status,"available");
        }
    }


    @Parameters({"url","id","name"})
    @Test(priority = 1)
   public void getcall(String url,String id,String name2)
    {
        Response response =get(url+"/"+id);
        String data = response.getBody().asString();
        System.out.println(data);
        String id_get =response.getBody().jsonPath().getString("id");
        System.out.println("Get Id is "+id_get);
        String name_get =response.getBody().jsonPath().getString("name");
        System.out.println("Get name is "+name_get);
        Assert.assertEquals(id_get,id);
        Assert.assertEquals(name_get,name2);
    }

    @Parameters({"url","id","name"})
    @Test(priority = 2)
    public void deletecall(String url,String id,String name)
    {
        Response response1 = given().
                contentType(ContentType.JSON).
                body(RJB.CreatePostJson(id,name)).
                when().
                delete(url +"/"+ id);
        String response_body = response1.getBody().asString();
        System.out.println("Object with id ="+id+" has been deleted");

        //Get call after deleted data

        Response response2 =get(url+"/"+id);
        String data3 = response2.getBody().asString();
        System.out.println(data3);
    }


    public String read_And_Print_XL_AsPerTestData(String columname) {

        try {

            String XLFilePath = "C:\\Users\\sonalik\\IdeaProjects\\RESTAPIAssignment\\src\\AssignDog.xlsx";
            FileInputStream myxlfile = new FileInputStream(XLFilePath);
            Workbook workbook = new XSSFWorkbook(myxlfile);
            Sheet sheet = workbook.getSheet("XLdata");
            int lastRow = sheet.getLastRowNum();
            System.out.println("The last row which has data ==" + lastRow);
            Iterator<Row> rowIterator = sheet.iterator();
           Row headerRow = rowIterator.next();
            //Iterator<Cell> headerCellIterator = headerRow.cellIterator();

          /*  while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(1);
                String key = keyCell.getStringCellValue();
                String value = valueCell.getStringCellValue();
                excelDataMap.put(key, value);
            }*/
            for (int i=0;i<20;i++)
            {
                Row row = rowIterator.next();
                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(1);
                String key = keyCell.getStringCellValue();
                String value = valueCell.getStringCellValue();
                excelDataMap.put(key, value);
            }

            System.out.println("Excel data mapped to HashMap: " + excelDataMap);
            //count1 = excelDataMap.size();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }

}
